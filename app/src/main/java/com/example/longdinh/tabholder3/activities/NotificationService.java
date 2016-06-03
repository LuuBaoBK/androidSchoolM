package com.example.longdinh.tabholder3.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.longdinh.tabholder3.R;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by long dinh on 31/05/2016.
 */
public class NotificationService extends Service {
    private Pusher pusher = new Pusher(Constant.PUSHER_APP_KEY);
    String id = null;

    @Nullable
    @Override

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("---oncreate service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!= null){
            id = intent.getStringExtra("id");
            writeToInternalStorage("id_pusher.txt", id);
            String mid = readFromInternalStorage("id_pusher.txt");
            System.out.println("id has save: " + mid);
        }else{
            id = readFromInternalStorage("id_pusher.txt");
            System.out.println("id has save: " + id);
        }
        pusher.connect();

        pusher.unsubscribe(id+"-channel");
        System.out.println("---onstartcommand" + id);
        Channel channel = pusher.subscribe(id + "-channel");
        channel.bind("new_mail_event", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {
                System.out.println("---nhan them 1 notice moi");
                notifiy(data);
            }
        });

//        channel.bind("new_notice_event", new SubscriptionEventListener() {
//            @Override
//            public void onEvent(String channelName, String eventName, final String data) {
//                notifiy(data);
//            }
//        });
//        return Service.START_REDELIVER_INTENT;
//        return Service.START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        Intent intent = new Intent("com.example.longdinh.tabholder3.activities");
        intent.putExtra("key", "locdinh");
        super.onDestroy();
    }


    public void notifiy(String data){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("NoticeService");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, myIntent, 0);
        Context context = getApplicationContext();
        Notification.Builder builder;
        builder = new  Notification.Builder(context)
                .setContentTitle("Thông báo mới")
                .setContentText(data)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.icon_education);
        Notification notification = builder.getNotification();
        NotificationManager notificationManager  = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    public void writeToInternalStorage(String fileName,String userName)
    {
        try{
            String endOfLine = System.getProperty("line.separator");
            StringBuffer buffer = new StringBuffer();

            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE); //// MODE_PRIVATE will create the file (or replace a file of the same name) and make it private to your application. Other modes available are: MODE_APPEND, MODE_WORLD_READABLE, and MODE_WORLD_WRITEABLE.

            buffer.append(userName.toString() + endOfLine);
            fos.write(buffer.toString().getBytes());
            //      writer.write(userName);

            fos.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public String readFromInternalStorage(String fileName)
    {
        try{
            File file = this.getFileStreamPath(fileName);
            if(file.exists() == true)
            {
                FileInputStream fis = openFileInput(fileName);
                StringBuilder buffer = new StringBuilder();
                int ch;
                while( (ch = fis.read()) != -1){
                    buffer.append((char)ch);
                }

                fis.close();

                return buffer.toString();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }

}
