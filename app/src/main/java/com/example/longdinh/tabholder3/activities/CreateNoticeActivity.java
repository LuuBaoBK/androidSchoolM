package com.example.longdinh.tabholder3.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.fragments.NoticeTeacher;

/**
 * Created by long dinh on 16/05/2016.
 */
public class CreateNoticeActivity extends Activity {

    ProgressDialog dialog;
    EditText etSubject;
    EditText etAuthor;
    EditText etDeadline;
    EditText etNotice;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_notice);

        etSubject = (EditText) findViewById(R.id.etSubject);
        etAuthor= (EditText) findViewById(R.id.etAuthor);
        etDeadline= (EditText) findViewById(R.id.etDeadline);
        etNotice= (EditText) findViewById(R.id.etNotice);
        btnSave= (Button) findViewById(R.id.btnSave);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send du lieu len server
                System.out.println("Notice sending....------");
                Intent intent = new Intent(getApplicationContext(), NoticeTeacher.class);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }


    public class JsonTask extends AsyncTask<String, String , String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            return "Notice is sending....";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }


}
