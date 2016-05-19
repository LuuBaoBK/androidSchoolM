package com.example.longdinh.tabholder3.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.adapters.MyExpandableListAdapter;
import com.example.longdinh.tabholder3.fragments.MyClass;
import com.example.longdinh.tabholder3.fragments.MyProfile;
import com.example.longdinh.tabholder3.fragments.NoticeBoardParent;
import com.example.longdinh.tabholder3.fragments.NoticeBoardStudent;
import com.example.longdinh.tabholder3.fragments.NoticeTeacher;
import com.example.longdinh.tabholder3.fragments.Schedule_Parent;
import com.example.longdinh.tabholder3.fragments.Schedule_Person;
import com.example.longdinh.tabholder3.fragments.Transcript_Show;
import com.example.longdinh.tabholder3.fragments.Transcript_Show_Student;
import com.example.longdinh.tabholder3.inner_fragments.Tab1Fragment;
import com.example.longdinh.tabholder3.inner_fragments.Tab2Fragment;
import com.example.longdinh.tabholder3.inner_fragments.Tab3Fragment;
import com.example.longdinh.tabholder3.inner_fragments.Tab4Fragment;
import com.example.longdinh.tabholder3.inner_fragments.Tab5Fragment;
import com.example.longdinh.tabholder3.models.EmailItem;
import com.example.longdinh.tabholder3.models.NavItem;
import com.example.longdinh.tabholder3.models.NavItemChild;
import com.example.longdinh.tabholder3.models.StudentItemSpinner;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private int lastExpandedPosition = -1;

    //using for saving and loading data saved
    Boolean isSaved = false;
    List<EmailItem>        InboxMailList = new ArrayList<EmailItem>();
    List<String>        InboxReadMail = new ArrayList<String>();
    List<String>        InboxDeleteMail = new ArrayList<String>();

    List<EmailItem>        OutboxMailList = new ArrayList<EmailItem>();

    List<EmailItem>        SendMailList = new ArrayList<EmailItem>();
    List<String>        SendDeleteMail = new ArrayList<String>();


    List<EmailItem>        DraftMailList = new ArrayList<EmailItem>();
    List<String>        DraftNewMail = new ArrayList<String>();
    List<String>        DraftDeleteMail = new ArrayList<String>();



    DrawerLayout drawerLayout;
    RelativeLayout drawerPane;
    ListView lvNav;
    TextView tvUserId;
    TextView tvFullname;
    TextView tvRole;
    CircleImageView ivIcon;
    int offsetNavList = 0;
    MyExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<NavItem> listDataHeader;
    List<NavItemChild> mailList;
    HashMap<String, List<NavItemChild>> listDataChild;
    List<NavItem> listNavItems;
    List<Fragment> listFragments;
//    List<String> listMaChild;// using for user parent for getschedule
//    List<String> listFullnameChild;// using for user parent show item navlist
    List<StudentItemSpinner> listChildren;
    String id;
    String token;
    String role;
    MyApplication app;
    int numChildren = 0;


    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);


        Pusher pusher = new Pusher("APP_KEY");
        pusher.connect();
        Channel channel = pusher.subscribe("my-channel");
        channel.bind("my-event", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {
                System.out.println(data + "-------");
            }
        });



        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerPane = (RelativeLayout) findViewById(R.id.drawer_pane);
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        tvFullname = (TextView) findViewById(R.id.tvFullname);
        tvRole = (TextView) findViewById(R.id.tvRole);
        tvUserId = (TextView) findViewById(R.id.tvUserId);
        app = (MyApplication)getApplicationContext();


//        Intent login = getIntent();
//        String data = login.getStringExtra("userinfo_string");
        String data = "{\"id\":\"t_00000013\",\"email\":\"t_0000013@schoolm.com\",\"role\":\"2\",\"fullname\":\"Trịnh Hiếu Vân\",\"token\":\"4ad2b006ff575c89d0c30fdf8b5f2b6a9f4b6a90\"}";
//        String data = "\"id\":\"a_0000000\",\"email\":\"a_0000000@schoolm.com\",\"role\":\"0\",\"fullname\":\"V\\u0103n H\\u1ea1 \\u0110\\u1ea1t\",\"token\":\"e4f5afc50773ae5b06b0b84c3c9d74cb341c9869\"";
        //su dung rieng cho parent
//        String data = "{\"id\":\"t_00000013\",\"email\":\"t_0000013@schoolm.com\",\"role\":\"3\",\"fullname\":\"TrịnhHiếuVân\",\"token\":\"4ad2b006ff575c89d0c30fdf8b5f2b6a9f4b6a90\",\"numchild\":2,\"children\":[{\"ma\":\"s_0000003\",\"fullname\":\"Nguyến Đinh Mai\"},{\"ma\":\"s_0000004\",\"fullname\":\"Nguyễn Phạn Hùng\"}]}";
        System.out.println(data + "----");
        try {
            JSONObject user = new JSONObject(data);
            id = user.getString("id");
            token = user.getString("token");

            app.setId(id);
            app.setToken(token);
            app.setFullName(user.getString("fullname"));

            tvFullname.setText(user.getString("fullname"));
            tvUserId.setText(id);

            role = user.getString("role");
            app.setRole(role);
            if(role.equals("0")){
                tvRole.setText("Admin");
                offsetNavList = 1;
            }else if (role.equals("1")){
                tvRole.setText("Teacher");
                offsetNavList = 4;
            }else if(role.equals("3")){
                tvRole.setText("Parent");
                numChildren = user.getInt("numchild");
                offsetNavList = 4;
                listChildren  = new ArrayList<>();
                JSONArray children = user.getJSONArray("children");
                listChildren.add(new StudentItemSpinner("0", "Choose a student"));
                for(int i = 0; i < numChildren; i++){
                    listChildren.add(new  StudentItemSpinner(children.getJSONObject(i).getString("ma"), children.getJSONObject(i).getString("fullname")));
                }
                app.setListchildren(listChildren);
            }else{
                tvRole.setText("Student");
                offsetNavList = 4;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            finish();
        }

        listFragments = new ArrayList<Fragment>();

        listFragments.add(new MyProfile());
        if(role.equals("0")){
            //do nothing
        }else if(role.equals("1")){
            listFragments.add(new MyClass());
            listFragments.add(new Schedule_Person());
            listFragments.add(new NoticeTeacher());
        }else if(role.equals("3")){
            listFragments.add(new Schedule_Parent());
            listFragments.add(new Transcript_Show());
            listFragments.add(new NoticeBoardParent());
        }else if(role.equals("2")){
            listFragments.add(new Schedule_Person());
            listFragments.add(new Transcript_Show_Student());
            listFragments.add(new NoticeBoardStudent());
        }

        listFragments.add(new Tab1Fragment());
        listFragments.add(new Tab2Fragment());
        listFragments.add(new Tab3Fragment());
        listFragments.add(new Tab4Fragment());
        listFragments.add(new Tab5Fragment());

        prepareListData();
        listAdapter = new MyExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        // load first fragment as default:
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_content, listFragments.get(0)).commit();

        setTitle(listDataHeader.get(0).getTitle());

//        lvNav.setItemChecked(0, true);
        expListView.setSelectedGroup(0);
        drawerLayout.closeDrawer(drawerPane);
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (listAdapter.getChildrenCount(groupPosition) == 0) {// neu nhu khong co con thi xu li thang
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.main_content, listFragments.get(groupPosition))
                            .commit();
                    setTitle(listDataHeader.get(groupPosition).getTitle());
                    drawerLayout.closeDrawer(drawerPane);
                }
                return false;
            }
        });


        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                expListView.expandGroup(groupPosition);
                expListView.setSelectedChild(groupPosition, childPosition, true);
                System.out.println("--------item nav--------print:" + groupPosition + childPosition);


                List<NavItemChild> mailList = listDataChild.get(listDataHeader.get(offsetNavList).getTitle());
                String title = new String();
                title = "MailBox-" + mailList.get(childPosition).getTitle();

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.main_content, listFragments.get(offsetNavList + childPosition))
                        .commit();
                setTitle(title);


                drawerLayout.closeDrawer(drawerPane);
                return false;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;

            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_opened, R.string.drawer_closed) {

            @Override
            public void onDrawerOpened(View drawerView) {
                // TODO Auto-generated method stub
                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // TODO Auto-generated method stub
                invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }

        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        System.out.println("nearly come to end of create-----");
        loadingdata();
        System.out.println("the end of creating-----");


        listAdapter.notifyDataSetChanged();
        System.out.println("da thay doi num of mail box-----");

    }



    // beginphan thiet lap ve luu data
    public void creatingData(){
        //create  data sharing

        System.out.println("creating data------");
        InboxMailList.add(new EmailItem(0, "Thu moi hop 1", "Feb 28", "vanminh@hostmail.com", "Kinh thi moi quy phu huynh...", true));
        InboxMailList.add(new EmailItem(1, "Thu moi hop 1", "Feb 27", "vanminh@hostmail.com", "Kinh  vam moi quy phu huynh...", true));
        InboxMailList.add(new EmailItem(2, "Thu hoc phi 1", "Jan 21", "giaovien_@van.com", "Thong bao nop hoc phi...", true));
        InboxMailList.add(new EmailItem(3, "Thu hoc phi 1", "Jan 20", "giaovien_@van.com", "Thong bao nop hoc phi...", true));
        app.setData_InboxMailList(InboxMailList);



        SendMailList = new ArrayList<EmailItem>();
        SendMailList.add(new EmailItem(4, "Thu moi hop 2", "Feb 28", "vanminh@hostmail.com", "Kinh moi quy phu huynh..."));
        SendMailList.add(new EmailItem(5, "Thu moi hop 2", "Feb 28", "vanminh@hostmail.com", "Kinh moi quy phu huynh..."));
        SendMailList.add(new EmailItem(6, "Thu hoc phi 2", "Jan 21", "giaovien_@van.com", "Thong bao nop hoc phi..."));
        SendMailList.add(new EmailItem(7, "Thu hoc phi 2", "Jan 21", "giaovien_@van.com", "Thong bao nop hoc phi..."));
        app.setData_SendMailList(SendMailList);

        DraftMailList = new ArrayList<EmailItem>();
        DraftMailList.add(new EmailItem(8, "Thu moi hop 3", "Feb 28", "vanminh@hostmail.com", "Kinh moi quy phu huynh..."));
        DraftMailList.add(new EmailItem(9, "Thu hoc phi 3", "Jan 21", "giaovien_@van.com", "Thong bao nop hoc phi..."));
        DraftMailList.add(new EmailItem(10, "Hop hoi dong 3", "July 8", "hoidong@gmail.com", "Sap co thoi khoa bieu moi..."));
        DraftMailList.add(new EmailItem(11, "Hop hoi dong 3", "July 1", "hoidong@gmail.com", "Sap co thoi khoa bieu moi..."));
        app.setData_DraftMailList(DraftMailList);






        OutboxMailList = new ArrayList<EmailItem>();
        OutboxMailList.add(new EmailItem(9, "Thu moi hop 5", "Feb 28", "vanminh@hostmail.com", "Kinh moi quy phu huynh..."));
        OutboxMailList.add(new EmailItem(12,"Thu hoc phi 5", "Jan 27", "giaovien_@van.com", "Thong bao nop hoc phi..."));
        OutboxMailList.add(new EmailItem(13,"Thu moi hop 5", "Feb 22", "vanminh@hostmail.com", "Kinh moi quy phu huynh..."));
        OutboxMailList.add(new EmailItem(14,"Thu hoc phi 5", "Jan 21", "giaovien_@van.com", "Thong bao nop hoc phi..."));

        app.setData_OutboxMailList(OutboxMailList);




    };

    public void loadingdata(){
        System.out.println("loading data------");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        isSaved = sp.getBoolean("isSaved", false);
        if(isSaved){
            /// using for inbox mail
            int numInbox = sp.getInt("numInbox", 0);
            for (int i = 0; i < numInbox; i++){
                int id = sp.getInt("idInbox"+ i, 0);
                String subject = sp.getString("subjectInbox" + i, "");
                String date= sp.getString("dateInbox"+ i, "");
                String sender= sp.getString("senderInbox"+ i, "");
                String preview= sp.getString("previewInbox" + i, "");
                System.out.println("mail---- " + i + ", " + id + ", " + subject + ", " + date + ", " + sender + ", " + preview);
                InboxMailList.add(new EmailItem(id, subject, date, sender, preview));
            }
            app.setData_InboxMailList(InboxMailList);

            int numReadInbox = sp.getInt("numReadInbox", 0);
            for (int i = 0; i < numReadInbox; i++){
                int id = sp.getInt("idReadInbox" + i, 0);
                System.out.println("mail read inbox---- " + i + ", " + id);
                InboxReadMail.add(id + "");
            }
            app.setInboxReadMail(InboxReadMail);

            int numDeleteInbox = sp.getInt("numDeleteInbox", 0);
            for (int i = 0; i < numDeleteInbox; i++){
                int id = sp.getInt("idDeleteInbox" + i, 0);
                System.out.println("mail delete inbox---- " + i + ", " + id);
                InboxDeleteMail.add(id + "");
            }
            System.out.println("InboxDeleteMail is ---" + InboxDeleteMail);

            app.setInboxDeleteMail(InboxDeleteMail);



            /// using for outbox mail
            int num = sp.getInt("numOutbox", 0);
            for (int i = 0; i < num; i++){
                int id = sp.getInt("idOutbox"+ i, 0);
                String subject = sp.getString("subjectOutbox" + i, "");
                String date= sp.getString("dateOutbox"+ i, "");
                String sender= sp.getString("senderOutbox"+ i, "");
                String preview= sp.getString("previewOutbox" + i, "");
                System.out.println("mail---- " + i + ", " + id + ", " + subject + ", " + date + ", " + sender + ", " + preview);
                OutboxMailList.add(new EmailItem(id, subject, date, sender, preview));
            }
            app.setData_OutboxMailList(OutboxMailList);




            //loading cho mail send
            int numSend = sp.getInt("numSend", 0);
            for (int i = 0; i < numSend; i++){
                int id = sp.getInt("idSend"+ i, 0);
                String subject = sp.getString("subjectSend" + i, "");
                String date= sp.getString("dateSend"+ i, "");
                String sender= sp.getString("senderSend"+ i, "");
                String preview= sp.getString("previewSend" + i, "");
                System.out.println("mail---- " + i + ", " + id + ", " + subject + ", " + date + ", " + sender + ", " + preview);
                SendMailList.add(new EmailItem(id, subject, date, sender, preview));
            }
            app.setData_SendMailList(SendMailList);

            int numDeleteSend = sp.getInt("numDeleteSend", 0);
            for (int i = 0; i < numDeleteSend; i++){
                int id = sp.getInt("idDeleteSend" + i, 0);
                System.out.println("mail delete Send---- " + i + ", " + id);
                SendDeleteMail.add(id + "");
            }
            System.out.println("InboxDeleteMail is ---" + SendDeleteMail);

            app.setSendDeleteMail(SendDeleteMail);


            //loading cho mail draft
            int numDraft = sp.getInt("numDraft", 0);
            for (int i = 0; i < numDraft; i++){
                int id = sp.getInt("idDraft"+ i, 0);
                String subject = sp.getString("subjectDraft" + i, "");
                String date= sp.getString("dateDraft"+ i, "");
                String sender= sp.getString("senderDraft"+ i, "");
                String preview= sp.getString("previewDraft" + i, "");
                System.out.println("mail---- " + i + ", " + id + ", " + subject + ", " + date + ", " + sender + ", " + preview);
                DraftMailList.add(new EmailItem(id, subject, date, sender, preview));
            }
            app.setData_DraftMailList(DraftMailList);

            int numNewDraft = sp.getInt("numNewDraft", 0);
            for (int i = 0; i < numNewDraft; i++){
                int id = sp.getInt("idNewDraft" + i, 0);
                System.out.println("mail NewDraft---- " + i + ", " + id);
                DraftNewMail.add(id + "");
            }
            app.setDraftNewMail(DraftNewMail);

            int numDeleteDraft = sp.getInt("numDeleteDraft", 0);
            for (int i = 0; i < numDeleteDraft; i++){
                int id = sp.getInt("idDeleteDraft" + i, 0);
                System.out.println("mail delete Draft---- " + i + ", " + id);
                DraftDeleteMail.add(id + "");
            }
            System.out.println("DraftDeleteMail is ---" + DraftDeleteMail);

            app.setDraftDeleteMail(DraftDeleteMail);




        }else{
            System.out.println("loadding from creating------");
            creatingData();
        }
    };

    public void savingData(){
        System.out.println("begin saving data------");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();

        //for saving data from inbox mail
        editor.putBoolean("isSaved", false);
        int numInbox = InboxMailList.size();
        editor.putInt("numInbox", numInbox);
        for (int i = 0; i < numInbox; i++){
            System.out.println("numInboum mail saving----" + numInbox);
            editor.putInt("idInbox" + i, InboxMailList.get(i).getId());
            editor.putString("subjectInbox" + i, InboxMailList.get(i).getSubject());
            editor.putString("dateInbox" + i, InboxMailList.get(i).getDate());
            editor.putString("senderInbox" + i, InboxMailList.get(i).getSender());
            editor.putString("previewInbox" + i, InboxMailList.get(i).getPreview());
        }

        int numReadInbox = InboxReadMail.size();
        editor.putInt("numReadInbox", numReadInbox);
        for (int i = 0; i < numReadInbox; i++) {
            editor.putInt("idReadInbox" + i, Integer.parseInt(InboxReadMail.get(i)));
            System.out.println("mail read inbox---- " + i + InboxReadMail.get(i));
        }


        int numDeleteInbox = InboxDeleteMail.size();
        System.out.println("size  of delete inbox is---- " + numDeleteInbox);
        editor.putInt("numDeleteInbox", numDeleteInbox);
        for (int i = 0; i < numDeleteInbox; i++) {
            editor.putInt("idDeleteInbox" + i, Integer.parseInt(InboxDeleteMail.get(i)));
            System.out.println("mail Delete inbox---- " + i + InboxDeleteMail.get(i));
        }




        //using for saving mail send
        int numSend = SendMailList.size();
        editor.putInt("numSend", numSend);
        for (int i = 0; i < numSend; i++){
            System.out.println("num mail saving----" + numSend);
            editor.putInt("idSend" + i, SendMailList.get(i).getId());
            editor.putString("subjectSend" + i, SendMailList.get(i).getSubject());
            editor.putString("dateSend" + i, SendMailList.get(i).getDate());
            editor.putString("senderSend" + i, SendMailList.get(i).getSender());
            editor.putString("previewSend" + i, SendMailList.get(i).getPreview());
        }

        int numDeleteSend = SendDeleteMail.size();
        editor.putInt("numDeleteSend", numDeleteSend);
        for (int i = 0; i < numDeleteSend; i++) {
            editor.putInt("idDeleteSend" + i, Integer.parseInt(SendDeleteMail.get(i)));
            System.out.println("mail read inbox---- " + i + SendDeleteMail.get(i));
        }


        //using for mail draft
        int numDraft = DraftMailList.size();
        editor.putInt("numDraft", numDraft);
        for (int i = 0; i < numDraft; i++){
            System.out.println("num mail saving----" + numDraft);
            editor.putInt("idDraft" + i, DraftMailList.get(i).getId());
            editor.putString("subjectDraft" + i, DraftMailList.get(i).getSubject());
            editor.putString("dateDraft" + i, DraftMailList.get(i).getDate());
            editor.putString("senderDraft" + i, DraftMailList.get(i).getSender());
            editor.putString("previewDraft" + i, DraftMailList.get(i).getPreview());
        }

        int numDeleteDraft = DraftDeleteMail.size();
        editor.putInt("numDeleteDraft", numDeleteDraft);
        for (int i = 0; i < numDeleteDraft; i++) {
            editor.putInt("idDeleteDraft" + i, Integer.parseInt(DraftDeleteMail.get(i)));
            System.out.println("mail read inbox---- " + i + DraftDeleteMail.get(i));
        }

        int numNewDraft = DraftNewMail.size();
        editor.putInt("numNewDraft", numNewDraft);
        for (int i = 0; i < numNewDraft; i++) {
            editor.putInt("idNewDraft" + i, Integer.parseInt(DraftNewMail.get(i)));
            System.out.println("mail new draft---- " + i + DraftNewMail.get(i));
        }
        //end for draft

        // using for mail outbox
        int numOutbox = OutboxMailList.size();
        editor.putInt("numOutbox", numOutbox);
        for (int i = 0; i < numOutbox; i++){
            System.out.println("num mail saving----" + numOutbox);
            editor.putInt("idOutbox" + i, OutboxMailList.get(i).getId());
            editor.putString("subjectOutbox" + i, OutboxMailList.get(i).getSubject());
            editor.putString("dateOutbox" + i, OutboxMailList.get(i).getDate());
            editor.putString("senderOutbox" + i, OutboxMailList.get(i).getSender());
            editor.putString("previewOutbox" + i, OutboxMailList.get(i).getPreview());
        }
        //end for outbox

        editor.commit();
        System.out.println("committed------");

    }
    //end  thiet lap


    @Override
    protected void onStart() {
        super.onStart();
        Pusher pusher = new Pusher(Constant.PUSHER_APP_KEY);
        pusher.connect();
        Channel channel = pusher.subscribe("my-channel");
        channel.bind("my-event", new SubscriptionEventListener() {

            @Override
            public void onEvent(String channelName, String eventName, final String data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notice();
                    }
                });
                System.out.println("abchahaha");

            }
        });



    }

    public void notice(){
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, LoginActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        Notification n  = new Notification.Builder(this)
                .setContentTitle("New mail from " + "test@gmail.com")
                .setContentText("Subject" + mailList.get(0).getNum() + 1)
                .setSmallIcon(R.drawable.notification_template_icon_bg)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
//                .addAction(R.drawable.notification_template_icon_bg, "Call", pIntent)
//                .addAction(R.drawable.notification_template_icon_bg, "More", pIntent)
//                .addAction(R.drawable.notification_template_icon_bg, "And more", pIntent)
                .build();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
        System.out.println("noticed");
        mailList.get(0).setNum(mailList.get(0).getNum() + 1);
        listAdapter.notifyDataSetChanged();

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<NavItem>();
        listDataChild = new HashMap<String, List<NavItemChild>>();

        ///---------------------------------------------------------
        if(role.equals("0")){
            listDataHeader.add(new NavItem("Profile", R.drawable.icon_profile));
            listDataHeader.add(new NavItem("MailBox",R.drawable.icon_mailbox1));
        }else if(role.equals("1")){
            listDataHeader.add(new NavItem("Profile", R.drawable.icon_profile));
            listDataHeader.add(new NavItem("Class",  R.drawable.icon_class));
            listDataHeader.add(new NavItem("Schedule", R.drawable.icon_schedule));
            listDataHeader.add(new NavItem("Notice Board",  R.drawable.icon_notice));
            listDataHeader.add(new NavItem("MailBox",R.drawable.icon_mailbox1));

        }else if(role.equals("3")){
            listDataHeader.add(new NavItem("Profile", R.drawable.icon_profile));
            listDataHeader.add(new NavItem("Schedule", R.drawable.icon_schedule));
            listDataHeader.add(new NavItem("Transcript",  R.drawable.icon_notice));
            listDataHeader.add(new NavItem("Notice Board",  R.drawable.icon_notice));
            listDataHeader.add(new NavItem("MailBox",R.drawable.icon_mailbox1));
        }else if(role.equals("2")){
            listDataHeader.add(new NavItem("Profile", R.drawable.icon_profile));
            listDataHeader.add(new NavItem("Schedule", R.drawable.icon_schedule));
            listDataHeader.add(new NavItem("Transcript",  R.drawable.icon_notice));
            listDataHeader.add(new NavItem("Notice Board",  R.drawable.icon_notice));
            listDataHeader.add(new NavItem("MailBox",R.drawable.icon_mailbox1));
        }


        // Adding child data
        mailList = new ArrayList<NavItemChild>();
        mailList.add(new NavItemChild("Inbox", R.drawable.icon_inbox));
        mailList.add(new NavItemChild("Sent",  R.drawable.icon_send));
        mailList.add(new NavItemChild("Drafts", R.drawable.icon_draft));
        mailList.add(new NavItemChild("Trash",R.drawable.icon_trash));
        mailList.add(new NavItemChild("Outbox",R.drawable.icon_outbox));
        listDataChild.put(listDataHeader.get(offsetNavList).getTitle(), mailList); // Header, Child data


        List<EmailItem>        TrashMailList = new ArrayList<EmailItem>();
        TrashMailList.add(new EmailItem(8, "Thu moi hop 4", "Feb 28", "vanminh@hostmail.com", "Kinh moi quy phu huynh..."));
        TrashMailList.add(new EmailItem(10,"Thu moi hop 4", "Feb 28", "vanminh@hostmail.com", "Kinh moi quy phu huynh..."));
        TrashMailList.add(new EmailItem(11,"Thu hoc phi 4", "Jan 21", "giaovien_@van.com", "Thong bao nop hoc phi..."));
        app.setData_TrashMailList(TrashMailList);

    }


    @Override
    protected void onPause() {
        super.onPause();
        savingData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the MyHome/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}
