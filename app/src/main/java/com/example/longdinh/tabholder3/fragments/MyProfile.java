package com.example.longdinh.tabholder3.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.example.longdinh.tabholder3.activities.Constant;
import com.example.longdinh.tabholder3.activities.MyApplication;
import com.example.longdinh.tabholder3.activities.RequestManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by long dinh on 05/05/2016.
 */
public class MyProfile extends Fragment{
    View v;
    private MyApplication app;
    private String role;
    String profile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApplication) getActivity().getApplication();
        this.loading();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        System.out.println("1 chay profile-----");
        role = app.getRole();
        if(role.equals("0")){
            v = inflater.inflate(R.layout.fragmennt_admininfo, container, false);
        }else if(app.getRole().equals("1")){
            v = inflater.inflate(R.layout.fragmennt_teacherinfo, container, false);
        }else if(app.getRole().equals("3")){
            v = inflater.inflate(R.layout.fragmennt_parentinfo, container, false);
        }else{
            v = inflater.inflate(R.layout.fragmennt_studentinfo, container, false);
        }
        ((TextView)v.findViewById(R.id.tvName)).setText(app.getFullName());
        ((TextView)v.findViewById(R.id.tvEmail)).setText(app.getId() + "@schoolm.com");

        if(profile != null){//trong moi truong hop de co hien thi thong tin local
            showResult(profile);
        }

        if(true){
            new getInfo().execute("");
        }

        return v;
    }

    public void loading(){
        System.out.println("loading profile------");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        profile = sp.getString("PROFILE", null);
        app.setProfile(profile);
        System.out.println("profile---" + profile);
    }



    public class getInfo extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {
            String data;
            String url = "api/user_info";
            RequestManager requestManager = new RequestManager();
            data = requestManager.methodGet(url,app.getToken());
            System.out.println(data + "----get data");
//            if(role.equals("0")){
////                data = "{\"mobilephone\":\"0124929961\",\"address\":\"18 Nguyễn Trãi, P2, Q5, TP.HCM\"}";
//            }else if(app.getRole().equals("1")){
                data = "{\"birthday\":\"08/08/1991\",\"mobilephone\":\"0124929961\",\"homephone\":\"0883837\",\"gender\":\"Nữ\",\"group\":\"Toán\",\"address\":\"18 Nguyễn Bỉnh Khiêm, P2, Q5, TP.HCM\"}";
//            }else if(app.getRole().equals("3")){
//                data = "{\"mobilephone\":\"0124929961\",\"homephone\":\"0883837\",\"job\":\"Công Nhân\",\"address\":\"18 Trường Chinh, P2, Q5, TP.HCM\"}";
//            }else{
//                data = "{\"birthday\":\"08/08/1991\",\"gender\":\"Nữ\",\"parent\":\"Đinh La Thăng\",\"mobilephone\":\"0124929961\",\"homephone\":\"0883837\",\"address\":\"18 Nô Bỉnh Khiêm, P2, Q5, TP.HCM\"}";
//            }
            return data;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result != null) {
                app.setProfile(result);
                profile = result;
                showResult(result);
            }

            return;

        }
    }

    public void showResult(String result ){
        try {
            JSONObject jsonObject = new JSONObject(result);

            CircleImageView ivAvatar = (CircleImageView) v.findViewById(R.id.ivAvatar);
            String avatar = jsonObject.getString("avatar");
            if(!avatar.equals("empty")){
                avatar = Constant.ROOT_API + avatar;
                System.out.println(avatar);
                ImageLoader.getInstance().displayImage(avatar,ivAvatar, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                    }
                });
            }


            if(role.equals("0")){
                ((TextView) v.findViewById(R.id.tvMobilePhone)).setText(jsonObject.getString("mobilephone"));
                ((TextView) v.findViewById(R.id.tvAddress)).setText(jsonObject.getString("address"));
            }else if(app.getRole().equals("1")){
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

    };

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
