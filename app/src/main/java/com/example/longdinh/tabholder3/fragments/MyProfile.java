package com.example.longdinh.tabholder3.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.activities.MyApplication;
import com.example.longdinh.tabholder3.models.NoticeBoardItem;

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
import java.util.List;

/**
 * Created by long dinh on 05/05/2016.
 */
public class MyProfile extends Fragment{
    View v;
    private MyApplication app;
    private String role;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        app = (MyApplication) getActivity().getApplication();
        role = app.getRole();
        if(role.equals("1")){
            v = inflater.inflate(R.layout.fragmennt_admininfo, container, false);
        }else if(app.getRole().equals("2")){
            v = inflater.inflate(R.layout.fragmennt_teacherinfo, container, false);
        }else if(app.getRole().equals("3")){
            v = inflater.inflate(R.layout.fragmennt_parentinfo, container, false);
        }else{
            v = inflater.inflate(R.layout.fragmennt_studentinfo, container, false);
        }
        new getInfo();
        return v;
    }

    public class getInfo extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {

            String data;
            if(role.equals("1")){
                data = "{\"mobilephone\":\"0124929961\",\"address\":\"18 Nguyễn Trãi, P2, Q5, TP.HCM\"}";
            }else if(app.getRole().equals("2")){
                data = "{\"birthday\":\"08/08/1991\",\"mobilephone\":\"0124929961\",\"homephone\":\"0883837\",\"gender\":\"Nữ\",\"group\":\"Toán\",\"address\":\"18 Nguyễn Bỉnh Khiêm, P2, Q5, TP.HCM\"}";
            }else if(app.getRole().equals("3")){
                data = "{\"mobilephone\":\"0124929961\",\"homephone\":\"0883837\",\"job\":\"Công Nhân\",\"address\":\"18 Trường Chinh, P2, Q5, TP.HCM\"}";
            }else{
                data = "{\"birthday\":\"08/08/1991\",\"gender\":\"Nữ\",\"parent\":\"Đinh La Thăng\",\"mobilephone\":\"0124929961\",\"homephone\":\"0883837\",\"address\":\"18 Nô Bỉnh Khiêm, P2, Q5, TP.HCM\"}";
            }
            return data;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);

                if(role.equals("1")){
                    ((TextView) v.findViewById(R.id.tvMobilePhone)).setText(jsonObject.getString("mobilephone"));
                    ((TextView) v.findViewById(R.id.tvAddress)).setText(jsonObject.getString("address"));
                }else if(app.getRole().equals("2")){
                    ((TextView) v.findViewById(R.id.tvMobilePhone)).setText(jsonObject.getString("mobilephone"));
                    ((TextView) v.findViewById(R.id.tvHomePhone)).setText(jsonObject.getString("homephone"));
                    ((TextView) v.findViewById(R.id.tvGender)).setText(jsonObject.getString("gender"));
                    ((TextView) v.findViewById(R.id.tvGroup)).setText(jsonObject.getString("group"));
                    ((TextView) v.findViewById(R.id.tvAddress)).setText(jsonObject.getString("address"));

                }else if(app.getRole().equals("3")){
                    ((TextView) v.findViewById(R.id.tvMobilePhone)).setText(jsonObject.getString("mobilephone"));
                    ((TextView) v.findViewById(R.id.tvHomePhone)).setText(jsonObject.getString("homephone"));
                    ((TextView) v.findViewById(R.id.tvJob)).setText(jsonObject.getString("job"));
                    ((TextView) v.findViewById(R.id.tvAddress)).setText(jsonObject.getString("address"));

                }else{
                    ((TextView) v.findViewById(R.id.tvDateOfBirth)).setText(jsonObject.getString("birthday"));
                    ((TextView) v.findViewById(R.id.tvGender)).setText(jsonObject.getString("gender"));
                    ((TextView) v.findViewById(R.id.tvParent)).setText(jsonObject.getString("parent"));
                    ((TextView) v.findViewById(R.id.tvMobilePhone)).setText(jsonObject.getString("mobilephone"));
                    ((TextView) v.findViewById(R.id.tvHomePhone)).setText(jsonObject.getString("homephone"));
                    ((TextView) v.findViewById(R.id.tvAddress)).setText(jsonObject.getString("address"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return;

        }
    }
}
