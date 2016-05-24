package com.example.longdinh.tabholder3.inner_fragments;

/**
 * Created by long dinh on 12/04/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.activities.MailContent;
import com.example.longdinh.tabholder3.activities.MyApplication;
import com.example.longdinh.tabholder3.activities.ReadMailAcitivity;
import com.example.longdinh.tabholder3.activities.RequestManager;
import com.example.longdinh.tabholder3.adapters.EmailItemAdapter;
import com.example.longdinh.tabholder3.models.EmailItem;
import com.software.shell.fab.ActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Tab1Fragment extends Fragment {
    List<EmailItem> emailItemList;
    ListView lvEmailItem;
    EmailItemAdapter adapter;
    final int  EMAIL_COMPOSE_NEW = 101;
    MyApplication app ;
    String vitri = new String(-1 + "");
    String token;
    SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab1fragment, container, false);
        app = (MyApplication) getActivity().getApplication();

        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setEnabled(false);

                if(isOnline()){
                    new getListMailInbox().execute("0");
                }else{
                    Toast.makeText(getContext(), "No connection", Toast.LENGTH_SHORT).show();
                }
                refreshLayout.setRefreshing(false);
                refreshLayout.setEnabled(true);
            }
        });


        lvEmailItem = (ListView) v.findViewById(R.id.lvEmailItem);
        lvEmailItem.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvEmailItem.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkedCount = lvEmailItem.getCheckedItemCount();
                mode.setTitle(checkedCount + " Selected");
                adapter.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_delete, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itDelete:
                        SparseBooleanArray selected = adapter.getSelectedIds();

                        String listDelete = "";
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                EmailItem selecteditem = (EmailItem) adapter.getItem(selected.keyAt(i));


                                //check if co mang thi gui len con neu khong co thi add vao trong list
                                if (isOnline()) {
                                    listDelete += selecteditem.getId() + ",";
                                } else {
                                    app.addItem_InboxDeleteMail(selecteditem.getId() + "");
                                    System.out.println("Them vao inboxDelete mail ----" + selecteditem.getId());
                                    Toast.makeText(getContext(), "Them vao inboxDelete mail", Toast.LENGTH_SHORT).show();
                                }
                                adapter.remove(selecteditem);
                            }
                        }
                        if (isOnline()) {
                            new updateDelete().execute(listDelete);
                        }

                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.removeSelection();
            }
        });

        Button btnLoadMore = (Button) v.findViewById(R.id.btnLoadMore);
        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()){
                    String size = app.getSize_InboxMailList()+"";
                    new getListMailInbox().execute(size);
                }
                Toast.makeText(getContext(), "Loading more", Toast.LENGTH_SHORT).show();
            }
        });

        ActionButton actionButton = (ActionButton) v.findViewById(R.id.action_button);
        actionButton.setImageResource(R.drawable.fab_plus_icon);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MailContent.class);
                intent.putExtra("type", "COMPOSE");
                intent.putExtra("id", -1);
                intent.putExtra("date", "");
                intent.putExtra("preview", "");
                intent.putExtra("sender", "");
                intent.putExtra("subject","");
                startActivityForResult(intent, EMAIL_COMPOSE_NEW);
            }
        });

//        createData();
//        emailItemList = new ArrayList<EmailItem>();
        emailItemList = app.getData_InboxMailList();
        adapter = new EmailItemAdapter(getContext(), R.layout.item_email, emailItemList);
        lvEmailItem.setAdapter(adapter);
        lvEmailItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(emailItemList.get(position).getIsRead()){
                    emailItemList.get(position).setIsRead(false);
                    app.addItem_InboxReadMail(emailItemList.get(position).getId() + "");
                    Toast.makeText(getContext(), "Them vao read inbox mail", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    System.out.println("read a inbox mail-----");
                    // cam kiem tra xem co mang khong hay co thuoc 100 mail dau tien khong
                }
                Intent intent = new Intent(getContext(), ReadMailAcitivity.class);
                intent.putExtra("id", emailItemList.get(position).getId()+ "");
                intent.putExtra("typeMail","inbox");
                startActivityForResult(intent, 700);
            }
        });

        if(isOnline()){
            new getListMailInbox().execute("0");
        }
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //do nothing
    }

    public class showMailDetail extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            System.out.println("---------------da toi day 0");
                String retur = new String("{ \"id\": 1,\"content\": \"Noi dung khong quan trong chay dung la dc\",\"title\": \"Mail sent to server\",\"date_time\": \"Apr 29\",\"author\": \"t0001@schoolm.com\"}");
                return retur;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject email = new JSONObject(result);

                Intent intent = new Intent(getContext(), MailContent.class);
                intent.putExtra("type", "COMPOSE");
                intent.putExtra("id", email.getInt("id"));
                intent.putExtra("date", email.getString("date_time"));
                intent.putExtra("preview", email.getString("content"));
                intent.putExtra("sender", email.getString("author"));
                intent.putExtra("subject", email.getString("title"));
                startActivityForResult(intent, EMAIL_COMPOSE_NEW);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class updateDelete extends AsyncTask<String, String , String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String data = "delete="+params[0];
            System.out.println(data);
            System.out.println("-dang doc mail detail----");
            RequestManager requestManager = new RequestManager();
            requestManager.postDataToServer("api/post/mailbox/update_log",app.getToken(),data);



            return null;//truong hop khong co trong mail ma cung khong the online
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }



    public class getListMailInbox extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {
            int size = Integer.parseInt(params[0]);
            size = size + 5;
            RequestManager requestManager = new RequestManager();
            String retur = requestManager.getInboxMail("api/post/mailbox/get_inbox", app.getToken(), size);
            System.out.println(retur + "--" + app.getToken());

                return retur;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject data = new JSONObject(result);
                JSONArray inbox = data.getJSONArray("list_inbox");

                emailItemList.clear();
                for(int i = 0; i < inbox.length(); i++){
                    JSONObject email = inbox.getJSONObject(i);
                    emailItemList.add(new EmailItem(email.getInt("id"), email.getString("title"), email.getString("date_time"), email.getString("author"), email.getString("receiver"), email.getString("content"), email.getBoolean("isRead"), true));
                }
                int numMail = data.getInt("new_mail");
                app.getNumMailinbox().setNum(numMail);
                app.getNavInbox().setNum(numMail);
                app.notifyChangeNumInbox();
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}


