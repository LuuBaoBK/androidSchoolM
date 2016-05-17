package com.example.longdinh.tabholder3.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by long dinh on 15/05/2016.
 */
public class ReadMailAcitivity extends Activity{

    TextView tvSubject;
    TextView tvStand;
    TextView tvSender;
    TextView tvNguoiNhan;
    TextView tvDate;
    TextView tvContent;
    LinearLayout btnReply;
    LinearLayout btnForward;
    String id;
    Boolean isTrashMail = false;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mail_read);

        tvSubject = (TextView) findViewById(R.id.tvSubject);
         tvStand= (TextView) findViewById(R.id.tvStand);
         tvSender= (TextView) findViewById(R.id.tvSender);
         tvNguoiNhan= (TextView) findViewById(R.id.tvNguoiNhan);
         tvDate= (TextView) findViewById(R.id.tvDate);
         tvContent= (TextView) findViewById(R.id.tvContent);
        btnReply = (LinearLayout) findViewById(R.id.btnReply);
         btnForward = (LinearLayout) findViewById(R.id.btnForward);


        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        new getMailDetail().execute(id);

        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MailContent.class);
                intent.putExtra("type", "FORWARD");
                intent.putExtra("content", tvContent.getText().toString());
                intent.putExtra("subject", tvSubject.getText());
                intent.putExtra("isTrashMail", isTrashMail);
                startActivityForResult(intent, 123);
                System.out.println("da kich hoat su kien forward"  + tvContent.getText());
            }
        });


        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MailContent.class);
                intent.putExtra("type", "REPLY");
                if( tvNguoiNhan.getText().equals("To me")){
                    intent.putExtra("sender", tvSender.getText());
                }else{
                    intent.putExtra("sender", tvNguoiNhan.getText());
                }
                intent.putExtra("subject", tvSubject.getText());
                intent.putExtra("isTrashMail", isTrashMail);
                startActivityForResult(intent, 321);
                System.out.println("da kich hoat su kien reply");
            }
        });
    }

    public class getMailDetail extends AsyncTask<String, String , String> {
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
                tvStand.setText(Character.toString(Character.toUpperCase(email.getString("author").charAt(0))));
                tvSubject.setText(email.getString("title"));
                tvSender.setText(email.getString("author"));
                tvNguoiNhan.setText("To me");
                tvDate.setText(email.getString("date_time"));
                tvContent.setText(email.getString("content"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
