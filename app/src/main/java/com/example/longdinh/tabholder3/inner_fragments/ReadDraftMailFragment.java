package com.example.longdinh.tabholder3.inner_fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.activities.MailContent;
import com.example.longdinh.tabholder3.activities.MainActivity;
import com.example.longdinh.tabholder3.activities.MyApplication;
import com.example.longdinh.tabholder3.models.EmailItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.entity.ContentType;

/**
 * Created by long dinh on 15/05/2016.
 */
public class ReadDraftMailFragment extends Fragment{

    TextView tvSubject;
    TextView tvStand;
    TextView tvSender;
    TextView tvNguoiNhan;
    TextView tvDate;
    TextView tvContent;
    LinearLayout btnEdit;
    String idEmail;
    Boolean isTrashMail = true;
    MyApplication app;



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_delete, menu);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.maildraft_read, container, false);
        setHasOptionsMenu(true);
//        getActivity().getActionBar().setDisplayShowTitleEnabled(false);
//        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActivity().getActionBar().setHomeButtonEnabled(true);//co the khong can thiet


        tvSubject = (TextView) v.findViewById(R.id.tvSubject);
         tvStand= (TextView) v.findViewById(R.id.tvStand);
         tvSender= (TextView) v.findViewById(R.id.tvSender);
         tvNguoiNhan= (TextView) v.findViewById(R.id.tvNguoiNhan);
         tvDate= (TextView) v.findViewById(R.id.tvDate);
         tvContent= (TextView) v.findViewById(R.id.tvContent);
         btnEdit = (LinearLayout) v.findViewById(R.id.btnEdit);

        app = (MyApplication) getActivity().getApplication();
//        ((MainActivity)getActivity()).drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        mActionBarDrawerToggle.setDrawerIndicatorEnabled(false)
//
//

//        Toolbar toolbar = (Toolbar)
//        setSupportActionBar(toolbar);
//
//        ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
//        mActionBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
//        mActionBar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //What to do on back clicked
//            }
//        });



        Bundle bundle = this.getArguments();
        idEmail = bundle.getString("idEmail", null);
        System.out.println("nhan dc idemail " + idEmail);

        //trong truong hop nay se xu li-> neu nhu mail co luu thi se khong can phai len gui request len server
        new getMailDetail().execute(idEmail);
        //thuc chat ko can gui request len server vi tat cac mail draft deu o luu


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), MailContent.class);
                intent.putExtra("type", "EDIT");
                intent.putExtra("idMail", idEmail);
                intent.putExtra("content", tvContent.getText().toString());
                intent.putExtra("subject", tvSubject.getText());
                intent.putExtra("sender", tvNguoiNhan.getText());
                intent.putExtra("isTrashMail", isTrashMail);
                startActivityForResult(intent, 124);
                System.out.println("da kich hoat su kien edit"  + tvContent.getText());
            }
        });
    return v;
    }


    public class getMailDetail extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            System.out.println("---------------da toi day 0");
            String retur = new String("{ \"id\": 1,\"content\": \"Noi dung khong quan trong chay dung la dc\",\"title\": \"Mail sent to server\",\"date_time\": \"Apr 29\",\"author\": \"t0001@schoolm.com\"}");
            return retur;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            List<EmailItem> draftMailList = app.getData_DraftMailList();
            for(int i = 0; i < draftMailList.size(); i++){
                EmailItem email = draftMailList.get(i);
                if(idEmail.equals(draftMailList.get(i).getId()+ "")){
                    tvStand.setText(Character.toString(Character.toUpperCase(email.getSubject().charAt(0))));
                tvSubject.setText(email.getSubject());
                tvSender.setText(app.getId()+"@schoolm.com");
                tvNguoiNhan.setText(email.getSender());
                tvDate.setText(email.getDate());
                tvContent.setText(email.getPreview());
                    return;
                }
            }


//            try {
//                JSONObject email = new JSONObject(result);
//                tvStand.setText(Character.toString(Character.toUpperCase(email.getString("author").charAt(0))));
//                tvSubject.setText(email.getString("title"));
//                tvSender.setText(email.getString("author"));
//                tvNguoiNhan.setText("To me");
//                tvDate.setText(email.getString("date_time"));
//                tvContent.setText(email.getString("content"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
    }
}
