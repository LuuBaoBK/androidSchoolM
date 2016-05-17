package com.example.longdinh.tabholder3.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;

import org.json.JSONException;
import org.json.JSONObject;

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

        Intent getData = getIntent();
        String nid = getData.getStringExtra("nid");
        new showNoticeDetail().execute(nid);
    }

    public class showNoticeDetail extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String retur ="{\"subject\":\"Ngữ Văn\",\"author\":\"Đinh Mạnh Quang\",\"ngaytao\":\"10/08/2016\",\"level\":\"1\",\"deadline\":\"12/08/2016\",\"notice\":\"Thông báo về kế hoạch dạy bù trong tuần này sẽ diễn ra bình thường theo dự kiến\"}";
            return retur;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject notice = new JSONObject(result);
                tvSubject.setText(notice.getString("subject"));
                tvAuthor.setText(notice.getString("author"));
                tvNgayTao.setText(notice.getString("ngaytao"));
                tvDeadline.setText(notice.getString("deadline"));
                tvNotice.setText(notice.getString("notice"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}
