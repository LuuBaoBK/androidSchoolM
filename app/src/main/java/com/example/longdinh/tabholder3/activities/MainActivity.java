package com.example.longdinh.tabholder3.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import com.example.longdinh.tabholder3.adapters.MyExpandableListAdapter;
import com.example.longdinh.tabholder3.fragments.MyProfile;
import com.example.longdinh.tabholder3.fragments.Schedule_Person;
import com.example.longdinh.tabholder3.fragments.MyHome;
import com.example.longdinh.tabholder3.fragments.MyMail;
import com.example.longdinh.tabholder3.fragments.MySettings;
import com.example.longdinh.tabholder3.fragments.Schedule_Parent;
import com.example.longdinh.tabholder3.inner_fragments.Tab1Fragment;
import com.example.longdinh.tabholder3.inner_fragments.Tab2Fragment;
import com.example.longdinh.tabholder3.inner_fragments.Tab3Fragment;
import com.example.longdinh.tabholder3.inner_fragments.Tab4Fragment;
import com.example.longdinh.tabholder3.models.EmailItem;
import com.example.longdinh.tabholder3.models.NavItem;
import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.models.UserInfo;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private int lastExpandedPosition = -1;
    DrawerLayout drawerLayout;
    RelativeLayout drawerPane;
    ListView lvNav;
    TextView tvUserId;
    TextView tvFullname;
    TextView tvRole;
    int offsetNavList = 0;
    MyExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<NavItem> listDataHeader;
    HashMap<String, List<NavItem>> listDataChild;


    List<NavItem> listNavItems;
    List<Fragment> listFragments;
    UserInfo userInfo;

    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent login = getIntent();
        try {
            userInfo = new UserInfo(login.getStringExtra("userinfo_string"));
        } catch (JSONException e) {
            e.printStackTrace();
            finish();
        }



        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerPane = (RelativeLayout) findViewById(R.id.drawer_pane);
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        tvFullname = (TextView) findViewById(R.id.tvFullname);
        tvRole = (TextView) findViewById(R.id.tvRole);
        tvUserId = (TextView) findViewById(R.id.tvUserId);
        tvFullname.setText(userInfo.getFullname());
        tvUserId.setText(userInfo.getId());
        tvRole.setText(userInfo.getRole());


        listFragments = new ArrayList<Fragment>();
        listFragments.add(new MyProfile());
        listFragments.add(new MySettings());
        System.out.println("-----------------------------------"+userInfo.getRole());
        if(userInfo.getRole().equals("Teacher") || userInfo.getRole().equals("Student")){
            listFragments.add(new Schedule_Person());
            offsetNavList = 1;
        }else if(userInfo.getRole().equals("Parent")){
            listFragments.add(new Schedule_Parent());
            offsetNavList = 1;
        }
        listFragments.add(new MyMail());
        listFragments.add(new Tab1Fragment());
        listFragments.add(new Tab2Fragment());
        listFragments.add(new Tab3Fragment());
        listFragments.add(new Tab4Fragment());

        prepareListData();
        listAdapter = new MyExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        SharedPreferences prefs = getSharedPreferences("toGetData", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", userInfo.getToken());
        editor.commit();
        //them ham xu li lua chon, if(role is child or parent or teacher)
        //schedule for child and teacher are the same

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
                if (listAdapter.getChildrenCount(groupPosition) == 0){
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
                System.out.println("------------------------print:" + groupPosition + childPosition);

                List<NavItem> mailList = listDataChild.get(listDataHeader.get(3).getTitle());
                String title = new String();
                title = "MailBox-"+ mailList.get(childPosition).getTitle();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.main_content, listFragments.get(3 + offsetNavList + childPosition))
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

        // Adding child data
        listDataHeader.add(new NavItem("Dashboard", R.drawable.ic_action_home));
        listDataHeader.add(new NavItem("Class",  R.drawable.ic_action_settings));
        if(offsetNavList != 0)
            listDataHeader.add(new NavItem("Schedule", R.drawable.ic_action_about));
        listDataHeader.add(new NavItem("MailBox",R.drawable.icon_action_mail_box));

        // Adding child data
        List<NavItem> mailList = new ArrayList<NavItem>();

        mailList.add(new NavItem("Inbox", R.drawable.ic_action_home));
        mailList.add(new NavItem("Sent",  R.drawable.ic_action_settings));
        mailList.add(new NavItem("Drafts", R.drawable.ic_action_about));
        mailList.add(new NavItem("Trash",R.drawable.icon_action_logout));

        listDataChild.put(listDataHeader.get(2 + offsetNavList).getTitle(), mailList); // Header, Child data


        //create  data sharing
        SharedPreferences draftPre = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editDraftPre = draftPre.edit();
        MyApplication app = (MyApplication) getApplicationContext();



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
