package com.example.longdinh.tabholder3.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by long dinh on 23/04/2016.
 */
public class Schedule_Parent extends Fragment implements AdapterView.OnItemSelectedListener{
    String WRONGPASS = "Error";
    List<String> MSSVList;
    List<String> NameList;
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_schedule_parent, container, false);

        Spinner spinner = (Spinner) v.findViewById(R.id.listchild);
        //lay du lieu tu intent parent

        // Spinner Drop down elements
        int numchild = 2;
        String[] MSSVList = new String [numchild];
        String[] NameList = new String [numchild];

        for(int i = 0; i < numchild; i++){
            NameList[i] = "child" + i;
            MSSVList[i] = "s_000000" + i;
        }


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),  android.R.layout.simple_spinner_item, NameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        //get get danh sach cac con
        new getSchedule().execute("url cua web");

        return v;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        new getSchedule(); //get schedule for each child
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public class getSchedule extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {
//            HttpURLConnection httpURLConnection = null;
//            BufferedReader bufferedReader = null;
//            String url_ = "http://schoolm.esy.es/app/json/get_token";
//            try {
//                URL url = new URL(url_);
//                httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestMethod("GET");
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
////                return stringBuffer.toString();
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
//                    return "e5";
//                }
//            }
            return "{  \"status\": \"schedule\",  \"date\": \"11-04-2016\",  \"tkb\": {    \"t0\": \"Chào Cờ\",    \"t1\": \"Anh Văn\",    \"t2\": \"Toán\",    \"t3\": \"Toán\",    \"t4\": \"Mỹ thuật\",    \"t5\": \"Anh Văn\",    \"t6\": \"Toán\",    \"t7\": \"Toán\",    \"t8\": \"Vật Lý\",    \"t9\": \"Chào Cờ\",    \"t10\": \"Anh Văn\",    \"t11\": \"Anh Văn\",    \"t12\": \"Toán\",    \"t13\": \"Toán\",    \"t14\": \"Tin Học\",    \"t15\": \"Anh Văn\",    \"t16\": \"Toán\",    \"t17\": \"Toán\",    \"t18\": \"Tin Học\",    \"t19\": \"Vật Lý\",   \"t20\": \"GDCD\",    \"t21\": \"\",    \"t22\": \"\",    \"t23\": \"\",    \"t24\": \"\",    \"t25\": \"\",    \"t26\": \"\",    \"t27\": \"\",    \"t28\": \"\",    \"t29\": \"\",    \"t30\": \"Công nghệ\",   \"t31\": \"Sinh Học\",    \"t32\": \"Sinh Học\",    \"t33\": \"Ngữ Văn\",    \"t34\": \"Ngữ Văn\",    \"t35\": \"Ngữ Văn\",    \"t36\": \"Địa Lý\",    \"t37\": \"\",    \"t38\": \"Hóa Học\",    \"t39\": \"Hóa Học\",    \"t40\": \"Anh Văn\",    \"t41\": \"Hát nhạc\",    \"t42\": \"Thể Dục\",    \"t43\": \"Ngữ Văn\",    \"t44\": \"SHCN\",    \"t45\": \"Thể Dục\",    \"t46\": \"Lịch Sử\",    \"t47\": \"Ngữ Văn\",    \"t48\": \"Công nghệ\",    \"t49\": \"SHCN\"  }}";
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
                            System.out.println("-------------------------------------print---------------------------------------");
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
//                Intent Idashboard = new Intent(getApplicationContext(), DashboardActivity.class);
//                Idashboard.putExtra("token",token);
//                startActivity(Idashboard);
//                finish();
                return;
            }
        }
    }
}
