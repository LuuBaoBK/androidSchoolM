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
import android.widget.Spinner;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.activities.MyApplication;
import com.example.longdinh.tabholder3.activities.RequestManager;
import com.example.longdinh.tabholder3.activities.ShowDetailStudent;
import com.example.longdinh.tabholder3.adapters.ListChildrenSpinnerAdapter;
import com.example.longdinh.tabholder3.adapters.SpinnerAdapter;
import com.example.longdinh.tabholder3.adapters.StudentClassAdapter;
import com.example.longdinh.tabholder3.models.StudentInClass;
import com.example.longdinh.tabholder3.models.ItemSpinner;
import com.example.longdinh.tabholder3.models.TeacherInClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyClass extends Fragment {
    private ListView listview;
    List<StudentInClass> studentClassList = new ArrayList<>();
    private MyApplication app;
    StudentClassAdapter   adapter;
    ListChildrenSpinnerAdapter adapterSpinner;
    Spinner spListClass;
    List<ItemSpinner> listClasses = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.list_student_class, container, false);
        app = (MyApplication) getActivity().getApplication();


        spListClass = (Spinner) v.findViewById(R.id.spListClass);
        new getListClass().execute();
        spListClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    new JsonTask().execute(listClasses.get(position).getMahs());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapterSpinner = new ListChildrenSpinnerAdapter(getContext(), R.layout.items_children_pinner, listClasses);
        spListClass.setAdapter(adapterSpinner);


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
    }



    public class getListClass extends AsyncTask<String, String , String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("on pre execute---");
        }

        @Override
        protected String doInBackground(String... params) {
//            RequestManager requestManager = new RequestManager();
//            String data = "{listClasses:[{\"classid\":\"15_8_A_1\",\"classname\":\"8A1\"},{\"classid\":\"15_8_A_2\",\"classname\":\"8A2\"},{\"classid\":\"15_8_A_3\",\"classname\":\"8A3\"},{\"classid\":\"15_8_A_4\",\"classname\":\"8A4\"}]}";
            RequestManager requestManager = new RequestManager();
//            String data = requestManager.getInboxMail("api/get_list_classes", app.getToken(), 1);
            String data = requestManager.methodGet("api/get_list_classes", app.getToken());
            System.out.println("get list classes" + data);
            return data;


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
//                JSONObject jsonObject = new JSONObject(result);
//                JSONArray jsonArray = jsonObject.getJSONArray("listClasses");
                JSONArray jsonArray = new JSONArray(result);

                System.out.println("create list class");
                ItemSpinner itemSpinner =  new ItemSpinner();
                itemSpinner.setMahs("no id");
                itemSpinner.setTen("Choose a class");
                listClasses.add(itemSpinner);

                for(int i = 0 ; i < jsonArray.length(); i++){
                    JSONObject finalObject = jsonArray.getJSONObject(i);
                    itemSpinner =  new ItemSpinner();
                    itemSpinner.setMahs(finalObject.getString("class_id"));
                    itemSpinner.setTen(finalObject.getString("class_name"));
                    listClasses.add(itemSpinner);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

            }

            adapterSpinner.notifyDataSetChanged();
        }
    }


    public class JsonTask extends AsyncTask<String, String , String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            RequestManager requestManager = new RequestManager();

//            String data = requestManager.getInboxMail("api/post/teacher/get_stulist", app.getToken(), 1);
//            String data = "{\"liststudents\":[{\"avatar\":\"empty\",\"name\":\"Phan quoc Huy\",\"ma\":\"t_0000013@schoolm\",\"subject\":\"Toan\"},{\"avatar\":\"empty\",\"name\":\"Phung Hung Qua\",\"ma\":\"t_0000014@schoolm\",\"subject\":\"Ly\"},{\"avatar\":\"empty\",\"name\":\"Song Phan Lien\",\"ma\":\"t_0000015@schoolm\",\"subject\":\"Hoa\"},{\"avatar\":\"empty\",\"name\":\"Quang Tra  Thu\",\"ma\":\"t_0000016@schoolm\",\"subject\":\"Sinh\"}]}";

            String data = requestManager.parentGetSchedule("api/post/teacher/get_stulist", app.getToken(), params[0]);
            System.out.println("checkthis" + data);
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
                    StudentInClass student =  new StudentInClass();
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

