package com.example.longdinh.tabholder3.activities;

import android.app.Activity;
import android.content.Intent;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
        System.out.println("id get dc la -----" + app.getId() );

        if(type.equals("FORWARD")){
            etContent.setText(getData.getStringExtra("content"));
            etSubject.setText("Forward:" + getData.getStringExtra("subject"));
            isDraftMail = getData.getBooleanExtra("isTrashMail", false);
            System.out.println("data content da get:" + getData.getStringExtra("subject"));
        }else if(type.equals("REPLY")){
            etNguoiNhan.setText(getData.getStringExtra("sender"));
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
//                if(!etNguoiNhan.getText().toString().matches("(([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)(\\s*;\\s*|\\s*$))"))
//                {
//                    Toast.makeText(MailContent.this, "Error Email Address", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if(isDraftMail){
                    app.addItem_DraftDeleteMail(idMail);
                    List<EmailItem> draftMailList = app.getData_DraftMailList();

                    // xoa mail tren hop thu mail rac them vao trong hop mail output
                    for(int i = 0; i < draftMailList.size(); i++){
                        EmailItem email = draftMailList.get(i);
                        if(idMail.equals(draftMailList.get(i).getId()+ "")){
                            draftMailList.remove(i);
                            System.out.println("draft mail list- xoa mail " + idMail);
                            Toast.makeText(getApplicationContext(), "Draft mail list xoa mail " + idMail, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                if(false){//neu co mang thi gui len luon roi cho ket qua tra ve
                    //
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
                    EmailItem item = new EmailItem(min-1, etSubject.getText().toString(), currentDateandTime, etNguoiNhan.getText().toString(), etContent.getText().toString());
                    app.addItem_OutboxhMailList(item);
                    //save nhu mail draft
                    System.out.println("them vao trong outbox moi ");
                    Toast.makeText(getApplicationContext(), "Save as outbox mail id=" + (min-1), Toast.LENGTH_SHORT).show();

                }




//                new sentMail( etSubject.getText().toString(),etNguoiNhan.getText().toString(), etContent.getText().toString()).execute("url send mail");
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
                System.out.println("Noi dung mai khong co doi");
                Toast.makeText(getApplicationContext(), "Noi dung mail ko doi", Toast.LENGTH_SHORT).show();
            }
            else{
                String currentDateandTime = new SimpleDateFormat("MMM dd").format(new Date());
                if(isDraftMail){
                    //ghi de
                    Toast.makeText(getApplicationContext(), "Save as draft mail... ghide", Toast.LENGTH_SHORT).show();

                    app.addItem_DraftDeleteMail(idMail);
                    List<EmailItem> draftMailList = app.getData_DraftMailList();

                    for(int i = 0; i < draftMailList.size(); i++){
                        EmailItem email = draftMailList.get(i);
                        if(idMail.equals(draftMailList.get(i).getId()+ "")){
                            if(false){//neu co mang thi gui len luon roi cho ket qua tra ve
                                //
                            }else{//neu khong co mang thi cho luu vo ben outbox
                                app.getData_OutboxMailList().add(0, email);//  thieu thong tin ve thoi gian update
                                System.out.println("OutboxMailList --- them mail " + idMail);
                                Toast.makeText(getApplicationContext(), "OutboxMailList them mail " + idMail, Toast.LENGTH_SHORT).show();
                            }

                            email.setSubject(etSubject.getText().toString());
                            email.setPreview(etContent.getText().toString());
                            email.setDate(currentDateandTime);

                            System.out.println("draft mail list- xoa mail " + idMail);
                            Toast.makeText(getApplicationContext(), "Draft mail list xoa mail " + idMail, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    //luu mail rac

                    //mail draft se luu bang so am theo chieu nguoc lai bat dau tu -1
                    app.addItem_DraftDeleteMail(idMail);
                    List<EmailItem> draftMailList = app.getData_DraftMailList();
                    int min = 0;
                    for(int i = 0; i < draftMailList.size(); i++){
                        EmailItem email = draftMailList.get(i);
                        if(email.getId() < min){
                            min = email.getId();
                        }
                    }

                    System.out.println("draft mail list- xoa mail " + idMail);
                    Toast.makeText(getApplicationContext(), "Draft mail list xoa mail " + idMail, Toast.LENGTH_SHORT).show();
                    if(false){//neu co mang
                        //update save mail moi
                    }else{
                        app.addItem_DraftNewMail((min-1)+"");
                    }

                    EmailItem item = new EmailItem(min-1, etSubject.getText().toString(), currentDateandTime, etNguoiNhan.getText().toString(), etContent.getText().toString());
                    app.addItem_DraftMailList(item);
                    //save nhu mail draft
                    System.out.println("save nhu mail draft moi ");
                    Toast.makeText(getApplicationContext(), "Save as draft mail... khongde", Toast.LENGTH_SHORT).show();
//                    ham lay chi so lon nhat cua mail draft tai dien diem hien tai

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
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            String url_ = Constant.ROOT_API + "api/login";
            //TO DO
            try {
                URL url = new URL(url_);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                //push data up to server
                OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
                out.write("data=" + this.title + "|" + this.listEmail + "|" + this.content);
                out.close();
                return null;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "e1";
            } catch (IOException e) {
                e.printStackTrace();
                return "e2";
            } finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
                try {
                    if (bufferedReader != null)
                        bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "e4";
                }
            }
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Intent infoReturn = new Intent();
            setResult(RESULT_OK, infoReturn);
            finish();
        }
    }
}
