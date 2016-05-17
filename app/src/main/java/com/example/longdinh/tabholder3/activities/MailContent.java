package com.example.longdinh.tabholder3.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.longdinh.tabholder3.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    Boolean isTrashMail =false;
    String currentString = "";
    String previousString = "";
    MyApplication app;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compose_message);

        final EditText etNguoiNhan = (EditText) findViewById(R.id.etNguoiNhan);
        final EditText etSubject = (EditText) findViewById(R.id.etSubject);
        final EditText etContent = (EditText) findViewById(R.id.etContent);
        final Button btnSend = (Button) findViewById(R.id.btnSend);

        app = (MyApplication) getApplicationContext();
        token = app.getToken();


        Intent getData = getIntent();
        type = getData.getStringExtra("type");
        // doan ma get ma cua nguoi dung va chen vao trong user sender

        ((TextView)findViewById(R.id.tvEmailFrom)).setText(app.getId() + "@schoolm.com");
        System.out.println("id get dc la -----" + app.getId() );

        if(type.equals("FORWARD")){
            etContent.setText(getData.getStringExtra("content"));
            etSubject.setText(getData.getStringExtra("subject"));
            isTrashMail = getData.getBooleanExtra("isTrashMail", false);
            System.out.println("data content da get:" + getData.getStringExtra("subject"));
        }else if(type.equals("REPLY")){
            etNguoiNhan.setText(getData.getStringExtra("sender"));
            etSubject.setText(getData.getStringExtra("subject"));
            isTrashMail = getData.getBooleanExtra("isTrashMail", false);
        }else if(type.equals("COMPOSE")){

        }

        // theo doi su kien co thay doi du lieu haykhong

        etNguoiNhan.addTextChangedListener(this);
        etSubject.addTextChangedListener(this);
        etContent.addTextChangedListener(this);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etNguoiNhan.getText().toString().matches("(([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)(\\s*;\\s*|\\s*$))*"))
                {
                    Toast.makeText(MailContent.this, "Error Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isTrashMail){
                    // delete mail rac tren server
                }
//                new sentMail( etSubject.getText().toString(),etNguoiNhan.getText().toString(), etContent.getText().toString()).execute("url send mail");
                Toast.makeText(getApplicationContext(), "Mail sending...", Toast.LENGTH_SHORT).show();
                return;
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent infoReturn = new Intent();
            if(ischanged == false){
                // khogn lam gi het
            }
            else{
                if(isTrashMail){
                    //ghi de
                    Toast.makeText(getApplicationContext(), "Save as draft mail... ghide", Toast.LENGTH_SHORT).show();
                }
                else{
                    //luu mail rac
                    Toast.makeText(getApplicationContext(), "Save as draft mail... khongde", Toast.LENGTH_SHORT).show();
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
        System.out.println("before change--" + s);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        currentString = s.toString();
        System.out.println("on change --" + s);
    }

    @Override
    public void afterTextChanged(Editable s) {

        if(!previousString.equals(currentString)){
            ischanged = true;
            System.out.println("is changed");
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
