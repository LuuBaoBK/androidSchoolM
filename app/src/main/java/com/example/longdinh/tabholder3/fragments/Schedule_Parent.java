package com.example.longdinh.tabholder3.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.activities.MyApplication;
import com.example.longdinh.tabholder3.activities.RequestManager;
import com.example.longdinh.tabholder3.adapters.ListChildrenSpinnerAdapter;
import com.example.longdinh.tabholder3.models.ItemSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by long dinh on 23/04/2016.
 */
public class Schedule_Parent extends Fragment implements AdapterView.OnItemSelectedListener{

    List<ItemSpinner> listChildren = new ArrayList<>();
    ListChildrenSpinnerAdapter adapter;
    private MyApplication app;
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.schedule_parent, container, false);
        app = (MyApplication) getActivity().getApplication();
        Spinner spinner = (Spinner) v.findViewById(R.id.listchild);
        spinner.setOnItemSelectedListener(this);
        listChildren = app.getListchildren();
        adapter = new ListChildrenSpinnerAdapter(getContext(), R.layout.items_children_pinner, listChildren);
        spinner.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                setCurrentColorDay(0);
                break;
            case Calendar.TUESDAY:
                setCurrentColorDay(1);
                break;
            case Calendar.WEDNESDAY:
                setCurrentColorDay(2);
                break;
            case Calendar.THURSDAY:
                setCurrentColorDay(3);
                break;
            case Calendar.FRIDAY:
                setCurrentColorDay(4);
                break;
        }
        return v;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("item selected is----- " + position);
        if(position != 0)
            new getSchedule().execute(listChildren.get(position).getMahs());
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public class getSchedule extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {
//            String data ="{\"status\":\"schedule\",\"date\":\"11-04-2016\",\"class\":\"9A2\",\"teacher\":\"Trấn Thu Hà\",\"tkb\":{\"t0\":\"Chào Cờ\",\"t1\":\"Anh Văn\",\"t2\":\"Toán\",\"t3\":\"Toán\",\"t4\":\"Mỹ thuật\",\"t5\":\"Anh Văn\",\"t6\":\"Toán\",\"t7\":\"Toán\",\"t8\":\"Vật Lý\",\"t9\":\"Chào Cờ\",\"t10\":\"Anh Văn\",\"t11\":\"Anh Văn\",\"t12\":\"Toán\",\"t13\":\"Toán\",\"t14\":\"Tin Học\",\"t15\":\"Anh Văn\",\"t16\":\"Toán\",\"t17\":\"Toán\",\"t18\":\"Tin Học\",\"t19\":\"Vật Lý\",\"t20\":\"GDCD\",\"t21\":\"\",\"t22\":\"\",\"t23\":\"\",\"t24\":\"\",\"t25\":\"\",\"t26\":\"\",\"t27\":\"\",\"t28\":\"\",\"t29\":\"\",\"t30\":\"Công nghệ\",\"t31\":\"Sinh Học\",\"t32\":\"Sinh Học\",\"t33\":\"Ngữ Văn\",\"t34\":\"Ngữ Văn\",\"t35\":\"Ngữ Văn\",\"t36\":\"Địa Lý\",\"t37\":\"\",\"t38\":\"Hóa Học\",\"t39\":\"Hóa Học\",\"t40\":\"Anh Văn\",\"t41\":\"Hát nhạc\",\"t42\":\"Thể Dục\",\"t43\":\"Ngữ Văn\",\"t44\":\"SHCN\",\"t45\":\"Thể Dục\",\"t46\":\"Lịch Sử\",\"t47\":\"Ngữ Văn\",\"t48\":\"Công nghệ\",\"t49\":\"SHCN\"}}";
            RequestManager requestManager = new RequestManager();
            String data = requestManager.parentGetSchedule("api/post/parent/get_schedule",app.getToken(),params[0]);

            return data;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject tkb = jsonObject.getJSONObject("tkb");
                String status = jsonObject.getString("status");
                if(!status.equals("schedule")){
                    for(int i = 0; i < 10; i++){
                        for(int j = 0; j < 5; j ++){
                            int resId = v.getResources().getIdentifier("cell" + i + j, "id", v.getContext().getPackageName());
                            TextView cell = (TextView) v.findViewById(resId);
                            cell.setText("");
                        }
                    }
                    return;
                }

                ((TextView) v.findViewById(R.id.tvDateUpdate)).setText("Ngày cập nhật: "+jsonObject.getString("date"));
                ((TextView) v.findViewById(R.id.tvClass)).setText("Lớp: "+jsonObject.getString("class"));
                ((TextView) v.findViewById(R.id.tvTeacher)).setText("Giáo viên: "+jsonObject.getString("teacher"));
                for(int i = 0; i < 10; i++){
                    for(int j = 0; j < 5; j ++){
                        int resId = v.getResources().getIdentifier("cell" + i + j, "id", v.getContext().getPackageName());
                        TextView cell = (TextView) v.findViewById(resId);
                        cell.setText(tkb.getString("t"+(i +j*10)));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return;

        }
    }

    public void setCurrentColorDay(int position){
        for(int i = 0; i < 10; i++){
            int resId = v.getResources().getIdentifier("cell" + i + position, "id", v.getContext().getPackageName());
            TextView cell = (TextView) v.findViewById(resId);
            cell.setBackgroundResource(R.drawable.current_date);
//
        }
    };
}
