package com.example.longdinh.tabholder3.fragments;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.activities.MyApplication;
import com.example.longdinh.tabholder3.activities.RequestManager;
import com.example.longdinh.tabholder3.adapters.ListChildrenSpinnerAdapter;
import com.example.longdinh.tabholder3.adapters.SpinnerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by long dinh on 23/04/2016.
 */
public class Transcript_Show_Student extends Fragment {

    List<String> listMonth = new ArrayList<>();
    ListChildrenSpinnerAdapter childrenAdapter;
    SpinnerAdapter monthAdapter;
    private MyApplication app;
    String mahs = null;
    View v;
    Button btnSaveTranscript;

    LinearLayout listScore;
    String bangdiem = "empty";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApplication) getActivity().getApplication();
        this.loading();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.show_transcript_student, container, false);

        mahs = app.getId();

        listScore = (LinearLayout) v.findViewById(R.id.listScore);

        Spinner monthSpinner = (Spinner) v.findViewById(R.id.spMonth);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("month selected ----- " + position);
                if (position != 0){
                    new getStranscript().execute(position + "");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        listMonth.clear();
        listMonth.add("Choose a month");
        for(int i = 0 ; i < 10; i++){
            listMonth.add("Tháng " + (1+(7+i)%12));
        }
        listMonth.add("Average of all month");


        btnSaveTranscript = (Button) v.findViewById(R.id.btnSaveTranscript);
        btnSaveTranscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnSaveTranscript.getText().toString().equals("SAVE")){
                    btnSaveTranscript.setText("DISCARD");
                    app.setBangdiem(bangdiem);
                }else{
                    btnSaveTranscript.setText("SAVE");
                    app.setBangdiem("empty");
                }
            }
        });


        monthAdapter = new SpinnerAdapter(getContext(), R.layout.items_children_pinner, listMonth);
        monthSpinner.setAdapter(monthAdapter);
        monthAdapter.notifyDataSetChanged();


        if(bangdiem != null){//trong moi truong hop de co hien thi thong tin local
            if(!bangdiem.equals("empty")){
                try {
                    JSONObject jsonObject = new JSONObject(bangdiem);
                    JSONArray returnListScore = jsonObject.getJSONArray("tb");
                    JSONArray listSubject = jsonObject.getJSONArray("subject_list");
                    if(listScore.getChildCount() > 0)
                        listScore.removeAllViews();
                    for(int i = 0; i < listSubject.length(); i++){
                        addnewScore(listSubject.getString(i), returnListScore.getString(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return v;
    }

    public void loading(){
        System.out.println("loading bang diem------");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        bangdiem = sp.getString("BANGDIEM", null);
        if(bangdiem != null)
            app.setBangdiem(bangdiem);
        System.out.println("bangdiem---" + bangdiem);
    }


    public class getStranscript extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {
            ///get dulieu tu 2 spinner
//            String data ="{\"Toan\":\"1.2\",\"NguVan\":\"2.2\",\"HoaHoc\":\"3.2\",\"AnhVan\":\"4.2\",\"MyThuat\":\"5.2\",\"TinHoc\":\"6.2\",\"VatLy\":\"7.2\",\"GDCD\":\"8.2\",\"CongNghe\":\"9.2\",\"SinhHoc\":\"1.3\",\"DiaLy\":\"1.3\",\"HatNhac\":\"1.24\",\"TheDuc\":\"1.6\",\"LichSu\":\"1.3\"}";
            RequestManager requestManager = new RequestManager();
            String data = requestManager.getTranscript("api/post/get_transcript", app.getToken(), app.getId(), params[0]);
            return data;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println(result +"result bang diem----");
            bangdiem = result;
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray returnListScore = jsonObject.getJSONArray("tb");
                JSONArray listSubject = jsonObject.getJSONArray("subject_list");
                if(listScore.getChildCount() > 0)
                    listScore.removeAllViews();
                for(int i = 0; i < listSubject.length(); i++){
                    addnewScore(listSubject.getString(i), returnListScore.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return;

        }
    }


    public void addnewScore(String subject, String score){
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
                );

        LinearLayout.LayoutParams vparams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        vparams.weight= 1 ;
        LinearLayout linear = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_transcirpt, null);
        ((TextView)linear.findViewById(R.id.tvMon)).setText(subject);
        ((TextView)linear.findViewById(R.id.tvScore)).setText(score);
        listScore.addView(linear);
    }
}
