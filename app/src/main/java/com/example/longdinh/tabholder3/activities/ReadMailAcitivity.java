package com.example.longdinh.tabholder3.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.models.EmailItem;
import com.example.longdinh.tabholder3.models.NavItemChild;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by long dinh on 15/05/2016.
 */
public class ReadMailAcitivity extends Activity{

    TextView tvSubject;
    TextView tvStand;
    TextView tvSender;
    TextView tvNguoiNhan;
    TextView tvDate;
    TextView tvContent;
    LinearLayout btnReply;
    LinearLayout btnForward;
    String id;
    Boolean isTrashMail = false;
    MyApplication app;
    String typeMail;
    RequestManager requestManager = new RequestManager();
    final int SIZE_INBOXMAIL_DETAIL = 5;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mail_read);

        app = (MyApplication) getApplication();

        tvSubject = (TextView) findViewById(R.id.tvSubject);
         tvStand= (TextView) findViewById(R.id.tvStand);
         tvSender= (TextView) findViewById(R.id.tvSender);
         tvNguoiNhan= (TextView) findViewById(R.id.tvNguoiNhan);
         tvDate= (TextView) findViewById(R.id.tvDate);
         tvContent= (TextView) findViewById(R.id.tvContent);
        btnReply = (LinearLayout) findViewById(R.id.btnReply);
         btnForward = (LinearLayout) findViewById(R.id.btnForward);


        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        typeMail = intent.getStringExtra("typeMail");
        new getMailDetail().execute(id);

        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //khi forward thikhong cn luu lai thong tin cua mail vi no tao ra mail moi thi khong cna thiet phai lam nhu vay.
                Intent intent = new Intent(getApplicationContext(), MailContent.class);
                intent.putExtra("type", "FORWARD");
                intent.putExtra("content", tvContent.getText().toString());
                intent.putExtra("subject", tvSubject.getText());
                intent.putExtra("isTrashMail", isTrashMail);
                startActivityForResult(intent, 123);
                System.out.println("da kich hoat su kien forward"  + tvContent.getText());
            }
        });


        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MailContent.class);
                intent.putExtra("type", "REPLY");
                if( tvNguoiNhan.getText().equals("To me")){
                    intent.putExtra("sender", tvSender.getText().toString().substring(5));
                }else{
                    intent.putExtra("sender", tvNguoiNhan.getText());
                }
                intent.putExtra("subject", tvSubject.getText());
                intent.putExtra("isTrashMail", isTrashMail);
                startActivityForResult(intent, 321);
                System.out.println("da kich hoat su kien reply");
            }
        });
    }

    public class getMailDetail extends AsyncTask<String, String , String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            System.out.println("-dang doc mail detail----");

            if(typeMail.equals("inbox")){
                List<EmailItem> inboxMailList = app.getData_InboxMailList();
                for(int i = 0; i < SIZE_INBOXMAIL_DETAIL; i++){
                    EmailItem email = inboxMailList.get(i);
                    if(id.equals(email.getId()+ "")){
                        if(email.getIsRead()){
                            NavItemChild item = app.getNumMailinbox();
                            item.setNum(item.getNum()-1);
                            app.notifyChangeNumInbox();
                        }
                        System.out.println("read mail inbox local -----");
                        if(isOnline()){
                            String data = "read="+email.getId()+",";
                            requestManager.postDataToServer("api/post/mailbox/update_log",app.getToken(),data);
                        }
                        else{
                            app.getInboxReadMail().add(email.getId()+"");
                        }
                        return email.toString();
                    }
                }
                if(isOnline()){
                    System.out.println("read mail inbox online -----");
                // gui requset len server
//                    String retur = "{\"id\":-1,\"content\":\"Noi dung khong quan trong chay dung la dc\",\"title\":\"Mail sent to server\",\"date_time\":\"Apr 29\",\"author\":\"t0001@schoolm.com\",\"receiver\":\"t_000002@schoolm.com\"}";
                    String retur = requestManager.postDataToServer("api/post/mailbox/read_mail",app.getToken(),"mail_id="+id);
                    System.out.println(retur);
                    return retur;
                }
            }else if(typeMail.equals("outbox")){// mail box chi co  luu o local nen khong cna phai gui request len server
                List<EmailItem> outboxMailList = app.getData_OutboxMailList();
                for(int i = 0; i < outboxMailList.size(); i++){
                    EmailItem email = outboxMailList.get(i);
                    if(id.equals(email.getId()+ "")){
                        System.out.println("read mail outbox local -----");
                        return email.toString();
                    }
                }
            }else if(typeMail.equals("send")){// xu li co giong voi truong hop mai inbox ahy khong
                List<EmailItem> sendMailList = app.getData_SendMailList();
                int sizeCheck =  isOnline()? SIZE_INBOXMAIL_DETAIL :sendMailList.size();
                for(int i = 0; i < sizeCheck; i++){
                    EmailItem email = sendMailList.get(i);
                    if(id.equals(email.getId()+ "")){
                        System.out.println("read mail send local -----");
                        return email.toString();
                    }
                }

                //GUI REQUEST LEN SERVER DE LAY DETAIL MAIL
                if(isOnline()){
                    System.out.println("read mail send online -----");
//                    String retur = "{\"id\":-1,\"content\":\"Noi dung khong quan trong chay dung la dc\",\"title\":\"Mail sent to server\",\"date_time\":\"Apr 29\",\"author\":\"t0001@schoolm.com\",\"receiver\":\"t_000002@schoolm.com\"}";
                    String retur = requestManager.postDataToServer("api/post/mailbox/read_mail",app.getToken(),"mail_id="+id);
                    return retur;
                }
            }else if(typeMail.equals("trash")){// xu li co giong voi truong hop mai inbox ahy khong
                if(isOnline()){
                    System.out.println("read mail trash online -----");
//                    String retur = "{\"id\":-1,\"content\":\"Noi dung khong quan trong chay dung la dc\",\"title\":\"Mail sent to server\",\"date_time\":\"Apr 29\",\"author\":\"t0001@schoolm.com\",\"receiver\":\"t_000002@schoolm.com\"}";
                    String retur = requestManager.postDataToServer("api/post/mailbox/read_mail",app.getToken(),"mail_id="+id);
                    return retur;
                }
            }else{
//                String retur = "{\"id\":-1,\"content\":\"Noi dung khong quan trong chay dung la dc\",\"title\":\"Mail sent to server\",\"date_time\":\"Apr 29\",\"author\":\"t0001@schoolm.com\",\"receiver\":\"t_000002@schoolm.com\"}";
                String retur = requestManager.postDataToServer("api/post/mailbox/read_mail",app.getToken(),"mail_id="+id);
                return retur;
            }
            return null;//truong hop khong co trong mail ma cung khong the online
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result == null){
                Toast.makeText(getApplication(), "Please connect internet", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                System.out.println(result);
                JSONObject email = new JSONObject(result);
                tvStand.setText(Character.toString(Character.toUpperCase(email.getString("author").charAt(0))));
                tvSubject.setText(email.getString("title"));
                tvSender.setText(email.getString("author"));
                tvNguoiNhan.setText(email.getString("receiver"));
                tvDate.setText(email.getString("date_time"));
                tvContent.setText(Html.fromHtml(email.getString("content")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
