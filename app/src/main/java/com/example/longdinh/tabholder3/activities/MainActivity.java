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
    Boolean isSaved = false;
    List<EmailItem>        InboxMailList = new ArrayList<EmailItem>();
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


        Intent login = getIntent();
        String data = login.getStringExtra("userinfo_string");
        //String data = "{\"id\":\"t_00000013\",\"email\":\"t_0000013@schoolm.com\",\"role\":\"2\",\"fullname\":\"Trịnh Hiếu Vân\",\"token\":\"4ad2b006ff575c89d0c30fdf8b5f2b6a9f4b6a90\"}";
//        String data = "\"id\":\"a_0000000\",\"email\":\"a_0000000@schoolm.com\",\"role\":\"0\",\"fullname\":\"V\\u0103n H\\u1ea1 \\u0110\\u1ea1t\",\"token\":\"e4f5afc50773ae5b06b0b84c3c9d74cb341c9869\"";
        //su dung rieng cho parent
//        String data = "{\"id\":\"t_00000013\",\"email\":\"t_0000013@schoolm.com\",\"role\":\"4\",\"fullname\":\"TrịnhHiếuVân\",\"token\":\"4ad2b006ff575c89d0c30fdf8b5f2b6a9f4b6a90\",\"numchild\":2,\"children\":[{\"ma\":\"s_0000003\",\"fullname\":\"Nguyến Đinh Mai\"},{\"ma\":\"s_0000004\",\"fullname\":\"Nguyễn Phạn Hùng\"}]}";
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
        InboxMailList.add(new EmailItem(0,"Thu moi hop 1", "Feb 28", "vanminh@hostmail.com", "Kinh thi moi quy phu huynh..."));
        InboxMailList.add(new EmailItem(1, "Thu moi hop 1", "Feb 27", "vanminh@hostmail.com", "Kinh  vam moi quy phu huynh..."));
        InboxMailList.add(new EmailItem(2, "Thu hoc phi 1", "Jan 21", "giaovien_@van.com", "Thong bao nop hoc phi..."));
        app.setData_InboxMailList(InboxMailList);

    };

    public void loadingdata(){
        System.out.println("loading data------");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        isSaved = sp.getBoolean("isSaved", false);
        if(isSaved){
            System.out.println("loading from saved------");
            int num = sp.getInt("num", 0);
            for (int i = 0; i < num; i++){
                int id = sp.getInt("id"+ i, 0);
                String subject = sp.getString("subject" + i, "");
                String date= sp.getString("date"+ i, "");
                String sender= sp.getString("sender"+ i, "");
                String preview= sp.getString("preview"+ i, "");
                System.out.println("mail---- " + i + ", " + id + ", " + subject + ", " + date + ", " + sender + ", " + preview);
                InboxMailList.add(new EmailItem(id, subject, date, sender, preview));
                app.setData_InboxMailList(InboxMailList);
            }
        }else{
            System.out.println("loadding from creating------");
            creatingData();
        }
    };

    public void savingData(){
        System.out.println("begin saving data------");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        int num = InboxMailList.size();
        editor.clear();
        editor.putBoolean("isSaved", true);
        editor.putInt("num", num);
        for (int i = 0; i < num; i++){
            System.out.println("num mail saving----" + num);
            editor.putInt("id" + i, InboxMailList.get(i).getId());
            editor.putString("subject" + i, InboxMailList.get(i).getSubject());
            editor.putString("date" + i, InboxMailList.get(i).getDate());
            editor.putString("sender" + i, InboxMailList.get(i).getSender());
            editor.putString("preview" + i, InboxMailList.get(i).getPreview());
        }
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
        listDataChild.put(listDataHeader.get(offsetNavList).getTitle(), mailList); // Header, Child data


        List<EmailItem>        SendMailList = new ArrayList<EmailItem>();
        SendMailList.add(new EmailItem(3, "Thu moi hop 2", "Feb 28", "vanminh@hostmail.com", "Kinh moi quy phu huynh..."));
        SendMailList.add(new EmailItem(4, "Thu moi hop 2", "Feb 28", "vanminh@hostmail.com", "Kinh moi quy phu huynh..."));
        SendMailList.add(new EmailItem(5, "Thu hoc phi 2", "Jan 21", "giaovien_@van.com", "Thong bao nop hoc phi..."));
        app.setData_SendMailList(SendMailList);

        List<EmailItem>        DraftMailList = new ArrayList<EmailItem>();
        DraftMailList.add(new EmailItem(6, "Thu moi hop 3", "Feb 28", "vanminh@hostmail.com", "Kinh moi quy phu huynh..."));
        DraftMailList.add(new EmailItem(7, "Thu hoc phi 3", "Jan 21", "giaovien_@van.com", "Thong bao nop hoc phi..."));
        DraftMailList.add(new EmailItem(8, "Hop hoi dong 3", "July 8", "hoidong@gmail.com", "Sap co thoi khoa bieu moi..."));
        app.setData_DraftMailList(DraftMailList);


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
