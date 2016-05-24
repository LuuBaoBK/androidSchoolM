package com.example.longdinh.tabholder3.fragments;

/**
 * Created by long dinh on 12/04/2016.
 */
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.activities.MyApplication;
import com.example.longdinh.tabholder3.activities.RequestManager;
import com.example.longdinh.tabholder3.activities.ShowDetailStudent;
import com.example.longdinh.tabholder3.adapters.StudentClassAdapter;
import com.example.longdinh.tabholder3.models.StudentClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyClass extends Fragment {
    private ListView listview;
    List<StudentClass> studentClassList = new ArrayList<>();
    private MyApplication app;
    StudentClassAdapter   adapter;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.list_student_class, container, false);
        app = (MyApplication) getActivity().getApplication();

        listview = (ListView) v.findViewById(R.id.lvStudentClass);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ShowDetailStudent.class);
                intent.putExtra("mahs", studentClassList.get(position).getMaHs());
                startActivityForResult(intent, 500);
            }
        });
         adapter = new StudentClassAdapter(getContext(), R.layout.item_student, studentClassList);
        listview.setAdapter(adapter);

//        new JsonTask().execute("http://jsonparsing.parseapp.com/jsonData/moviesData.txt");

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        new JsonTask().execute("");

    }

    public class JsonTask extends AsyncTask<String, String , String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            RequestManager requestManager = new RequestManager();

            String data = requestManager.getInboxMail("api/post/teacher/get_stulist", app.getToken(), 1);
            return data;


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
//
                System.out.println("result---" + result);
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("liststudents");
                studentClassList.clear();

                for(int i = 0 ; i < jsonArray.length(); i++){
                    JSONObject finalObject = jsonArray.getJSONObject(i);
                    StudentClass student =  new StudentClass();
                    student.setImage(finalObject.getString("avatar"));
                    student.setMaHs(finalObject.getString("ma"));
                    student.setName(finalObject.getString("name"));
//                    student.setSdt(finalObject.getString("sdt"));
                    studentClassList.add(student);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

            }

            adapter.notifyDataSetChanged();
        }
    }
}

