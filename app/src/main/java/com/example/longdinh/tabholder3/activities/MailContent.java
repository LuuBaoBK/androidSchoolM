package com.example.longdinh.tabholder3.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.models.EmailItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by long dinh on 30/04/2016.
 */
public class MailContent  extends Activity implements TextWatcher{
    String date;
    String email;
    String content;
    String subject;
    String type;
    Boolean ischanged = false;
    Boolean isDraftMail =false;
    String currentString = "";
    String previousString = "";
    MyApplication app;
    String token;
    String idMail;
    EditText etNguoiNhan;
    EditText etSubject;
    EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compose_message);

        etNguoiNhan = (EditText) findViewById(R.id.etNguoiNhan);
        etSubject = (EditText) findViewById(R.id.etSubject);
        etContent = (EditText) findViewById(R.id.etContent);
        Button btnSend = (Button) findViewById(R.id.btnSend);

        app = (MyApplication) getApplicationContext();
        token = app.getToken();


        Intent getData = getIntent();
        type = getData.getStringExtra("type");
        // doan ma get ma cua nguoi dung va chen vao trong user sender

        ((TextView)findViewById(R.id.tvEmailFrom)).setText(app.getId() + "@schoolm.com");

        if(type.equals("FORWARD")){
            etContent.setText(getData.getStringExtra("content"));
            etSubject.setText("Forward:" + getData.getStringExtra("subject"));
            isDraftMail = getData.getBooleanExtra("isTrashMail", false);
        }else if(type.equals("REPLY")){
            etNguoiNhan.setText(getData.getStringExtra("receiver"));
            etSubject.setText(getData.getStringExtra("subject"));
            isDraftMail = getData.getBooleanExtra("isTrashMail", false);
        }else if(type.equals("COMPOSE")){

        }else if(type.equals("EDIT")){
            etContent.setText(getData.getStringExtra("content"));
            etSubject.setText(getData.getStringExtra("subject"));
            etNguoiNhan.setText(getData.getStringExtra("sender"));
            idMail = getData.getStringExtra("idMail");
            isDraftMail = true;
        }

        // theo doi su kien co thay doi du lieu haykhong

        etNguoiNhan.addTextChangedListener(this);
        etSubject.addTextChangedListener(this);
        etContent.addTextChangedListener(this);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etNguoiNhan.getText().toString().matches("((([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)(\\s*;\\s*|\\s*)))+"))
                {
                    Toast.makeText(MailContent.this, "Error Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isDraftMail){
                    app.addItem_DraftDeleteMail(idMail);
                    List<EmailItem> draftMailList = app.getData_DraftMailList();

                    // xoa mail tren hop thu mail rac them vao trong hop mail output
                    for(int i = 0; i < draftMailList.size(); i++){
                        EmailItem email = draftMailList.get(i);
                        RequestManager requestManager = new RequestManager();

                        if(idMail.equals(draftMailList.get(i).getId()+ "")){
                            draftMailList.remove(i);
//                            Toast.makeText(getApplicationContext(), "Draft mail list xoa mail " + idMail, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                if(isOnline()){//neu co mang thi gui len luon roi cho ket qua tra ve
                    if(isDraftMail){
                        new sentDraftMail(idMail,etSubject.getText().toString(), etNguoiNhan.getText().toString(), etContent.getText().toString()).execute();
                    }else{
                        new sentMail(etSubject.getText().toString(), etNguoiNhan.getText().toString(), etContent.getText().toString()).execute();
                    }
                }else{//neu khong co mang thi cho luu vo ben outbox
                    // outbox luu thong tin nhung mail khong gui len server dc-> khong co ma
                    List<EmailItem> outBoxmailList = app.getData_OutboxMailList();
                    int min = 0;
                    for(int i = 0; i < outBoxmailList.size(); i++){
                        EmailItem email = outBoxmailList.get(i);
                        if(email.getId() < min){
                            min = email.getId();
                        }
                    }

                    String currentDateandTime = new SimpleDateFormat("MMM dd").format(new Date());
                    EmailItem item = new EmailItem(min-1, etSubject.getText().toString(), currentDateandTime, app.getId()+"@schoolm.com", etNguoiNhan.getText().toString(), etContent.getText().toString(),false, false );
                    app.addItem_OutboxhMailList(item);
                    //save nhu mail draft
//                    Toast.makeText(getApplicationContext(), "Save as outbox mail id=" + (min-1), Toast.LENGTH_SHORT).show();

                }

                Toast.makeText(getApplicationContext(), "Mail sending...", Toast.LENGTH_SHORT).show();
                Intent infoReturn = new Intent();
                setResult(RESULT_OK, infoReturn);
                finish();
                return;
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent infoReturn = new Intent();
            if(ischanged == false){
//                Toast.makeText(getApplicationContext(), "Noi dung mail ko doi", Toast.LENGTH_SHORT).show();
            }
            else{
                String currentDateandTime = new SimpleDateFormat("MMM dd").format(new Date());
                if(isDraftMail){
//                    Toast.makeText(getApplicationContext(), "Save as draft mail... ghide", Toast.LENGTH_SHORT).show();
                    List<EmailItem> draftMailList = app.getData_DraftMailList();
                    for(int i = 0; i < draftMailList.size(); i++){
                        EmailItem email = draftMailList.get(i);
                        if(idMail.equals(draftMailList.get(i).getId()+ "")){
                            if(isOnline()){//neu co mang thi gui len luon roi cho ket qua tra ve
                                String data ="title=" + etSubject.getText().toString()
                                        + "&receiver=" + etNguoiNhan.getText().toString()
                                        + "&content=" + etContent.getText().toString()
                                        + "&id=" + idMail;
                                new sentDraft().execute(data);
                            }else{
                                app.addItem_DraftNewMail(idMail);
                            }

                            // co hay khong cung deu update thong tin cho mail nhap hien tai
                            String subject = etSubject.getText().toString();
                            subject = subject.equals("")?"No title":subject;

                            email.setSubject(subject);
                            email.setPreview(etContent.getText().toString());
                            email.setDate(currentDateandTime);
                            // va thi trong ds ve new draft cung phai luu lai trang thai cua mahi moi lam moi

//                            Toast.makeText(getApplicationContext(), "Draft mail list xoa mail " + idMail, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{//luu mail rac
//                    Toast.makeText(getApplicationContext(), "Draft mail list xoa mail " + idMail, Toast.LENGTH_SHORT).show();
                    if(isOnline()){//neu co mang
                        String data ="title=" + etSubject.getText().toString()
                                + "&receiver=" + etNguoiNhan.getText().toString()
                                + "&content=" + etContent.getText().toString()
                                + "&id=-1";
                        new sentDraft().execute(data);
                    }else{
                        List<EmailItem> draftMailList = app.getData_DraftMailList();
                        int min = 0;
                        for(int i = 0; i < draftMailList.size(); i++){
                            EmailItem email = draftMailList.get(i);
                            if(email.getId() < min){
                                min = email.getId();
                            }
                        }
                        String subject = etSubject.getText().toString();
                        subject = subject.equals("")?"No title":subject;
                        app.addItem_DraftNewMail((min-1)+"");
                        EmailItem item = new EmailItem(min-1, subject, currentDateandTime, app.getId()+"@schoolm.com", etNguoiNhan.getText().toString(), etContent.getText().toString(), false, false);
                        app.addItem_DraftMailList(item);

                    }
                }
            /// O DAY CO THE UPDATE MAIL TREN NOI BO
            }
            setResult(RESULT_CANCELED, infoReturn);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        previousString = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        currentString = s.toString();
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!previousString.equals(currentString)){
            ischanged = true;
        }
    }



    public class sentDraft extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {

            String data = params[0];
            RequestManager requestManager = new RequestManager();
            requestManager.postDataToServer("api/post/mailbox/save_draft", app.getToken(), data);
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Intent infoReturn = new Intent();
            setResult(RESULT_OK, infoReturn);
            finish();
        }
    }


    public class sentMail extends AsyncTask<String, String , String> {
        String title;
        String listEmail;
        String content;

        public sentMail(String title, String listEmail, String content) {
            this.title = title;
            this.listEmail = listEmail;
            this.content = content;
        }

        @Override
        protected String doInBackground(String... params) {

            RequestManager requestManager = new RequestManager();

            String data ="title=" + this.title + "&receiver=" + this.listEmail + "&content=" + this.content;

            requestManager.postDataToServer("api/post/mailbox/send_mail", app.getToken(), data);
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Intent infoReturn = new Intent();
            setResult(RESULT_OK, infoReturn);
            finish();
        }
    }

    public class sentDraftMail extends AsyncTask<String, String , String> {
        String title;
        String listEmail;
        String content;
        String id;

        public sentDraftMail(String id, String title, String listEmail, String content) {
            this.title = title;
            this.listEmail = listEmail;
            this.content = content;
            this.id = id;
        }

        @Override
        protected String doInBackground(String... params) {

            RequestManager requestManager = new RequestManager();

            String data ="id="+id+"&title=" + this.title + "&receiver=" + this.listEmail + "&content=" + this.content;

            requestManager.postDataToServer("api/post/mailbox/send_draftmail", app.getToken(), data);

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Intent infoReturn = new Intent();
            setResult(RESULT_OK, infoReturn);
            finish();
        }
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
