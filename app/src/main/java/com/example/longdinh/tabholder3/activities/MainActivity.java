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
import com.example.longdinh.tabholder3.models.NoticeBoardItem;
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
    List<EmailItem>        TrashMailList = new ArrayList<EmailItem>();


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
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(defaultOptions).build();
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


        //GET AND PARSE DATA USER
        Intent login = getIntent();
        dataInfo = login.getStringExtra("userinfo_string");
//        dataInfo = "{\"id\":\"t_00000013\",\"email\":\"t_0000013@schoolm.com\",\"role\":\"1\",\"fullname\":\"Trịnh Hiếu Vân\",\"token\":\"4ad2b006ff575c89d0c30fdf8b5f2b6a9f4b6a90\",\"num_new_mail\":4}";
//        dataInfo = "{\"id\":\"p_00000001\",\"email\":\"t_0000013@schoolm.com\",\"role\":\"3\",\"fullname\":\"TrịnhHiếuVân\",\"token\":\"4ad2b006ff575c89d0c30fdf8b5f2b6a9f4b6a90\",\"numchild\":2,\"children\":[{\"ma\":\"s_0000003\",\"fullname\":\"Nguyến Đinh Mai\"},{\"ma\":\"s_0000004\",\"fullname\":\"Nguyễn Phạn Hùng\"}],\"num_new_mail\":4}";
        System.out.println(dataInfo + "---");
        try {
            JSONObject user = new JSONObject(dataInfo);
            id = user.getString("id");
            token = user.getString("token");
            num_new_mail = user.getInt("num_new_mail");
            role = user.getString("role");

            tvUserId.setText(id);
            tvFullname.setText(user.getString("fullname"));

            app.setId(id);
            app.setToken(token);
            app.setFullName(user.getString("fullname"));
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


            System.out.println(role+" role");

        } catch (JSONException e) {
            e.printStackTrace();
            finish();
        }



        //CREATE OBJECT FOR MY APP CLASS
        app.setData_InboxMailList(InboxMailList);
        app.setInboxReadMail(InboxReadMail);
        app.setInboxDeleteMail(InboxDeleteMail);

        app.setData_SendMailList(SendMailList);
        app.setSendDeleteMail(SendDeleteMail);

        app.setData_DraftMailList(DraftMailList);
        app.setDraftNewMail(DraftNewMail);
        app.setDraftDeleteMail(DraftDeleteMail);

        app.setData_TrashMailList(TrashMailList);

        app.setData_OutboxMailList(OutboxMailList);

        loadingdata();




        //BEGIN CREATE SCREENS
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

        //CREATE NAVIGATE LIST
        prepareListData();

        listAdapter = new MyExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        app.setListAdapter(listAdapter);

        //SET DEFAULT
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, listFragments.get(0)).commit();
        setTitle(listDataHeader.get(0).getTitle());

//        lvNav.setItemChecked(0, true);
        expListView.setSelectedGroup(0);
        drawerLayout.closeDrawer(drawerPane);
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (listAdapter.getChildrenCount(groupPosition) == 0) {// neu nhu khong co con thi xu li thang
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.main_content, listFragments.get(groupPosition)).commit();
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
                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }

        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);


        //AFTER FINISH CREATE DATA AND SCREENS THEN UPDATE OR SYNC DATA LOCAL
        if(isOnline()){
//            SyncDraftMail syncDraftMail = new SyncDraftMail(app, new RequestManager(), null, null);
//            SyncOutboxMail syncOutboxMail = new SyncOutboxMail(app, new RequestManager(), null, null);
//            SyncReadOrDeleteMail syncReadOrDeleteMail = new SyncReadOrDeleteMail(app, new RequestManager(), syncOutboxMail);
//            syncReadOrDeleteMail.execute();
//            syncOutboxMail.execute();
        }

        listAdapter.notifyDataSetChanged();
    }

    //CREATING DATA FOR TEST
    public void creatingData(){

        InboxMailList.add(new EmailItem(0, "Thu moi hop 1", "Feb 28", "vanminh@hostmail.com","t_00001@schoolm.com",  "Kinh thi moi quy phu huynh...", true, true));
        InboxMailList.add(new EmailItem(1, "Thu moi hop 1", "Feb 27", "vanminh@hostmail.com", "t_00002@schoolm.com", "Kinh  vam moi quy phu huynh...", true, true));
        InboxMailList.add(new EmailItem(2, "Thu hoc phi 1", "Jan 21", "giaovien_@van.com", "t_00003@schoolm.com", "Thong bao nop hoc phi...", true, true));
        InboxMailList.add(new EmailItem(3, "Thu hoc phi 1", "Jan 20", "giaovien_@van.com", "t_00004@schoolm.com","Thong bao nop hoc phi...", true, true));

        SendMailList.add(new EmailItem(4, "Thu moi hop 2", "Feb 28", "vanminh@hostmail.com", "t_00001@schoolm.com", "Kinh moi quy phu huynh...", false, true));
        SendMailList.add(new EmailItem(5, "Thu moi hop 2", "Feb 28", "vanminh@hostmail.com", "t_00002@schoolm.com", "Kinh moi quy phu huynh...", false, true));
        SendMailList.add(new EmailItem(6, "Thu hoc phi 2", "Jan 21", "giaovien_@van.com","t_0003@schoolm.com", "Thong bao nop hoc phi...", false, true));
        SendMailList.add(new EmailItem(7, "Thu hoc phi 2", "Jan 21", "giaovien_@van.com", "t_00004@schoolm.com", "Thong bao nop hoc phi...", false, true));

        DraftMailList.add(new EmailItem(8, "Thu moi hop 3", "Feb 28", "vanminh@hostmail.com", "vanminh1@hostmail.com", "Kinh moi quy phu huynh...", false, true));
        DraftMailList.add(new EmailItem(9, "Thu hoc phi 3", "Jan 21", "giaovien_@van.com", "vanminh2@hostmail.com", "Thong bao nop hoc phi...", false, true));
        DraftMailList.add(new EmailItem(10, "Hop hoi dong 3", "July 8", "hoidong@gmail.com","vanminh3@hostmail.com", "Sap co thoi khoa bieu moi...", false, true));
        DraftMailList.add(new EmailItem(11, "Hop hoi dong 3", "July 1", "hoidong@gmail.com","vanminh4@hostmail.com", "Sap co thoi khoa bieu moi...", false, true));

        OutboxMailList.add(new EmailItem(9, "Thu moi hop 5", "Feb 28", "vanminh@hostmail.com","vanminh5@hostmail.com", "Kinh moi quy phu huynh...",false, false));
        OutboxMailList.add(new EmailItem(12,"Thu hoc phi 5", "Jan 27", "giaovien_@van.com","vanminh6@hostmail.com", "Thong bao nop hoc phi...",false, false));
        OutboxMailList.add(new EmailItem(13,"Thu moi hop 5", "Feb 22", "vanminh@hostmail.com","vanminh7@hostmail.com", "Kinh moi quy phu huynh...",false, false));
        OutboxMailList.add(new EmailItem(14, "Thu hoc phi 5", "Jan 21", "giaovien_@van.com", "vanminh8@hostmail.com", "Thong bao nop hoc phi...", false, false));

        TrashMailList.add(new EmailItem(8, "Thu moi hop 4", "Feb 28", "vanminh@hostmail.com","vanminh@hostmail.com", "Kinh moi quy phu huynh...", false, true));
        TrashMailList.add(new EmailItem(10, "Thu moi hop 4", "Feb 28", "vanminh@hostmail.com", "vanminh@hostmail.com", "Kinh moi quy phu huynh...", false, true));
        TrashMailList.add(new EmailItem(11, "Thu hoc phi 4", "Jan 21", "giaovien_@van.com", "vanminh@hostmail.com", "Thong bao nop hoc phi...",false, true));

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
        isSaved = sp.getBoolean(id+"IS_SAVED", false);
        if(isSaved){
            //LOADING MAIL INBOX
            int numInbox = sp.getInt(id+"NUM_INBOX", 0);

            System.out.println("loading mail inbox+:----" + numInbox);


            for (int i = 0; i < numInbox; i++){
                String jsonString = sp.getString(id+"INBOX"+i, null);
                System.out.println("INBOX " + i + " :" + jsonString );
                InboxMailList.add(new EmailItem(jsonString));
            }

            int numReadInbox = sp.getInt(id+"NUM_READ_INBOX", 0);
            for (int i = 0; i < numReadInbox; i++){
                int ID = sp.getInt(id+"ID_READ_INBOX" + i, 0);
                System.out.println("mail read inbox---- " + i + ", " + ID);
                InboxReadMail.add(ID + "");
            }

            int numDeleteInbox = sp.getInt(id+"NUM_DELETE_INBOX", 0);
            for (int i = 0; i < numDeleteInbox; i++){
                int ID = sp.getInt(id+"ID_DELETE_INBOX" + i, 0);
                System.out.println("mail delete inbox---- " + i + ", " + ID);
                InboxDeleteMail.add(ID + "");
            }
            //END LOADING MAIL INBOX


            //LOADING MAIL OUTBOX
            int num = sp.getInt(id+"NUM_OUTBOX", 0);
            for (int i = 0; i < num; i++){
                String jsonString= sp.getString(id+"OUTBOX" + i, null);
                System.out.println("Outbox "  + i + ": " + jsonString);
                OutboxMailList.add(new EmailItem(jsonString));
            }
            //END LOADING MAIL OUTBOX


            //LOADING MAIL SEND
            int numSend = sp.getInt(id+"NUM_SEND", 0);
            for (int i = 0; i < numSend; i++){
                String jsonString= sp.getString(id+"SEND" + i, null);
                System.out.println("Send " + i + ": " + jsonString);
                SendMailList.add(new EmailItem(jsonString));
            }

            int numDeleteSend = sp.getInt(id+"NUM_DELETE_SEND", 0);
            for (int i = 0; i < numDeleteSend; i++){
                int ID = sp.getInt(id+"ID_DELETE_SEND" + i, 0);
                System.out.println("mail delete Send---- " + i + ", " + ID);
                SendDeleteMail.add(ID + "");
            }
            System.out.println("SendDeleteMail is ---" + SendDeleteMail);

            //LOADING MAIL DRAFT
            int numDraft = sp.getInt(id+"NUM_DRAFT", 0);
            for (int i = 0; i < numDraft; i++){
                String jsonString = sp.getString(id+"DRAFT" + i, null);
                System.out.println("Draft " + i + ": " + jsonString);
                DraftMailList.add(new EmailItem(jsonString));
            }


            int numNewDraft = sp.getInt(id+"NUM_NEW_DRAFT", 0);
            for (int i = 0; i < numNewDraft; i++){
                int ID = sp.getInt(id+"ID_NEW_DRAFT" + i, 0);
                System.out.println("mail NewDraft---- " + i + ", " + ID);
                DraftNewMail.add(ID + "");
            }


            int numDeleteDraft = sp.getInt(id+"NUM_DELETE_DRAFT", 0);
            for (int i = 0; i < numDeleteDraft; i++){
                int ID = sp.getInt(id+"ID_DELETE_DRAFT" + i, 0);
                DraftDeleteMail.add(ID + "");
            }
            System.out.println("DraftDeleteMail is ---" + DraftDeleteMail);
            //END LOADING MAIL DRAFT

            //SAVING NOTICE FOR STUDENT ROLE
            if(role.equals("2")){

                //NOTICE T2
                int numNoticeT2 = sp.getInt(id+"NUM_NOTICE_T2", 0);
                for (int i = 0; i < numNoticeT2; i++){
                    String jsonString = sp.getString(id+"NOTICE_T3" + i, null);
                    app.addItem_NoticeListT2(new NoticeBoardItem(jsonString));
                }

                //NOTICE T3
                int numNoticeT3 = sp.getInt(id + "NUM_NOTICE_T3", 0);
                for (int i = 0; i < numNoticeT3; i++){
                    String jsonString = sp.getString(id+"NOTICE_T3" + i, null);
                    app.addItem_NoticeListT3(new NoticeBoardItem(jsonString));
                }

                //NOTICE T4
                int numNoticeT4 = sp.getInt(id + "NUM_NOTICE_T4", 0);
                for (int i = 0; i < numNoticeT4; i++){
                    String jsonString = sp.getString(id+"NOTICE_T4" + i, null);
                    app.addItem_NoticeListT4(new NoticeBoardItem(jsonString));
                }
                //NOTICE T5
                int numNoticeT5 = sp.getInt(id + "NUM_NOTICE_T5", 0);
                for (int i = 0; i < numNoticeT5; i++){
                    String jsonString = sp.getString(id+"NOTICE_T5" + i, null);
                    app.addItem_NoticeListT5(new NoticeBoardItem(jsonString));
                }
                //NOTICE T6
                int numNoticeT6 = sp.getInt(id + "NUM_NOTICE_T6", 0);
                for (int i = 0; i < numNoticeT6; i++){
                    String jsonString = sp.getString(id+"NOTICE_T6" + i, null);
                    app.addItem_NoticeListT6(new NoticeBoardItem(jsonString));
                }
                //NOTICE T7
                int numNoticeT7 = sp.getInt(id + "NUM_NOTICE_T7", 0);
                for (int i = 0; i < numNoticeT7; i++){
                    String jsonString = sp.getString(id+"NOTICE_T7" + i, null);
                    app.addItem_NoticeListT7(new NoticeBoardItem(jsonString));
                }


            }


        }else{
            System.out.println("loadding from creating------");
            creatingData();
        }
    };

    public void savingDataLogIn(SharedPreferences.Editor editor){
        editor.putString("DATA_INFO", dataInfo);
        editor.putString("PROFILE",app.getProfile());
        System.out.println("saving info" + dataInfo);
    };

    public void clearDataLogIn(SharedPreferences.Editor editor){
        editor.putString("DATA_INFO", null);
        editor.putString("PROFILE", null);
        System.out.println("clear info" + dataInfo);
    };

    public void savingData(){
        System.out.println("begin saving data------");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();

        //SAVING FOR DATA LOGIN
        if(saveDataLogin){
            savingDataLogIn(editor);
        }
        else{
            System.out.println("clear");
        }

        editor.putBoolean(id + "IS_SAVED", true);
        //INBOX MAIL
        int numInbox = InboxMailList.size();
        numInbox = numInbox>5?5:numInbox;
        editor.putInt(id+"NUM_INBOX", numInbox);
        for (int i = 0; i < numInbox; i++){
            editor.putString(id+"INBOX"+i, InboxMailList.get(i).toJsonString());
            System.out.println(id+"INBOX"+i+ InboxMailList.get(i).toJsonString());
        }



        int numReadInbox = InboxReadMail.size();
        editor.putInt(id+"NUM_READ_INBOX", numReadInbox);
        for (int i = 0; i < numReadInbox; i++) {
            editor.putInt(id + "ID_READ_INBOX" + i, Integer.parseInt(InboxReadMail.get(i)));
            System.out.println(id + "ID_READ_INBOX" + i + Integer.parseInt(InboxReadMail.get(i)));
        }

        int numDeleteInbox = InboxDeleteMail.size();
        editor.putInt("NUM_DELETE_INBOX", numDeleteInbox);
        for (int i = 0; i < numDeleteInbox; i++) {
            editor.putInt(id + "ID_DELETE_INBOX" + i, Integer.parseInt(InboxDeleteMail.get(i)));
            System.out.println(id + "ID_DELETE_INBOX" + i + Integer.parseInt(InboxDeleteMail.get(i)));
        }
        //END INBOX MAIL

        //MAIL SEND
        int numSend = SendMailList.size();
        numSend = numSend>5?5:numSend;
        editor.putInt(id + "NUM_SEND", numSend);
        for (int i = 0; i < numSend; i++){
            editor.putString(id+"SEND"+i, SendMailList.get(i).toJsonString());
        }

        int numDeleteSend = SendDeleteMail.size();
        editor.putInt(id+"NUM_DELETE_SEND", numDeleteSend);
        for (int i = 0; i < numDeleteSend; i++) {
            editor.putInt(id + "ID_DELETE_SEND" + i, Integer.parseInt(SendDeleteMail.get(i)));
        }
        //END MAIL SEND


        //DRAFT MAIL
        int numDraft = DraftMailList.size();
        numDraft = numDraft>5?5:numDraft;
        editor.putInt(id+"NUM_DRAFT", numDraft);
        for (int i = 0; i < numDraft; i++){
            editor.putString(id+"DRAFT" + i, DraftMailList.get(i).toJsonString());
            System.out.println(id + "DRAFT" + i + DraftMailList.get(i).toJsonString());
        }

        int numDeleteDraft = DraftDeleteMail.size();
        editor.putInt(id+"NUM_DELETE_DRAFT", numDeleteDraft);
        for (int i = 0; i < numDeleteDraft; i++) {
            editor.putInt(id + "ID_DELETE_DRAFT" + i, Integer.parseInt(DraftDeleteMail.get(i)));
            System.out.println(id + "ID_DELETE_DRAFT" + i + Integer.parseInt(DraftDeleteMail.get(i)));
        }

        int numNewDraft = DraftNewMail.size();
        editor.putInt(id+"NUM_NEW_DRAFT", numNewDraft);
        for (int i = 0; i < numNewDraft; i++) {
            editor.putInt(id + "ID_NEW_DRAFT" + i, Integer.parseInt(DraftNewMail.get(i)));
            System.out.println(id + "ID_NEW_DRAFT" + i+ Integer.parseInt(DraftNewMail.get(i)));
        }
        //END DRAFT MAIL

        //MAIL OUTBOX
        int numOutbox = OutboxMailList.size();
        editor.putInt(id+"NUM_OUTBOX", numOutbox);
        for (int i = 0; i < numOutbox; i++){
            editor.putString(id+"OUTBOX" + i, OutboxMailList.get(i).toJsonString());
        }
        //END MAIL OUTBOX



        //SAVING NOTICE FOR STUDENT ROLE
        if(role.equals("2")){

            //NOTICE T2
            int numNoticeT2 = app.getSize_NoticeListT2();
            numNoticeT2 = numNoticeT2>5?5:numNoticeT2;
            editor.putInt(id+"NUM_NOTICE_T2", numNoticeT2);
            for (int i = 0; i < numNoticeT2; i++){
                editor.putString(id+"NOTICE_T2" + i, app.getData_NoticeListT2().get(i).toJsonString());
            }

            //NOTICE T3
            int numNoticeT3 = app.getSize_NoticeListT3();
            numNoticeT3 = numNoticeT3>5?5:numNoticeT3;
            editor.putInt(id+"NUM_NOTICE_T3", numNoticeT3);
            for (int i = 0; i < numNoticeT3; i++){
                editor.putString(id+"NOTICE_T3" + i, app.getData_NoticeListT3().get(i).toJsonString());
            }

            //NOTICE T4
            int numNoticeT4 = app.getSize_NoticeListT4();
            numNoticeT4 = numNoticeT2>5?5:numNoticeT4;
            editor.putInt(id+"NUM_NOTICE_T4", numNoticeT4);
            for (int i = 0; i < numNoticeT4; i++){
                editor.putString(id+"NOTICE_T4" + i, app.getData_NoticeListT4().get(i).toJsonString());
            }

            //NOTICE T5
            int numNoticeT5 = app.getSize_NoticeListT5();
            numNoticeT5 = numNoticeT5>5?5:numNoticeT5;
            editor.putInt(id+"NUM_NOTICE_T5", numNoticeT5);
            for (int i = 0; i < numNoticeT5; i++){
                editor.putString(id+"NOTICE_T5" + i, app.getData_NoticeListT5().get(i).toJsonString());
            }

            //NOTICE T6
            int numNoticeT6 = app.getSize_NoticeListT6();
            numNoticeT6 = numNoticeT6>5?5:numNoticeT6;
            editor.putInt(id+"NUM_NOTICE_T6", numNoticeT6);
            for (int i = 0; i < numNoticeT6; i++){
                editor.putString(id+"NOTICE_T6" + i, app.getData_NoticeListT6().get(i).toJsonString());
            }

            //NOTICE T7
            int numNoticeT7 = app.getSize_NoticeListT7();
            numNoticeT7 = numNoticeT2>5?5:numNoticeT7;
            editor.putInt(id+"NUM_NOTICE_T7", numNoticeT7);
            for (int i = 0; i < numNoticeT7; i++){
                editor.putString(id+"NOTICE_T7" + i, app.getData_NoticeListT7().get(i).toJsonString());
            }
        }



        editor.commit();
        System.out.println("committed------");

    }
    //END SAVING DADTA


    @Override
    protected void onStart() {
        super.onStart();
// ADD NOTICE CREATE
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

        NavItem MailBox = new NavItem("MailBox",R.drawable.icon_mailbox1, num_new_mail);
        app.setNavInbox(MailBox);

        if(role.equals("0")){
            listDataHeader.add(new NavItem("Profile", R.drawable.icon_profile));
            listDataHeader.add(MailBox);
        }else if(role.equals("1")){
            listDataHeader.add(new NavItem("Profile", R.drawable.icon_profile));
            listDataHeader.add(new NavItem("Class",  R.drawable.icon_class));
            listDataHeader.add(new NavItem("Schedule", R.drawable.icon_schedule));
            listDataHeader.add(new NavItem("Notice Board",  R.drawable.icon_notice));
            listDataHeader.add(MailBox);
        }else if(role.equals("3")){
            listDataHeader.add(new NavItem("Profile", R.drawable.icon_profile));
            listDataHeader.add(new NavItem("Schedule", R.drawable.icon_schedule));
            listDataHeader.add(new NavItem("Transcript",  R.drawable.icon_notice));
            listDataHeader.add(new NavItem("Notice Board",  R.drawable.icon_notice));
            listDataHeader.add(MailBox);
        }else if(role.equals("2")){
            listDataHeader.add(new NavItem("Profile", R.drawable.icon_profile));
            listDataHeader.add(new NavItem("Schedule", R.drawable.icon_schedule));
            listDataHeader.add(new NavItem("Transcript",  R.drawable.icon_notice));
            listDataHeader.add(new NavItem("Notice Board",  R.drawable.icon_notice));
            listDataHeader.add(MailBox);
        }
        // Adding child data
        mailList = new ArrayList<NavItemChild>();
        NavItemChild inboxitem = new NavItemChild("Inbox", R.drawable.icon_inbox);
        inboxitem.setNum(num_new_mail);
        mailList.add(inboxitem);
        app.setNumMailinbox(inboxitem);
        mailList.add(new NavItemChild("Sent",  R.drawable.icon_send));
        mailList.add(new NavItemChild("Drafts", R.drawable.icon_draft));
        mailList.add(new NavItemChild("Trash", R.drawable.icon_trash));
        mailList.add(new NavItemChild("Outbox",R.drawable.icon_outbox));

        listDataChild.put(listDataHeader.get(offsetNavList).getTitle(), mailList); // Header, Child data
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
