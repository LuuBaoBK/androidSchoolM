package com.example.longdinh.tabholder3.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.fragments.NoticeTeacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by long dinh on 16/05/2016.
 */
public class CreateNoticeActivity extends Activity {

    ProgressDialog dialog;
    EditText etSubject;
    EditText etTitle;
    RadioGroup radio_level;
    CheckBox checkbox_nextclass;
    EditText etDeadline;
    ListView listviewClass;
    EditText etContent;
    Button btnSave;
    List<String> listClass = new ArrayList<>();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_notice);

        etSubject = (EditText) findViewById(R.id.etSubject);
        etTitle = (EditText) findViewById(R.id.etTitle);
        radio_level = (RadioGroup) findViewById(R.id.radio_level);
        etDeadline= (EditText) findViewById(R.id.etDeadline);
        checkbox_nextclass= (CheckBox) findViewById(R.id.checkbox_nextclass);
        listviewClass = (ListView) findViewById(R.id.listviewClass);
        etContent= (EditText) findViewById(R.id.etContent);

        btnSave= (Button) findViewById(R.id.btnSave);

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");


        listviewClass.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
       adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listClass);

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



    public class getClass extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {
            String data = "{\"listclass\":[\"6A1\",\"7A1\",\"6A2\",\"6A3\",\"8A1\"]}";
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray listclass = jsonObject.getJSONArray("listclass");
                for(int i = 0 ; i< listclass.length(); i++){
                    listClass.add(listclass.getString(i));
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }






    public class JsonTask extends AsyncTask<String, String , String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
            System.out.println(etSubject.getText()  + ", " + etTitle.getText()  + ", "
                    + radio_level.getCheckedRadioButtonId() + ", "
                    + etDeadline.getText()  +", " + listviewClass.getSelectedItemPosition() + etContent.getText());
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
