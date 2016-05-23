package com.example.longdinh.tabholder3.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.SyncMail.SyncDraftMail;
import com.example.longdinh.tabholder3.SyncMail.SyncOutboxMail;
import com.example.longdinh.tabholder3.SyncMail.SyncReadOrDeleteMail;
import com.example.longdinh.tabholder3.adapters.MyExpandableListAdapter;
import com.example.longdinh.tabholder3.fragments.MyClass;
import com.example.longdinh.tabholder3.fragments.MyMail;
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
    private Pusher pusher = new Pusher(Constant.PUSHER_APP_KEY);
    private int num_new_mail = 0;

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



    public DrawerLayout drawerLayout;
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
    List<StudentItemSpinner> listChildren;
    String id = "";
    String token;
    String role;
    MyApplication app;
    int numChildren = 0;
    String dataInfo;
    Boolean saveDataLogin = true;


    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onStop() {
        super.onStop();
        pusher.disconnect();
    }

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




        if(isOnline()){
            Toast.makeText(this, "Co mang", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Vui long ket noi mang", Toast.LENGTH_SHORT).show();
        }


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerPane = (RelativeLayout) findViewById(R.id.drawer_pane);
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        tvFullname = (TextView) findViewById(R.id.tvFullname);
        tvRole = (TextView) findViewById(R.id.tvRole);
        tvUserId = (TextView) findViewById(R.id.tvUserId);
        app = (MyApplication)getApplicationContext();


        Intent login = getIntent();
//        dataInfo = login.getStringExtra("userinfo_string");
//        dataInfo = "{\"id\":\"t_00000013\",\"email\":\"t_0000013@schoolm.com\",\"role\":\"2\",\"fullname\":\"Trịnh Hiếu Vân\",\"token\":\"4ad2b006ff575c89d0c30fdf8b5f2b6a9f4b6a90\"}";
        dataInfo = "{\"id\":\"a_0000000\",\"email\":\"a_0000000@schoolm.com\",\"role\":\"1\",\"fullname\":\"V\\u0103n H\\u1ea1 \\u0110\\u1ea1t\",\"token\":\"e4f5afc50773ae5b06b0b84c3c9d74cb341c9869\"}";
        //su dung rieng cho parent
//        String dataInfo = "{\"id\":\"t_00000013\",\"email\":\"t_0000013@schoolm.com\",\"role\":\"3\",\"fullname\":\"TrịnhHiếuVân\",\"token\":\"4ad2b006ff575c89d0c30fdf8b5f2b6a9f4b6a90\",\"numchild\":2,\"children\":[{\"ma\":\"s_0000003\",\"fullname\":\"Nguyến Đinh Mai\"},{\"ma\":\"s_0000004\",\"fullname\":\"Nguyễn Phạn Hùng\"}]}";
        System.out.println(dataInfo + "----");
        try {
            JSONObject user = new JSONObject(dataInfo);
            id = user.getString("id");
            token = user.getString("token");

            app.setId(id);
            app.setToken(token);
            app.setFullName(user.getString("fullname"));
            num_new_mail = user.getInt("num_new_mail");

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

        listFragments.add(new MyMail());
        listFragments.add(new Tab2Fragment());
        listFragments.add(new Tab3Fragment());
        listFragments.add(new Tab4Fragment());
        listFragments.add(new Tab5Fragment());

        prepareListData();
        listAdapter = new MyExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        app.setListAdapter(listAdapter);

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

//        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//            getActivity().setDisplayShowTitleEnabled(false);
//        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActivity().getActionBar().setHomeButtonEnabled(true);//co the khong can thiet

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        System.out.println("nearly come to end of create-----");
        loadingdata();
        if(isOnline()){// sau kho d loadding dat thi minh bat dau tien hanh dong bo
            SyncDraftMail syncDraftMail = new SyncDraftMail(app, new RequestManager(), null, null);
            SyncOutboxMail syncOutboxMail = new SyncOutboxMail(app, new RequestManager(), syncDraftMail, null);
            SyncReadOrDeleteMail syncReadOrDeleteMail = new SyncReadOrDeleteMail(app, new RequestManager(), syncOutboxMail);
            syncReadOrDeleteMail.execute();
        }
        System.out.println("the end of creating-----");


        listAdapter.notifyDataSetChanged();
        System.out.println("da thay doi num of mail box-----");

    }



    // beginphan thiet lap ve luu data
    public void creatingData(){
        //create  data sharing

        System.out.println("creating data------");
        InboxMailList.add(new EmailItem(0, "Thu moi hop 1", "Feb 28", "vanminh@hostmail.com","t_00001@schoolm.com",  "Kinh thi moi quy phu huynh...", true));
        InboxMailList.add(new EmailItem(1, "Thu moi hop 1", "Feb 27", "vanminh@hostmail.com", "t_00002@schoolm.com","Kinh  vam moi quy phu huynh...", true));
        InboxMailList.add(new EmailItem(2, "Thu hoc phi 1", "Jan 21", "giaovien_@van.com", "t_00003@schoolm.com","Thong bao nop hoc phi...", true));
        InboxMailList.add(new EmailItem(3, "Thu hoc phi 1", "Jan 20", "giaovien_@van.com", "t_00004@schoolm.com","Thong bao nop hoc phi...", true));
        app.setData_InboxMailList(InboxMailList);
        app.setInboxReadMail(InboxReadMail);
        app.setInboxDeleteMail(InboxDeleteMail);


        SendMailList = new ArrayList<EmailItem>();
        SendMailList.add(new EmailItem(4, "Thu moi hop 2", "Feb 28", "vanminh@hostmail.com","t_00001@schoolm.com", "Kinh moi quy phu huynh...", false));
        SendMailList.add(new EmailItem(5, "Thu moi hop 2", "Feb 28", "vanminh@hostmail.com","t_00002@schoolm.com", "Kinh moi quy phu huynh...", false));
        SendMailList.add(new EmailItem(6, "Thu hoc phi 2", "Jan 21", "giaovien_@van.com","t_0003@schoolm.com", "Thong bao nop hoc phi...", false));
        SendMailList.add(new EmailItem(7, "Thu hoc phi 2", "Jan 21", "giaovien_@van.com","t_00004@schoolm.com", "Thong bao nop hoc phi...", false));
        app.setData_SendMailList(SendMailList);
        app.setSendDeleteMail(SendDeleteMail);

        DraftMailList = new ArrayList<EmailItem>();
        DraftMailList.add(new EmailItem(8, "Thu moi hop 3", "Feb 28", "vanminh@hostmail.com","vanminh1@hostmail.com", "Kinh moi quy phu huynh...", false));
        DraftMailList.add(new EmailItem(9, "Thu hoc phi 3", "Jan 21", "giaovien_@van.com","vanminh2@hostmail.com", "Thong bao nop hoc phi...", false));
        DraftMailList.add(new EmailItem(10, "Hop hoi dong 3", "July 8", "hoidong@gmail.com","vanminh3@hostmail.com", "Sap co thoi khoa bieu moi...", false));
        DraftMailList.add(new EmailItem(11, "Hop hoi dong 3", "July 1", "hoidong@gmail.com","vanminh4@hostmail.com", "Sap co thoi khoa bieu moi...", false));
        app.setData_DraftMailList(DraftMailList);
        app.setDraftDeleteMail(DraftDeleteMail);
        app.setDraftNewMail(DraftNewMail);

        OutboxMailList = new ArrayList<EmailItem>();
        OutboxMailList.add(new EmailItem(9, "Thu moi hop 5", "Feb 28", "vanminh@hostmail.com","vanminh5@hostmail.com", "Kinh moi quy phu huynh...",false));
        OutboxMailList.add(new EmailItem(12,"Thu hoc phi 5", "Jan 27", "giaovien_@van.com","vanminh6@hostmail.com", "Thong bao nop hoc phi...",false));
        OutboxMailList.add(new EmailItem(13,"Thu moi hop 5", "Feb 22", "vanminh@hostmail.com","vanminh7@hostmail.com", "Kinh moi quy phu huynh...",false));
        OutboxMailList.add(new EmailItem(14,"Thu hoc phi 5", "Jan 21", "giaovien_@van.com","vanminh8@hostmail.com", "Thong bao nop hoc phi...",false));

        app.setData_OutboxMailList(OutboxMailList);




    };


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



    public void loadingdata(){
        System.out.println("loading data------");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        isSaved = sp.getBoolean(id+"isSaved", false);
        if(isSaved){
            /// using for inbox mail
            int numInbox = sp.getInt(id+"numInbox", 0);
            for (int i = 0; i < numInbox; i++){
                int ID = sp.getInt(id+"idInbox"+ i, 0);
                String subject = sp.getString(id + "subjectInbox" + i, "");
                String date= sp.getString(id+"dateInbox"+ i, "");
                String sender= sp.getString(id+"senderInbox"+ i, "");
                String preview= sp.getString(id+"previewInbox" + i, "");
                System.out.println("mail---- " + i + ", " + ID + ", " + subject + ", " + date + ", " + sender + ", " + preview);
                InboxMailList.add(new EmailItem(ID, subject, date, sender, preview));
            }
            app.setData_InboxMailList(InboxMailList);

            int numReadInbox = sp.getInt(id+"numReadInbox", 0);
            for (int i = 0; i < numReadInbox; i++){
                int ID = sp.getInt(id+"idReadInbox" + i, 0);
                System.out.println("mail read inbox---- " + i + ", " + ID);
                InboxReadMail.add(ID + "");
            }
            app.setInboxReadMail(InboxReadMail);

            int numDeleteInbox = sp.getInt(id+"numDeleteInbox", 0);
            for (int i = 0; i < numDeleteInbox; i++){
                int ID = sp.getInt(id+"idDeleteInbox" + i, 0);
                System.out.println("mail delete inbox---- " + i + ", " + ID);
                InboxDeleteMail.add(ID + "");
            }
            System.out.println("InboxDeleteMail is ---" + InboxDeleteMail);

            app.setInboxDeleteMail(InboxDeleteMail);



            /// using for outbox mail
            int num = sp.getInt(id+"numOutbox", 0);
            for (int i = 0; i < num; i++){
                int ID = sp.getInt(id+"idOutbox"+ i, 0);
                String subject = sp.getString(id+"subjectOutbox" + i, "");
                String date= sp.getString(id+"dateOutbox"+ i, "");
                String sender= sp.getString(id+"senderOutbox"+ i, "");
                String preview= sp.getString(id+"previewOutbox" + i, "");
                System.out.println("mail---- " + i + ", " + ID + ", " + subject + ", " + date + ", " + sender + ", " + preview);
                OutboxMailList.add(new EmailItem(ID, subject, date, sender, preview));
            }
            app.setData_OutboxMailList(OutboxMailList);




            //loading cho mail send
            int numSend = sp.getInt(id+"numSend", 0);
            for (int i = 0; i < numSend; i++){
                int ID = sp.getInt(id+"idSend"+ i, 0);
                String subject = sp.getString(id+"subjectSend" + i, "");
                String date= sp.getString(id+"dateSend"+ i, "");
                String sender= sp.getString(id+"senderSend"+ i, "");
                String preview= sp.getString(id+"previewSend" + i, "");
                System.out.println("mail---- " + i + ", " + ID + ", " + subject + ", " + date + ", " + sender + ", " + preview);
                SendMailList.add(new EmailItem(ID, subject, date, sender, preview));
            }
            app.setData_SendMailList(SendMailList);

            int numDeleteSend = sp.getInt(id+"numDeleteSend", 0);
            for (int i = 0; i < numDeleteSend; i++){
                int ID = sp.getInt(id+"idDeleteSend" + i, 0);
                System.out.println("mail delete Send---- " + i + ", " + ID);
                SendDeleteMail.add(ID + "");
            }
            System.out.println("SendDeleteMail is ---" + SendDeleteMail);

            app.setSendDeleteMail(SendDeleteMail);


            //loading cho mail draft
            int numDraft = sp.getInt(id+"numDraft", 0);
            for (int i = 0; i < numDraft; i++){
                int ID = sp.getInt(id+"idDraft"+ i, 0);
                String subject = sp.getString(id+"subjectDraft" + i, "");
                String date= sp.getString(id+"dateDraft"+ i, "");
                String sender= sp.getString(id+"senderDraft"+ i, "");
                String preview= sp.getString(id+"previewDraft" + i, "");
                System.out.println("mail---- " + i + ", " + ID + ", " + subject + ", " + date + ", " + sender + ", " + preview);
                DraftMailList.add(new EmailItem(ID, subject, date, sender, preview));
            }
            app.setData_DraftMailList(DraftMailList);

            int numNewDraft = sp.getInt(id+"numNewDraft", 0);
            for (int i = 0; i < numNewDraft; i++){
                int ID = sp.getInt(id+"idNewDraft" + i, 0);
                System.out.println("mail NewDraft---- " + i + ", " + ID);
                DraftNewMail.add(ID + "");
            }
            app.setDraftNewMail(DraftNewMail);

            int numDeleteDraft = sp.getInt(id+"numDeleteDraft", 0);
            for (int i = 0; i < numDeleteDraft; i++){
                int ID = sp.getInt(id+"idDeleteDraft" + i, 0);
                System.out.println("mail delete Draft---- " + i + ", " + id);
                DraftDeleteMail.add(ID + "");
            }
            System.out.println("DraftDeleteMail is ---" + DraftDeleteMail);

            app.setDraftDeleteMail(DraftDeleteMail);




        }else{
            System.out.println("loadding from creating------");
            creatingData();
        }
    };


    public void savingDataLogIn(SharedPreferences.Editor editor){
        editor.putString("dataInfo", dataInfo);
        editor.putString("profile",app.getProfile());
        System.out.println("saving info" + dataInfo);


    };


    public void clearDataLogIn(SharedPreferences.Editor editor){
        editor.putString("dataInfo", null);
        editor.putString("profile", null);
        System.out.println("clear info" + dataInfo);
    };

    public void savingData(){
        System.out.println("begin saving data------");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
//        editor.putString("dataInfo", dataInfo);
//        System.out.println("saving info" + dataInfo);

        if(saveDataLogin){
            savingDataLogIn(editor);
            System.out.println("saving info" + dataInfo);
        }
        else{
            clearDataLogIn(editor);
            System.out.println("clean info" + dataInfo);
        }


        //for saving data from inbox mail
        editor.putBoolean(id + "isSaved", false);

        int numInbox = InboxMailList.size();
        editor.putInt(id+"numInbox", numInbox);
        for (int i = 0; i < numInbox; i++){
            System.out.println("numInboum mail saving----" + numInbox);
            editor.putInt(id+"idInbox" + i, InboxMailList.get(i).getId());
            editor.putString(id+"subjectInbox" + i, InboxMailList.get(i).getSubject());
            editor.putString(id+"dateInbox" + i, InboxMailList.get(i).getDate());
            editor.putString(id+"senderInbox" + i, InboxMailList.get(i).getSender());
            editor.putString(id+"previewInbox" + i, InboxMailList.get(i).getPreview());
        }

        int numReadInbox = InboxReadMail.size();
        editor.putInt(id+"numReadInbox", numReadInbox);
        for (int i = 0; i < numReadInbox; i++) {
            editor.putInt(id+"idReadInbox" + i, Integer.parseInt(InboxReadMail.get(i)));
            System.out.println("mail read inbox---- " + i + InboxReadMail.get(i));
        }


        int numDeleteInbox = InboxDeleteMail.size();
        System.out.println("size  of delete inbox is---- " + numDeleteInbox);
        editor.putInt("numDeleteInbox", numDeleteInbox);
        for (int i = 0; i < numDeleteInbox; i++) {
            editor.putInt(id+"idDeleteInbox" + i, Integer.parseInt(InboxDeleteMail.get(i)));
            System.out.println("mail Delete inbox---- " + i + InboxDeleteMail.get(i));
        }




        //using for saving mail send
        int numSend = SendMailList.size();
        editor.putInt(id+"numSend", numSend);
        for (int i = 0; i < numSend; i++){
            System.out.println("num send mail saving----" + numSend);
            editor.putInt(id+"idSend" + i, SendMailList.get(i).getId());
            editor.putString(id+"subjectSend" + i, SendMailList.get(i).getSubject());
            editor.putString(id+"dateSend" + i, SendMailList.get(i).getDate());
            editor.putString(id+"senderSend" + i, SendMailList.get(i).getSender());
            editor.putString(id+"previewSend" + i, SendMailList.get(i).getPreview());
        }

        int numDeleteSend = SendDeleteMail.size();
        editor.putInt(id+"numDeleteSend", numDeleteSend);
        for (int i = 0; i < numDeleteSend; i++) {
            editor.putInt(id+"idDeleteSend" + i, Integer.parseInt(SendDeleteMail.get(i)));
            System.out.println("mail read inbox---- " + i + SendDeleteMail.get(i));
        }


        //using for mail draft
        int numDraft = DraftMailList.size();
        editor.putInt(id+"numDraft", numDraft);
        for (int i = 0; i < numDraft; i++){
            System.out.println("num save mail saving----" + numDraft);
            editor.putInt(id+"idDraft" + i, DraftMailList.get(i).getId());
            editor.putString(id+"subjectDraft" + i, DraftMailList.get(i).getSubject());
            editor.putString(id+"dateDraft" + i, DraftMailList.get(i).getDate());
            editor.putString(id+"senderDraft" + i, DraftMailList.get(i).getSender());
            editor.putString(id+"previewDraft" + i, DraftMailList.get(i).getPreview());
        }

        int numDeleteDraft = DraftDeleteMail.size();
        editor.putInt(id+"numDeleteDraft", numDeleteDraft);
        for (int i = 0; i < numDeleteDraft; i++) {
            editor.putInt(id+"idDeleteDraft" + i, Integer.parseInt(DraftDeleteMail.get(i)));
            System.out.println("mail read inbox---- " + i + DraftDeleteMail.get(i));
        }

        int numNewDraft = DraftNewMail.size();
        editor.putInt(id+"numNewDraft", numNewDraft);
        for (int i = 0; i < numNewDraft; i++) {
            editor.putInt(id+"idNewDraft" + i, Integer.parseInt(DraftNewMail.get(i)));
            System.out.println("mail new draft---- " + i + DraftNewMail.get(i));
        }
        //end for draft

        // using for mail outbox
        int numOutbox = OutboxMailList.size();
        editor.putInt(id+"numOutbox", numOutbox);
        for (int i = 0; i < numOutbox; i++){
            System.out.println("num outbox mail saving----" + numOutbox);
            editor.putInt(id+"idOutbox" + i, OutboxMailList.get(i).getId());
            editor.putString(id+"subjectOutbox" + i, OutboxMailList.get(i).getSubject());
            editor.putString(id+"dateOutbox" + i, OutboxMailList.get(i).getDate());
            editor.putString(id+"senderOutbox" + i, OutboxMailList.get(i).getSender());
            editor.putString(id+"previewOutbox" + i, OutboxMailList.get(i).getPreview());
        }
        //end for outbox

        editor.commit();
        System.out.println("committed------");

    }
    //end  thiet lap


    @Override
    protected void onStart() {
        super.onStart();
//        pusher.connect();
//        Channel channel = pusher.subscribe("my-channel");
//        channel.bind("my-event", new SubscriptionEventListener() {
//
//            @Override
//            public void onEvent(String channelName, String eventName, final String data) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        notice();
//                    }
//                });
//                System.out.println("abchahaha");
//
//            }
//        });



    }

    public void notice(){
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
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
        NavItemChild inboxitem =new NavItemChild("Inbox", R.drawable.icon_inbox);
        inboxitem.setNum(num_new_mail);
        mailList.add(inboxitem);
        mailList.add(new NavItemChild("Sent",  R.drawable.icon_send));
        mailList.add(new NavItemChild("Drafts", R.drawable.icon_draft));
        mailList.add(new NavItemChild("Trash",R.drawable.icon_trash));
        mailList.add(new NavItemChild("Outbox",R.drawable.icon_outbox));
        app.setNumMailinbox(inboxitem);


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

        switch (item.getItemId()) {
            case R.id.logout:
                saveDataLogin = false;
                System.out.println("log out app -----");


                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}
