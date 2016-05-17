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
import com.example.longdinh.tabholder3.activities.ShowDetailStudent;
import com.example.longdinh.tabholder3.adapters.StudentClassAdapter;
import com.example.longdinh.tabholder3.models.StudentClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyClass extends Fragment {
    private ListView listview;
    List<StudentClass> studentClassList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.list_student_class, container, false);
        listview = (ListView) v.findViewById(R.id.lvStudentClass);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ShowDetailStudent.class);
                intent.putExtra("mahs", studentClassList.get(position).getMaHs());
                startActivityForResult(intent, 500);
            }
        });

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
        new JsonTask().execute("http://jsonparsing.parseapp.com/jsonData/moviesData.txt");

    }

    public class JsonTask extends AsyncTask<String, String , List<StudentClass>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<StudentClass> doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.connect();


                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                StringBuffer stringBuffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }

                String data = "{\"liststudents\":[\t{\t\"avatar\":\"http://jsonparsing.parseapp.com/jsonData/images/avengers.jpg\",\t\"name\":\"Trần Hữu Cầu\"\t,\"ma\":\"s_0000001\",\t\"sdt\":\"0120968899\" \t},\t{\t\"avatar\":\"http://jsonparsing.parseapp.com/jsonData/images/interstellar.jpg\",\t\"name\":\"Trần Hữu Cát\"\t,\"ma\":\"s_0000002\",\t\"sdt\":\"012096887799\"\t}]}";
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("liststudents");
                studentClassList.clear();

                for(int i = 0 ; i < jsonArray.length(); i++){
                    JSONObject finalObject = jsonArray.getJSONObject(i);
                     StudentClass student =  new StudentClass();
                    student.setImage(finalObject.getString("avatar"));
                    student.setMaHs(finalObject.getString("ma"));
                    student.setName(finalObject.getString("name"));
                    student.setSdt(finalObject.getString("sdt"));
                    studentClassList.add(student);
                }
                return studentClassList;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
                try {
                    if (bufferedReader != null)
                        bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<StudentClass> result) {
            super.onPostExecute(result);
            StudentClassAdapter   adapter = new StudentClassAdapter(getContext(), R.layout.item_student, result);
            listview.setAdapter(adapter);
        }
    }
}

