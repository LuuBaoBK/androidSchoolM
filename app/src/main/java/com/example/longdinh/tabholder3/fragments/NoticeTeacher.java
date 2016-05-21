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
import com.example.longdinh.tabholder3.activities.MyApplication;
import com.example.longdinh.tabholder3.activities.ShowDetailNotice;
import com.example.longdinh.tabholder3.activities.CreateNoticeActivity;
import com.example.longdinh.tabholder3.activities.ShowDetailNoticeTeacher;
import com.example.longdinh.tabholder3.adapters.NoticeBoardAdapter;
import com.example.longdinh.tabholder3.models.NoticeBoardItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

        new JsonTask().execute("http://jsonparsing.parseapp.com/jsonData/moviesData.txt");

        return v;
    }

    public class JsonTask extends AsyncTask<String, String , List<NoticeBoardItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected List<NoticeBoardItem> doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                StringBuffer stringBuffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }

                String data = "{\"listnotice\":[{\"nid\":\"99\",\"subject\":\"Ngữ Văn\",\"notice\":\"Dạy bù môn ngữ văn sẽ tổ chức bình thường như dự kiến\",\"level\":\"1\",\"deadline\":\"10/08/2013\"},{\"nid\":\"12\",\"subject\":\"GDCD\",\"notice\":\"thong bao hoc bu\",\"level\":\"2\",\"deadline\":\"10/08/2013\"},{\"nid\":\"11\",\"subject\":\"Toán\",\"notice\":\"thong bao hoc bu\",\"level\":\"3\",\"deadline\":\"10/08/2013\"},{\"nid\":\"02\",\"subject\":\"Sinh học\",\"notice\":\"thong bao hoc bu\",\"level\":\"1\",\"deadline\":\"10/08/2013\"}]}";
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("listnotice");

                noticeBoardItemList.clear();
                for(int i = 0 ; i < jsonArray.length(); i++){
                    JSONObject finalObject = jsonArray.getJSONObject(i);
                    NoticeBoardItem noticeItem =  new NoticeBoardItem();
                    noticeItem.setId(finalObject.getString("nid"));
                    noticeItem.setSubject(finalObject.getString("subject"));
                    noticeItem.setNotice(finalObject.getString("notice"));
                    noticeItem.setLevel(finalObject.getString("level"));
                    noticeItem.setDeadline(finalObject.getString("deadline"));
                    noticeBoardItemList.add(noticeItem);
                }

                return noticeBoardItemList;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
                try {
                    if (bufferedReader != null)
                        bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<NoticeBoardItem> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            adapter.notifyDataSetChanged();
        }
    }



}
