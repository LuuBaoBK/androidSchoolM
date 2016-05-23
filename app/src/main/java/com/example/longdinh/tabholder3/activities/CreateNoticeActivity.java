package com.example.longdinh.tabholder3.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.fragments.NoticeTeacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
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
    EditText etCalendar;
    ListView lvClass;
    EditText etContent;
    Button btnSave;
    List<String> listClassName = new ArrayList<>();
    List<String> listClassId = new ArrayList<>();
    ArrayAdapter<String> adapter;
    int mYear;
    int mMonth;
    int mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_notice);

        etSubject = (EditText) findViewById(R.id.etSubject);
        etTitle = (EditText) findViewById(R.id.etTitle);
        radio_level = (RadioGroup) findViewById(R.id.radio_level);
        etCalendar= (EditText) findViewById(R.id.etDeadline);
        checkbox_nextclass= (CheckBox) findViewById(R.id.checkbox_nextclass);
        lvClass = (ListView) findViewById(R.id.listviewClass);
        etContent= (EditText) findViewById(R.id.etContent);

        btnSave= (Button) findViewById(R.id.btnSave);

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");


        lvClass.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        lvClass.setItemsCanFocus(false);
        lvClass.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });




       adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, listClassName);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send du lieu len server
                System.out.println("Notice sending....------");
                new JsonTask().execute("");

            }
        });

        etCalendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate=Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth=mcurrentDate.get(Calendar.MONTH);
                mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker=new DatePickerDialog(CreateNoticeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        etCalendar.setText(selectedday + "-" + (selectedmonth+1) + "-" + selectedyear);
                    }
                },mYear, mMonth, mDay);
        mDatePicker.getDatePicker().setCalendarViewShown(false);
        mDatePicker.setTitle("Select date");
        mDatePicker.show();  }
});
        lvClass.setAdapter(adapter);


        new getClass().execute();
    }



    public class getClass extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {
            String data = "{\"listclass\":[{\"idclass\":\"16_6A1\",\"classname\":\"6A1\"},{\"idclass\":\"16_6A2\",\"classname\":\"6A2\"},{\"idclass\":\"16_6A3\",\"classname\":\"6A3\"},{\"idclass\":\"16_7A1\",\"classname\":\"7A1\"},{\"idclass\":\"16_8A1\",\"classname\":\"8A1\"}]}";
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray listclassjson = jsonObject.getJSONArray("listclass");
                for(int i = 0 ; i< listclassjson.length(); i++){
                    JSONObject one = listclassjson.getJSONObject(i);
                    listClassName.add(one.getString("classname"));
                    listClassId.add(one.getString("idclass"));
                    System.out.println(listclassjson.getString(i));
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

            int selectedId = radio_level.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton radioLevelButton = (RadioButton) findViewById(selectedId);
            int level = 1;

            if(radioLevelButton.getText().toString().equals("Straightway")){
                level = 0;
            }else if(radioLevelButton.getText().toString().equals("Gradual")){
                level = 1;
            }else{
                level = 2;
            }


            String selected = "";
            int cntChoice = lvClass.getCount();
            SparseBooleanArray sparseBooleanArray = lvClass.getCheckedItemPositions();
            for(int i = 0; i < cntChoice; i++){
                if(sparseBooleanArray.get(i)) {
                    selected += listClassId.get(i).toString() + ", ";
                }
            }

            System.out.println(etSubject.getText()  + ", " + etTitle.getText()  + ", "
                    + radio_level.getCheckedRadioButtonId() + ", "
                    + etCalendar.getText()  +", " + selected + etContent.getText()
                    + "," + radioLevelButton.getText().toString() + "= "+level+", " + checkbox_nextclass.isChecked());



        }

        @Override
        protected String doInBackground(String... params) {

            return "Notice is sending....";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(getApplicationContext(), NoticeTeacher.class);
//            setResult(RESULT_CANCELED, intent);
//            finish();
        }
    }


}
