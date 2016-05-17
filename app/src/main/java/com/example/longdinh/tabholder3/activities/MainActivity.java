package com.example.longdinh.tabholder3.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.longdinh.tabholder3.adapters.MyExpandableListAdapter;
import com.example.longdinh.tabholder3.fragments.MyClass;
import com.example.longdinh.tabholder3.fragments.NoticeBoardParent;
import com.example.longdinh.tabholder3.fragments.MyProfile;
import com.example.longdinh.tabholder3.fragments.NoticeBoardStudent;
import com.example.longdinh.tabholder3.fragments.NoticeTeacher;
import com.example.longdinh.tabholder3.fragments.Schedule_Person;
import com.example.longdinh.tabholder3.fragments.Schedule_Parent;
import com.example.longdinh.tabholder3.fragments.Transcript_Show;
import com.example.longdinh.tabholder3.fragments.Transcript_Show_Student;
import com.example.longdinh.tabholder3.models.StudentItemSpinner;
import com.example.longdinh.tabholder3.inner_fragments.Tab1Fragment;
import com.example.longdinh.tabholder3.inner_fragments.Tab2Fragment;
import com.example.longdinh.tabholder3.inner_fragments.Tab3Fragment;
import com.example.longdinh.tabholder3.inner_fragments.Tab4Fragment;
import com.example.longdinh.tabholder3.models.EmailItem;
import com.example.longdinh.tabholder3.models.NavItem;
import com.example.longdinh.tabholder3.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private int lastExpandedPosition = -1;
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
    HashMap<String, List<NavItem>> listDataChild;
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

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerPane = (RelativeLayout) findViewById(R.id.drawer_pane);
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        tvFullname = (TextView) findViewById(R.id.tvFullname);
        tvRole = (TextView) findViewById(R.id.tvRole);
        tvUserId = (TextView) findViewById(R.id.tvUserId);
        app = (MyApplication)getApplicationContext();


//        Intent login = getIntent();
        //String data = "{\"id\":\"t_00000013\",\"email\":\"t_0000013@schoolm.com\",\"role\":\"2\",\"fullname\":\"Trịnh Hiếu Vân\",\"token\":\"4ad2b006ff575c89d0c30fdf8b5f2b6a9f4b6a90\"}";
        //su dung rieng cho parent
        String data = "{\"id\":\"t_00000013\",\"email\":\"t_0000013@schoolm.com\",\"role\":\"4\",\"fullname\":\"TrịnhHiếuVân\",\"token\":\"4ad2b006ff575c89d0c30fdf8b5f2b6a9f4b6a90\",\"numchild\":2,\"children\":[{\"ma\":\"s_0000003\",\"fullname\":\"Nguyến Đinh Mai\"},{\"ma\":\"s_0000004\",\"fullname\":\"Nguyễn Phạn Hùng\"}]}";
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
            if(role.equals("1")){
                tvRole.setText("Admin");
                offsetNavList = 1;
            }else if (role.equals("2")){
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
        if(role.equals("1")){
            //do nothing
        }else if(role.equals("2")){
            listFragments.add(new MyClass());
            listFragments.add(new Schedule_Person());
            listFragments.add(new NoticeTeacher());
        }else if(role.equals("3")){
            listFragments.add(new Schedule_Parent());
            listFragments.add(new Transcript_Show());
            listFragments.add(new NoticeBoardParent());
        }else if(role.equals("4")){
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
                if (listAdapter.getChildrenCount(groupPosition) == 0){// neu nhu khong co con thi xu li thang
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


                List<NavItem> mailList = listDataChild.get(listDataHeader.get(offsetNavList).getTitle());
                String title = new String();
                title = "MailBox-"+ mailList.get(childPosition).getTitle();
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

    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<NavItem>();
        listDataChild = new HashMap<String, List<NavItem>>();

        ///---------------------------------------------------------
        if(role.equals("1")){
            listDataHeader.add(new NavItem("Profile", R.drawable.icon_profile));
            listDataHeader.add(new NavItem("MailBox",R.drawable.icon_mailbox1));
        }else if(role.equals("2")){
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
        }else if(role.equals("4")){
            listDataHeader.add(new NavItem("Profile", R.drawable.icon_profile));
            listDataHeader.add(new NavItem("Schedule", R.drawable.icon_schedule));
            listDataHeader.add(new NavItem("Transcript",  R.drawable.icon_notice));
            listDataHeader.add(new NavItem("Notice Board",  R.drawable.icon_notice));
            listDataHeader.add(new NavItem("MailBox",R.drawable.icon_mailbox1));
        }


        // Adding child data
        List<NavItem> mailList = new ArrayList<NavItem>();
        mailList.add(new NavItem("Inbox", R.drawable.icon_inbox));
        mailList.add(new NavItem("Sent",  R.drawable.icon_send));
        mailList.add(new NavItem("Drafts", R.drawable.icon_draft));
        mailList.add(new NavItem("Trash",R.drawable.icon_trash));
        listDataChild.put(listDataHeader.get(offsetNavList).getTitle(), mailList); // Header, Child data

        //create  data sharing
        List<EmailItem>        InboxMailList = new ArrayList<EmailItem>();
        InboxMailList.add(new EmailItem(0,"Thu moi hop 1", "Feb 28", "vanminh@hostmail.com", "Kinh moi quy phu huynh..."));
        InboxMailList.add(new EmailItem(1, "Thu moi hop 1", "Feb 28", "vanminh@hostmail.com", "Kinh moi quy phu huynh..."));
        InboxMailList.add(new EmailItem(2, "Thu hoc phi 1", "Jan 21", "giaovien_@van.com", "Thong bao nop hoc phi..."));
        app.setData_InboxMailList(InboxMailList);


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
