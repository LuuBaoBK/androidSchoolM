package com.example.longdinh.tabholder3.noticeday;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.activities.MyApplication;
import com.example.longdinh.tabholder3.activities.RequestManager;
import com.example.longdinh.tabholder3.activities.ShowDetailNotice;
import com.example.longdinh.tabholder3.adapters.NoticeBoardAdapter;
import com.example.longdinh.tabholder3.models.NoticeBoardItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by long dinh on 13/05/2016.
 */
public class NoticeT2 extends Fragment {

    ListView listView;
    ProgressDialog dialog;
    List<NoticeBoardItem> noticeBoardItemList = new ArrayList<>();
    NoticeBoardAdapter adapter;
    String mahs;
    MyApplication app;
    SwipeRefreshLayout refreshLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.notice_day, container, false);

        app = (MyApplication) getActivity().getApplication();
        noticeBoardItemList = app.getData_NoticeListT2();
        mahs = app.getCurrentchild();

        listView = (ListView) v.findViewById(R.id.lvNotice);
        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");
        adapter = new NoticeBoardAdapter(getContext(), R.layout.item_noticeboard, noticeBoardItemList);

        listView.setAdapter(adapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                new showNoticeDetail().execute(noticeBoardItemList.get(position).getId() + "");
                Intent intent = new Intent(getContext(), ShowDetailNotice.class);
                intent.putExtra("nid", noticeBoardItemList.get(position).getId());
                intent.putExtra("date", "2");
                startActivityForResult(intent, 333);
            }
        });


        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setEnabled(false);

                if(isOnline()){
                    new JsonTask().execute("0");
                }else{
                    Toast.makeText(getContext(), "No connection", Toast.LENGTH_SHORT).show();
                }
                refreshLayout.setRefreshing(false);
                refreshLayout.setEnabled(true);
            }
        });

        if(!mahs.equals("nodata")){
            new JsonTask().execute("");
        }

        return v;
    }

    public class JsonTask extends AsyncTask<String, String , List<NoticeBoardItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog.show();
        }

        @Override
        protected List<NoticeBoardItem> doInBackground(String... params) {
            try {

//                String data = "{\"listnotice\":[{\"nid\":\"99\",\"subject\":\"Ngữ Văn\",\"notice\":\"Dạy bù môn ngữ văn sẽ tổ chức bình thường như dự kiến\",\"level\":\"1\",\"deadline\":\"10/08/2013\"},{\"nid\":\"12\",\"subject\":\"GDCD\",\"notice\":\"thong bao hoc bu\",\"level\":\"2\",\"deadline\":\"10/08/2013\"},{\"nid\":\"11\",\"subject\":\"Toán\",\"notice\":\"thong bao hoc bu\",\"level\":\"3\",\"deadline\":\"10/08/2013\"},{\"nid\":\"02\",\"subject\":\"Sinh học\",\"notice\":\"thong bao hoc bu\",\"level\":\"1\",\"deadline\":\"10/08/2013\"}]}";
                RequestManager requestManager = new RequestManager();
                String data;
                if(app.getRole().equals("2")) {
                    data = requestManager.studentGetNotice("api/post/student/get_noticeboard", app.getToken(), "0");
                }else{
                    data = requestManager.parentGetNotice("api/post/parent/get_noticeboard", app.getToken(), "0", app.getCurrentchild());
                }
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("listnotice");

                noticeBoardItemList.clear();
//                System.out.println("print thu data_+_"+data);
                for(int i = 0 ; i < jsonArray.length(); i++){
                    JSONObject finalObject = jsonArray.getJSONObject(i);
                    NoticeBoardItem noticeItem =  new NoticeBoardItem();
                    noticeItem.setId(finalObject.getString("nid"));
                    noticeItem.setSubject(finalObject.getString("subject"));
                    noticeItem.setNotice(finalObject.getString("notice"));
                    noticeItem.setLevel(finalObject.getString("level"));
                    noticeItem.setDeadline(finalObject.getString("deadline"));
                    if(app.getRole().equals("2")){
                        noticeItem.setTitle(finalObject.getString("title"));
                        noticeItem.setDatewrote(finalObject.getString("datewrote"));
                        noticeItem.setAuthor(finalObject.getString("author"));
                    }
                    noticeBoardItemList.add(noticeItem);
                }

                return noticeBoardItemList;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

            }
            return null;
        }

        @Override
        protected void onPostExecute(List<NoticeBoardItem> result) {
            super.onPostExecute(result);
//            dialog.dismiss();
            adapter.notifyDataSetChanged();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



}
