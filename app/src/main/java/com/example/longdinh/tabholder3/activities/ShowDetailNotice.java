package com.example.longdinh.tabholder3.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.models.EmailItem;
import com.example.longdinh.tabholder3.models.NavItemChild;
import com.example.longdinh.tabholder3.models.NoticeBoardItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by long dinh on 13/05/2016.
 */
public class ShowDetailNotice extends Activity {
    TextView tvTitle;
    TextView tvSubject;
    TextView tvAuthor;
    TextView tvNgayTao;
    TextView tvDeadline;
    TextView tvNotice;
    MyApplication app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_detail_notice);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvSubject = (TextView) findViewById(R.id.tvSubject);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        tvNgayTao = (TextView) findViewById(R.id.tvNgayTao);
        tvDeadline = (TextView) findViewById(R.id.tvDeadline);
        tvNotice = (TextView) findViewById(R.id.tvNotice);
        app = (MyApplication) getApplication();

        Intent getData = getIntent();
        String nid = getData.getStringExtra("nid");
        String date =getData.getStringExtra("date");

        List<NoticeBoardItem> noticeBoardItemList;
        if(date.equals("2")){
            noticeBoardItemList = app.getData_NoticeListT2();
        }else if(date.equals("3")){
            noticeBoardItemList = app.getData_NoticeListT3();
        }else if(date.equals("4")){
            noticeBoardItemList = app.getData_NoticeListT4();
        }else if(date.equals("5")){
            noticeBoardItemList = app.getData_NoticeListT5();
        }else if(date.equals("6")){
            noticeBoardItemList = app.getData_NoticeListT6();
        }else{
            noticeBoardItemList = app.getData_NoticeListT7();
        }
        int sizeCheck = noticeBoardItemList.size();
        sizeCheck = sizeCheck < 5?sizeCheck:5;

        for(int i = 0; i < sizeCheck; i++){
            NoticeBoardItem item = noticeBoardItemList.get(i);
            if(nid.equals(item.getId())){
                tvSubject.setText(item.getSubject());
                tvTitle.setText(item.getTitle());
                tvAuthor.setText(item.getAuthor());
                tvNgayTao.setText(item.getDatewrote());
                tvDeadline.setText(item.getDeadline());
                tvNotice.setText(Html.fromHtml(item.getNotice()));
                return;
            }
        }
        if(isOnline()){
            System.out.println("read notice online -----");
            new showNoticeDetail().execute(nid);
        }
    }

    public class showNoticeDetail extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {
            String nid = params[0];
//            String data ="{\"subject\":\"Ngữ Văn\",\"author\":\"Đinh Mạnh Quang\",\"ngaytao\":\"10/08/2016\",\"level\":\"1\",\"deadline\":\"12/08/2016\",\"notice\":\"Thông báo về kế hoạch dạy bù trong tuần này sẽ diễn ra bình thường theo dự kiến\"}";

            RequestManager requestManager = new RequestManager();
            String data;
            String url = "api/post/get_notice_detail";
            if(app.getRole().equals("3")){
                data = requestManager.getNoticeDetail(url, app.getToken(), nid, app.getCurrentchild());
            }
            else{
                data = requestManager.getNoticeDetail(url, app.getToken(), nid, "self");
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject notice = new JSONObject(result);
                tvSubject.setText(notice.getString("subject"));
                tvTitle.setText(notice.getString("title"));
                tvAuthor.setText(notice.getString("author"));
                tvNgayTao.setText(notice.getString("ngaytao"));
                tvDeadline.setText(notice.getString("deadline"));
                tvNotice.setText(Html.fromHtml(notice.getString("notice")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



}
