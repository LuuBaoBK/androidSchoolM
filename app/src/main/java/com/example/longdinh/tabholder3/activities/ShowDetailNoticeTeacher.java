package com.example.longdinh.tabholder3.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.adapters.ClassReceiveAdapter;
import com.example.longdinh.tabholder3.models.ItemClassDate;
import com.example.longdinh.tabholder3.models.NoticeTeacherItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by long dinh on 21/05/2016.
 */
public class ShowDetailNoticeTeacher extends Activity{


    TextView tvSubject;
    TextView tvTitle;
    TextView tvLevel;
    TextView tvDateWrote;
    ListView listviewClass;
    List<ItemClassDate> listClassDate  = new ArrayList<>();
    ClassReceiveAdapter adapter;
    TextView tvContent;
    String nid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_detail_notice_teacher);

        Intent intent = getIntent();
        nid = intent.getStringExtra("nid");

        tvSubject = (TextView) findViewById(R.id.tvSubject);
                tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvLevel = (TextView) findViewById(R.id.tvLevel);
                tvDateWrote = (TextView) findViewById(R.id.tvDateWrote);
        listviewClass = (ListView) findViewById(R.id.listviewClass);
                tvContent = (TextView) findViewById(R.id.tvContent);
        adapter = new ClassReceiveAdapter(getApplicationContext(), R.layout.item_class_date, listClassDate);

        listviewClass.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                v.onTouchEvent(event);
                return true;
            }
        });

        listviewClass.setAdapter(adapter);

        new showNoticeDetail().execute("");
    }




    public class showNoticeDetail extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {
            String nid = params[0];
            RequestManager requestManager = new RequestManager();
            // nhu vay doi voi teacher thi khi doc 1 notice thi se gui len server roi lay data ve khong co o local
            String data = "{\"subject\":\"Ngữ Văn\",\"title\":\"Thông báo học bù\",\"level\":\"1\",\"datewrote\":\"11/08/2016\",\"listclass\":[{\"classname\":\"9A1\",\"date\":\"13/08/2016\"},{\"classname\":\"9A2\",\"date\":\"13/08/2016\"},{\"classname\":\"9A3\",\"date\":\"13/08/2016\"}],\"content\":\"việc học bù diễn ra bình thường như thông báo đã gửi cho từng lớp\"}";
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject notice = new JSONObject(result);
                System.out.println("notice teacher: " + result);
                tvSubject.setText(notice.getString("subject"));
                tvTitle.setText(notice.getString("title"));
                tvLevel.setText(notice.getString("level"));
                tvDateWrote.setText(notice.getString("datewrote"));
                JSONArray listclass = notice.getJSONArray("listclass");
                for(int i = 0; i< listclass.length(); i++){
                    JSONObject oneclass = listclass.getJSONObject(i);
                    ItemClassDate itemClassDate = new ItemClassDate(oneclass.getString("classname"), oneclass.getString("date"));
                    listClassDate.add(itemClassDate);
                    System.out.println("classlist --" + itemClassDate.getClassname());
                }
                tvContent.setText(Html.fromHtml(notice.getString("content")));
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }










}
