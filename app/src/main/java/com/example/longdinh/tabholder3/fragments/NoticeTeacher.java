package com.example.longdinh.tabholder3.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.activities.CreateNoticeActivity;
import com.example.longdinh.tabholder3.activities.MyApplication;
import com.example.longdinh.tabholder3.activities.RequestManager;
import com.example.longdinh.tabholder3.activities.ShowDetailNoticeTeacher;
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
public class NoticeTeacher extends Fragment {

    ListView listView;
    ProgressDialog dialog;
    List<NoticeBoardItem> noticeBoardItemList;
    NoticeBoardAdapter adapter;
    String mahs;
    MyApplication app;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.notice_teacher, container, false);
        app = (MyApplication) getActivity().getApplication();
        listView = (ListView) v.findViewById(R.id.lvNotice);
        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");
        noticeBoardItemList = new ArrayList<>();
        adapter = new NoticeBoardAdapter(getContext(), R.layout.item_noticeboard, noticeBoardItemList);

        listView.setAdapter(adapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                new showNoticeDetail().execute(noticeBoardItemList.get(position).getId() + "");
                Intent intent = new Intent(getContext(), ShowDetailNoticeTeacher.class);
                intent.putExtra("nid", noticeBoardItemList.get(position).getId());
                startActivityForResult(intent, 333);
            }
        });

        Button btnNew = (Button) v.findViewById(R.id.btnNew);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createNotice = new Intent(getContext(), CreateNoticeActivity.class);
                startActivityForResult(createNotice, 444);
            }
        });

        new JsonTask().execute("");

        return v;
    }

    public class JsonTask extends AsyncTask<String, String , String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            RequestManager requestManager = new RequestManager();
            System.out.println(app.getToken() + "--- my token");
            String data = requestManager.getInboxMail("api/post/teacher/get_te_noticeboard", app.getToken(),1);

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

              System.out.println(result+"-notice teacher");
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("listnotice");

                noticeBoardItemList.clear();
                for(int i = 0 ; i < jsonArray.length(); i++){
                    JSONObject finalObject = jsonArray.getJSONObject(i);
                    NoticeBoardItem noticeItem =  new NoticeBoardItem();
                    noticeItem.setId(finalObject.getString("nid"));
                    noticeItem.setSubject(finalObject.getString("title"));
                    noticeItem.setNotice(finalObject.getString("content"));
                    noticeItem.setLevel(finalObject.getString("level"));
                    noticeItem.setDeadline(finalObject.getString("deadline"));
                    noticeBoardItemList.add(noticeItem);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
            }


            dialog.dismiss();
            adapter.notifyDataSetChanged();
        }
    }



}
