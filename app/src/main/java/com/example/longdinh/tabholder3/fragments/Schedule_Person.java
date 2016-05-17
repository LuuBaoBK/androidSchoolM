package com.example.longdinh.tabholder3.fragments;

/**
 * Created by long dinh on 12/04/2016.
 */
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.activities.Constant;
import com.example.longdinh.tabholder3.activities.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class Schedule_Person extends Fragment {
    String WRONGPASS = "Error";
    View v;
    String token;
    MyApplication app;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.schedule_personal, container, false);
        app = (MyApplication) this.getContext().getApplicationContext();
        token = app.getToken();

        // create thoi khoa bieu

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getContext(), "Resum", Toast.LENGTH_LONG).show();
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
                cell.setBackgroundResource(R.drawable.cell_shape1);
        }
    }

    ;


    public class getSchedule extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            String url_ = Constant.ROOT_API + "api/get_schedule";
//            try {
//                URL url = new URL(url_);
//                httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestMethod("GET");
//                httpURLConnection.setRequestProperty(Constant.X_AUTH, token);
//
//
//                httpURLConnection.connect();
//                InputStream inputStream = httpURLConnection.getInputStream();
//                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                String line = null;
//                StringBuffer stringBuffer = new StringBuffer();
//                while ((line = bufferedReader.readLine()) != null) {
//                    stringBuffer.append(line + "\n");
//                }
//
//                return stringBuffer.toString();
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                return "e1";
//            } catch (IOException e) {
//                e.printStackTrace();
//                return "e2";
//            } finally {
//                if (httpURLConnection != null)
//                    httpURLConnection.disconnect();
//                try {
//                    if (bufferedReader != null)
//                        bufferedReader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return "e4";
//                }
//            }
//            IT WORK
            String data ="{\"status\":\"schedule\",\"date\":\"11-04-2016\",\"class\":\"9A2\",\"teacher\":\"Trấn Thu Hà\",\"tkb\":{\"t0\":\"Chào Cờ\",\"t1\":\"Anh Văn\",\"t2\":\"Toán\",\"t3\":\"Toán\",\"t4\":\"Mỹ thuật\",\"t5\":\"Anh Văn\",\"t6\":\"Toán\",\"t7\":\"Toán\",\"t8\":\"Vật Lý\",\"t9\":\"Chào Cờ\",\"t10\":\"Anh Văn\",\"t11\":\"Anh Văn\",\"t12\":\"Toán\",\"t13\":\"Toán\",\"t14\":\"Tin Học\",\"t15\":\"Anh Văn\",\"t16\":\"Toán\",\"t17\":\"Toán\",\"t18\":\"Tin Học\",\"t19\":\"Vật Lý\",\"t20\":\"GDCD\",\"t21\":\"\",\"t22\":\"\",\"t23\":\"\",\"t24\":\"\",\"t25\":\"\",\"t26\":\"\",\"t27\":\"\",\"t28\":\"\",\"t29\":\"\",\"t30\":\"Công nghệ\",\"t31\":\"Sinh Học\",\"t32\":\"Sinh Học\",\"t33\":\"Ngữ Văn\",\"t34\":\"Ngữ Văn\",\"t35\":\"Ngữ Văn\",\"t36\":\"Địa Lý\",\"t37\":\"\",\"t38\":\"Hóa Học\",\"t39\":\"Hóa Học\",\"t40\":\"Anh Văn\",\"t41\":\"Hát nhạc\",\"t42\":\"Thể Dục\",\"t43\":\"Ngữ Văn\",\"t44\":\"SHCN\",\"t45\":\"Thể Dục\",\"t46\":\"Lịch Sử\",\"t47\":\"Ngữ Văn\",\"t48\":\"Công nghệ\",\"t49\":\"SHCN\"}}";
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(!result.equals( WRONGPASS)) {
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
            else{
                return;
            }
        }
    }
}

