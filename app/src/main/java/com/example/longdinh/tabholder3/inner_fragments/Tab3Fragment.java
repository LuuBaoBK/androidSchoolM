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
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.activities.MailContent;
import com.example.longdinh.tabholder3.activities.MyApplication;
import com.example.longdinh.tabholder3.activities.ReadDraftMailAcitivity;
import com.example.longdinh.tabholder3.activities.RequestManager;
import com.example.longdinh.tabholder3.adapters.EmailItemAdapter;
import com.example.longdinh.tabholder3.models.EmailItem;
import com.software.shell.fab.ActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Tab3Fragment extends Fragment {
    List<EmailItem> emailItemList;
    ListView lvEmailItem;
    EmailItemAdapter adapter;
    final int  EMAIL_COMPOSE_NEW = 101;
    String vitri = new String(-1 + "");
    String token;
    MyApplication app ;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab1fragment, container, false);
        app = (MyApplication) getActivity().getApplication();

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

                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                EmailItem selecteditem = (EmailItem) adapter.getItem(selected.keyAt(i));
                                if(false){//neu nhu co mang
                                    // goi ham update  thong tin mai
                                }else{
                                    app.addItem_DraftDeleteMail(selecteditem.getId() + "");
                                    System.out.println("Them vao draft Delete mail ----" + selecteditem.getId());
                                    Toast.makeText(getContext(), "Them vao draft Delete mail"+ selecteditem.getId(), Toast.LENGTH_SHORT).show();
                                }
                                adapter.remove(selecteditem);
                            }
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
        emailItemList = app.getData_DraftMailList();
        adapter = new EmailItemAdapter(getContext(), R.layout.item_email, emailItemList);
        lvEmailItem.setAdapter(adapter);
        lvEmailItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vitri = position + "";
                Intent intent = new Intent(getContext(), ReadDraftMailAcitivity.class);
                intent.putExtra("id", emailItemList.get(position).getId()+ "");
                intent.putExtra("typeMail","draft");
                startActivityForResult(intent, 710);
            }
        });

        if(isOnline()){
            new getListMailInbox().execute("");
        }
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
//        new getListMailInbox().execute("");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

//    private void createData(){
//        emailItemList = new ArrayList<EmailItem>();
//        MyApplication app = (MyApplication) this.getContext().getApplicationContext();
//        emailItemList = app.getData_InboxMailList();
//    };

    public class showMailDetail extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
//            HttpURLConnection httpURLConnection = null;
//            BufferedReader bufferedReader = null;
//            String url_ = Constant.ROOT_API + "api/get_schedule";
//            //chinh sua lai link
//            try {
//                URL url = new URL(url_);
//                httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestMethod("GET");
//                httpURLConnection.setRequestProperty(Constant.X_AUTH, token);
//
//
//                httpURLConnection.connect();
//                InputStream inputStream = httpURLConnection.getInputStream();
//                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                String line = null;
//                StringBuffer stringBuffer = new StringBuffer();
//                while ((line = bufferedReader.readLine()) != null) {
//                    stringBuffer.append(line + "\n");
//                }
//
            System.out.println("---------------da toi day 0");
            String retur = new String("{ \"id\": 1,\"content\": \"Noi dung khong quan trong chay dung la dc\",\"title\": \"Mail sent to server\",\"date_time\": \"Apr 29\",\"author\": \"t0001@schoolm.com\"}");
//                return stringBuffer.toString();
            return retur;

//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                return "e1";
//            } catch (IOException e) {
//                e.printStackTrace();
//                return "e2";
//            } finally {
//                if (httpURLConnection != null)
//                    httpURLConnection.disconnect();
//                try {
//                    if (bufferedReader != null)
//                        bufferedReader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return "e4";
//                }
//            }
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



    public class getListMailInbox extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {

            RequestManager requestManager = new RequestManager();
            String retur = requestManager.getInboxMail("api/post/mailbox/get_draft", app.getToken(), 5);
            System.out.println(retur + "--" + app.getToken());
//            retur = "[    { \"id\": 1,      \"content\": \"Xay dung khu hoc tap moi...\",      \"title\": \"Hop hoi Dong\",      \"date_time\": \"Apr 29\",      \"author\": \"t0001@schoolm.com\"    },    {      \"id\": 2,      \"content\": \"Lay y kien xay dung phuon...\",      \"title\": \"Ke Hoach Moi\",      \"date_time\": \"Jun 29\",      \"author\": \"a00003@schoolm.com\"    },\t{      \"id\": 3,      \"content\": \"Lay y kien xay dung phuon...\",      \"title\": \"Hang phim Thong tan...\",      \"date_time\": \"Jun 29\",      \"author\": \"a00003@schoolm.com\"    }  ]";

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
//                    emailItemList.add(new EmailItem(email.getInt("id"), email.getString("title"), email.getString("date_time"), email.getString("author"), email.getString("content")));
//                    emailItemList.add(new EmailItem(email.getInt("id"), email.getString("title"), email.getString("date_time"), email.getString("author"), email.getString("content")));
                    emailItemList.add(new EmailItem(email.getInt("id"), email.getString("title"), email.getString("date_time"), email.getString("author"), "", email.getString("content"), false));

                }
//                int numMail = data.getInt("new_mail");
//                app.getNumMailinbox().setNum(numMail);
//                app.notifyChangeNumInbox();

                adapter.notifyDataSetChanged();
//                lvEmailItem.setSelection(8);

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

