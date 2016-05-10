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
import android.widget.Toast;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.activities.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Schedule_Person extends Fragment {
    String WRONGPASS = "Error";
    View v;
    String token;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_about, container, false);
        SharedPreferences settings = getActivity().getSharedPreferences("toGetData", 0);
        token = settings.getString("token", null);



        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getContext(), "Resum", Toast.LENGTH_LONG).show();
        new getSchedule().execute("url cua web");
    }

    public class getSchedule extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            String url_ = Constant.ROOT_API + "api/get_schedule";
            try {
                URL url = new URL(url_);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty(Constant.X_AUTH, token);


                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                StringBuffer stringBuffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }

                return stringBuffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "e1";
            } catch (IOException e) {
                e.printStackTrace();
                return "e2";
            } finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
                try {
                    if (bufferedReader != null)
                        bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "e4";
                }
            }
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
                return;
            }
        }
    }
}

