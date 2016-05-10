package com.example.longdinh.tabholder3.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.longdinh.tabholder3.R;

import org.json.JSONException;
import org.json.JSONObject;

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
public class MailContent  extends Activity{
    String date;
    String email;
    String preview;
    String subject;
    String type;
    Boolean editable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compose_message);

        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etSubject = (EditText) findViewById(R.id.etSubject);
        final EditText etContent = (EditText) findViewById(R.id.etContent);
        final Button btnSend = (Button) findViewById(R.id.btnSend);


        Intent getData = getIntent();
        type = getData.getStringExtra("type");
        if(type.equals("SHOW")){
            etEmail.setFocusable(false);
            etSubject.setFocusable(false);
            etContent.setFocusable(false);
            btnSend.setText("EDIT");
            editable = false;
        }else if(type.equals("EDIT")){

        }else if(type.equals("COMPOSE")){

        }

        date = getData.getStringExtra("date");
        email = getData.getStringExtra("sender");
        preview = getData.getStringExtra("preview");
        subject = getData.getStringExtra("subject");

        etEmail.setText(email);
        etSubject.setText(subject);
        etContent.setText(preview);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editable == false){
                    etEmail.setFocusableInTouchMode(true);
                    etSubject.setFocusableInTouchMode(true);
                    etContent.setFocusableInTouchMode(true);
                    btnSend.setText("SEND");
                    editable = true;
                    return;
                }

                //if(etEmail.getText().toString().matches("^(([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5}){1,25})+([;.](([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5}){1,25})+)*$"))
                if(!etEmail.getText().toString().matches("(([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)(\\s*;\\s*|\\s*$))*"))
                {
                    Toast.makeText(MailContent.this, "Error Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                //class dung chung nen can thay doi url cua request.
                new sentMail( etSubject.getText().toString(),etEmail.getText().toString(), etContent.getText().toString()).execute("url send mail");
                return;
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent infoReturn = new Intent();
            if(editable == false){
                //trong truong hop doc mail
                infoReturn.putExtra("isSave", false);
            }
            else{
                infoReturn.putExtra("isSave", true);
                infoReturn.putExtra("sender", ((EditText) findViewById(R.id.etEmail)).getText().toString());
                infoReturn.putExtra("subject", ((EditText) findViewById(R.id.etSubject)).getText().toString());
                infoReturn.putExtra("preview", ((EditText)findViewById(R.id.etContent)).getText().toString() );

                DateFormat df = new SimpleDateFormat("d MMM yyyy, HH:mm");
                String date = df.format(Calendar.getInstance().getTime());
                new sentMail( ((EditText) findViewById(R.id.etSubject)).getText().toString(),
                        ((EditText) findViewById(R.id.etEmail)).getText().toString(),
                        ((EditText)findViewById(R.id.etContent)).getText().toString()).execute("url send mail");

                infoReturn.putExtra("date", date);

            }
            setResult(RESULT_CANCELED, infoReturn);
            finish();
        }
        return super.onKeyDown(keyCode, event);
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
