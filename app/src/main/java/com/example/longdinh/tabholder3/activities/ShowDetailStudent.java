package com.example.longdinh.tabholder3.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by long dinh on 14/05/2016.
 */
public class ShowDetailStudent extends Activity {
    TextView tvName;
    TextView tvEmail;
    TextView tvDateOfBirth;
    TextView tvGender;
    TextView tvParent;
    TextView tvMobilePhone;
    TextView tvAddress;
    CircleImageView ivAvatar;
    MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmennt_studentinfo);
        app = (MyApplication) getApplication();

        ivAvatar = (CircleImageView) findViewById(R.id.ivAvatar);
        tvName = (TextView) findViewById(R.id.tvName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvDateOfBirth = (TextView) findViewById(R.id.tvDateOfBirth);
        tvGender = (TextView) findViewById(R.id.tvGender);
        tvParent = (TextView) findViewById(R.id.tvParent);
        tvMobilePhone = (TextView) findViewById(R.id.tvMobilePhone);
        tvAddress = (TextView) findViewById(R.id.tvAddress);


        Intent getData = getIntent();
        String ma = getData.getStringExtra("mahs");
        System.out.println("get Ma"  + ma);
        new showStudentDetail().execute(ma);
    }



    public class showStudentDetail extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {
            String mahs = params[0];
            RequestManager requestManager = new RequestManager();
            String data = requestManager.getData("api/post/teacher/get_stu_detail", app.getToken(), "id="+mahs);
//            String retur = "{\"avatar\":\"http://jsonparsing.parseapp.com/jsonData/images/avengers.jpg\",\"email\":\"s_0000001@schoolm.com\",\"name\":\"Trần Quách Tĩnh\",\"birthday\":\"01/09/1997\",\"gender\":\"Nam\",\"parent\":\"Trần Thiên Hoàng\",\"phone\":\"099292997\",\"address\":\"Trung Thành Tây, Mỹ Quang, Q.3\"}";
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject student = new JSONObject(result);

                ImageLoader.getInstance().displayImage(Constant.ROOT_API+student.getString("avatar"), ivAvatar, new ImageLoadingListener() {
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
                }); // Default options will be used

                tvName.setText(student.getString("name"));
                tvEmail.setText(student.getString("email"));
                tvDateOfBirth.setText(student.getString("birthday"));
                tvGender.setText(student.getString("gender"));
                tvParent.setText(student.getString("parent"));
                tvMobilePhone.setText(student.getString("phone"));
                tvAddress.setText( student.getString("address"));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
