package com.example.longdinh.tabholder3.fragments;

/**
 * Created by long dinh on 12/04/2016.
 */
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.activities.MyApplication;
import com.example.longdinh.tabholder3.activities.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Schedule_Person extends Fragment {
    String WRONGPASS = "Error";
    View v;
    String token;
    MyApplication app;
    String schedule="empty";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApplication) this.getContext().getApplicationContext();
        this.loading();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.schedule_personal, container, false);

        token = app.getToken();
        if(schedule != null){//trong moi truong hop de co hien thi thong tin local
            if(!schedule.equals("empty")){
                showSchedule(schedule);
            }
        }
        return v;
    }

    public void loading(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        schedule = sp.getString("SCHEDULE", null);
        if(schedule != null)
            app.setSchedule(schedule);
    }

    @Override
    public void onResume() {
        super.onResume();
        new getSchedule().execute("url cua web");
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

    }

    public void setCurrentColorDay(int position){
        for(int i = 0; i < 10; i++){
                int resId = v.getResources().getIdentifier("cell" + i + position, "id", v.getContext().getPackageName());
                TextView cell = (TextView) v.findViewById(resId);
                cell.setBackgroundResource(R.drawable.current_date);
//            cell.setBackgroundColor(0xffb93221);
        }
    };


    public class getSchedule extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {

            String data;
            String url = "api/get_schedule";
            RequestManager requestManager = new RequestManager();

            data = requestManager.methodGet(url,app.getToken());
            schedule = data;
            app.setSchedule(data);

//            String data ="{\"status\":\"schedule\",\"date\":\"11-04-2016\",\"class\":\"9A2\",\"teacher\":\"Trấn Thu Hà\",\"tkb\":{\"t0\":\"Chào Cờ\",\"t1\":\"Anh Văn\",\"t2\":\"Toán\",\"t3\":\"Toán\",\"t4\":\"Mỹ thuật\",\"t5\":\"Anh Văn\",\"t6\":\"Toán\",\"t7\":\"Toán\",\"t8\":\"Vật Lý\",\"t9\":\"Chào Cờ\",\"t10\":\"Anh Văn\",\"t11\":\"Anh Văn\",\"t12\":\"Toán\",\"t13\":\"Toán\",\"t14\":\"Tin Học\",\"t15\":\"Anh Văn\",\"t16\":\"Toán\",\"t17\":\"Toán\",\"t18\":\"Tin Học\",\"t19\":\"Vật Lý\",\"t20\":\"GDCD\",\"t21\":\"\",\"t22\":\"\",\"t23\":\"\",\"t24\":\"\",\"t25\":\"\",\"t26\":\"\",\"t27\":\"\",\"t28\":\"\",\"t29\":\"\",\"t30\":\"Công nghệ\",\"t31\":\"Sinh Học\",\"t32\":\"Sinh Học\",\"t33\":\"Ngữ Văn\",\"t34\":\"Ngữ Văn\",\"t35\":\"Ngữ Văn\",\"t36\":\"Địa Lý\",\"t37\":\"\",\"t38\":\"Hóa Học\",\"t39\":\"Hóa Học\",\"t40\":\"Anh Văn\",\"t41\":\"Hát nhạc\",\"t42\":\"Thể Dục\",\"t43\":\"Ngữ Văn\",\"t44\":\"SHCN\",\"t45\":\"Thể Dục\",\"t46\":\"Lịch Sử\",\"t47\":\"Ngữ Văn\",\"t48\":\"Công nghệ\",\"t49\":\"SHCN\"}}";
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(!result.equals( WRONGPASS)) {
               showSchedule(result);
            }
            else{
                return;
            }
        }
    }


    public void showSchedule(String result){
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject tkb = jsonObject.getJSONObject("tkb");
            String status = jsonObject.getString("status");

            if(!status.equals("schedule"))
                return;
            ((TextView) v.findViewById(R.id.tvDateUpdate)).setText("Ngày cập nhật: "+jsonObject.getString("date"));
            for(int i = 0; i < 10; i++){
                for(int j = 0; j < 5; j ++){
                    int resId = v.getResources().getIdentifier("cell" + i + j, "id", v.getContext().getPackageName());
                    System.out.println(resId);
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

