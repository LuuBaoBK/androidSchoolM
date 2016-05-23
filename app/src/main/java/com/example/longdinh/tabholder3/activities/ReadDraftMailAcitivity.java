package com.example.longdinh.tabholder3.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.models.EmailItem;

import java.util.List;

/**
 * Created by long dinh on 15/05/2016.
 */
public class ReadDraftMailAcitivity extends Activity{

    TextView tvSubject;
    TextView tvStand;
    TextView tvSender;
    TextView tvNguoiNhan;
    TextView tvDate;
    TextView tvContent;
    LinearLayout btnEdit;
    String id;
    Boolean isTrashMail = true;
    MyApplication app;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maildraft_read);

        tvSubject = (TextView) findViewById(R.id.tvSubject);
         tvStand= (TextView) findViewById(R.id.tvStand);
         tvSender= (TextView) findViewById(R.id.tvSender);
         tvNguoiNhan= (TextView) findViewById(R.id.tvNguoiNhan);
         tvDate= (TextView) findViewById(R.id.tvDate);
         tvContent= (TextView) findViewById(R.id.tvContent);
         btnEdit = (LinearLayout) findViewById(R.id.btnEdit);

        app = (MyApplication) getApplication();


        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        //trong truong hop nay se xu li-> neu nhu mail co luu thi se khong can phai len gui request len server
        new getMailDetail().execute(id);
        //thuc chat ko can gui request len server vi tat cac mail draft deu o luu


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MailContent.class);
                intent.putExtra("type", "EDIT");
                intent.putExtra("idMail", id);
                intent.putExtra("content", tvContent.getText().toString());
                intent.putExtra("subject", tvSubject.getText());
                intent.putExtra("sender", tvNguoiNhan.getText());
                intent.putExtra("isTrashMail", isTrashMail);
                startActivityForResult(intent, 124);
                System.out.println("da kich hoat su kien edit"  + tvContent.getText());
            }
        });

    }

    public class getMailDetail extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            {
                // vay de dam bao tinh toan ven thi nhung mail nao co trong list draft thi deu co the doc dc
                List<EmailItem> draftMailList = app.getData_DraftMailList();
                for(int i = 0; i < draftMailList.size(); i++){
                    EmailItem email = draftMailList.get(i);
                    if(id.equals(email.getId()+ "")){
                        System.out.println("dang doc mail luu draft -----");
                        return email.toString();
                    }
                }

                //GUI REQUEST LEN SERVER DE LAY DETAIL MAIL
                if(isOnline()){
                    String retur = "{\"id\":-1,\"content\":\"Noi dung khong quan trong chay dung la dc\",\"title\":\"Mail sent to server\",\"date_time\":\"Apr 29\",\"author\":\"t0001@schoolm.com\",\"receiver\":\"t_000002@schoolm.com\"}";
                    return retur;
                }
            }




            String retur = new String("{ \"id\": 1,\"content\": \"Noi dung khong quan trong chay dung la dc\",\"title\": \"Mail sent to server\",\"date_time\": \"Apr 29\",\"author\": \"t0001@schoolm.com\"}");
            return retur;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            List<EmailItem> draftMailList = app.getData_DraftMailList();
            for(int i = 0; i < draftMailList.size(); i++){
                EmailItem email = draftMailList.get(i);
                if(id.equals(draftMailList.get(i).getId()+ "")){
                    tvStand.setText(Character.toString(Character.toUpperCase(email.getSubject().charAt(0))));
                    tvSubject.setText(email.getSubject());
                    tvSender.setText(app.getId()+"@schoolm.com");
                    tvNguoiNhan.setText(email.getSender());
                    tvDate.setText(email.getDate());
                    tvContent.setText(email.getContent());
                    System.out.println("dang doc mail draft-----");
                    return;
                }
            }


//            try {
//                JSONObject email = new JSONObject(result);
//                tvStand.setText(Character.toString(Character.toUpperCase(email.getString("author").charAt(0))));
//                tvSubject.setText(email.getString("title"));
//                tvSender.setText(email.getString("author"));
//                tvNguoiNhan.setText("To me");
//                tvDate.setText(email.getString("date_time"));
//                tvContent.setText(email.getString("content"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
