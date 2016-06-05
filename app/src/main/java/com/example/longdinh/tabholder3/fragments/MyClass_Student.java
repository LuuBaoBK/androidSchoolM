package com.example.longdinh.tabholder3.fragments;

/**
 * Created by long dinh on 12/04/2016.
 */
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.activities.MyApplication;
import com.example.longdinh.tabholder3.activities.RequestManager;
import com.example.longdinh.tabholder3.adapters.TeacherClassAdapter;
import com.example.longdinh.tabholder3.models.TeacherInClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyClass_Student extends Fragment {
    private ListView listview;
    List<TeacherInClass> teacherInClasses = new ArrayList<>();
    private MyApplication app;
    TeacherClassAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.list_teacher_class, container, false);
        app = (MyApplication) getActivity().getApplication();

        TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
        tvTitle.setText("DANH SÁCH GIÁO VIÊN");
        listview = (ListView) v.findViewById(R.id.lvStudentClass);
         adapter = new TeacherClassAdapter(getContext(), R.layout.item_teacher_subject, teacherInClasses);
        listview.setAdapter(adapter);

        new JsonTask().execute("");

        return v;
    }


    public class JsonTask extends AsyncTask<String, String , String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("on pre execute---");
        }

        @Override
        protected String doInBackground(String... params) {

            RequestManager requestManager = new RequestManager();
//            String data = "{\"listteachers\":[{\"avatar\":\"empty\",\"name\":\"Phan quoc Huy\",\"email\":\"t_0000013@schoolm\",\"subject\":\"Toan\"},{\"avatar\":\"empty\",\"name\":\"Phung Hung Qua\",\"email\":\"t_0000014@schoolm\",\"subject\":\"Ly\"},{\"avatar\":\"empty\",\"name\":\"Song Phan Lien\",\"email\":\"t_0000015@schoolm\",\"subject\":\"Hoa\"},{\"avatar\":\"empty\",\"name\":\"Quang Tra  Thu\",\"email\":\"t_0000016@schoolm\",\"subject\":\"Sinh\"}]}";

            String data = requestManager.parentGetSchedule("api/post/get_te_list", app.getToken(), app.getId()  );
            return data;


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
//
                System.out.println("result---" + result);
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("listteachers");
                teacherInClasses.clear();

                for(int i = 0 ; i < jsonArray.length(); i++){
                    JSONObject finalObject = jsonArray.getJSONObject(i);
                    TeacherInClass teacher =  new TeacherInClass();
                    teacher.setImage(finalObject.getString("avatar"));
                    teacher.setName(finalObject.getString("name"));
                    teacher.setSubject(finalObject.getString("subject"));
                    teacher.setEmail(finalObject.getString("email"));
//                    student.setSdt(finalObject.getString("sdt"));
                    teacherInClasses.add(teacher);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

            }

            adapter.notifyDataSetChanged();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}



