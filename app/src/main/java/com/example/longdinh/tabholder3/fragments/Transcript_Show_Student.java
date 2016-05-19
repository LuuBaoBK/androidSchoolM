package com.example.longdinh.tabholder3.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    TextView tvToan;
    TextView tvNguVan;
    TextView tvHoaHoc;
    TextView tvAnhVan;
    TextView tvMyThuat;
    TextView tvTinHoc;
    TextView tvVatLy;
    TextView tvGDCD;
    TextView tvCongNghe;
    TextView tvSinhHoc;
    TextView tvDiaLy;
    TextView tvHatNhac;
    TextView tvTheDuc;
    TextView tvLichSu;

    LinearLayout listScore;



    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.show_transcript_student, container, false);

        app = (MyApplication) getActivity().getApplication();
        mahs = app.getId();

        listScore = (LinearLayout) v.findViewById(R.id.listScore);
//        tvToan = (TextView) v.findViewById(R.id.tvToan);
//                tvNguVan = (TextView) v.findViewById(R.id.tvNguVan);
//        tvHoaHoc = (TextView) v.findViewById(R.id.tvHoaHoc);
//                tvAnhVan = (TextView) v.findViewById(R.id.tvAnhVan);
//        tvMyThuat = (TextView) v.findViewById(R.id.tvMyThuat);
//                tvTinHoc = (TextView) v.findViewById(R.id.tvTinHoc);
//        tvVatLy = (TextView) v.findViewById(R.id.tvVatLy);
//                tvGDCD = (TextView) v.findViewById(R.id.tvGDCD);
//        tvCongNghe = (TextView) v.findViewById(R.id.tvCongNghe);
//                tvSinhHoc = (TextView) v.findViewById(R.id.tvSinhHoc);
//        tvDiaLy = (TextView) v.findViewById(R.id.tvDiaLy);
//                tvHatNhac = (TextView) v.findViewById(R.id.tvHatNhac);
//        tvTheDuc = (TextView) v.findViewById(R.id.tvTheDuc);
//                tvLichSu = (TextView) v.findViewById(R.id.tvLichSu);

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



        monthAdapter = new SpinnerAdapter(getContext(), R.layout.items_children_pinner, listMonth);
        monthSpinner.setAdapter(monthAdapter);
        monthAdapter.notifyDataSetChanged();

        return v;
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
            try {
                JSONObject jsonObject = new JSONObject(result);
//                tvToan.setText(jsonObject.getString("Toan"));
//                        tvNguVan.setText(jsonObject.getString("NguVan"));
//                tvHoaHoc.setText(jsonObject.getString("HoaHoc"));
//                        tvAnhVan.setText(jsonObject.getString("AnhVan"));
//                tvMyThuat.setText(jsonObject.getString("MyThuat"));
//                        tvTinHoc.setText(jsonObject.getString("TinHoc"));
//                tvVatLy.setText(jsonObject.getString("VatLy"));
//                        tvGDCD.setText(jsonObject.getString("GDCD"));
//                tvCongNghe.setText(jsonObject.getString("CongNghe"));
//                        tvSinhHoc.setText(jsonObject.getString("SinhHoc"));
//                tvDiaLy.setText(jsonObject.getString("DiaLy"));
//                        tvHatNhac.setText(jsonObject.getString("HatNhac"));
//                tvTheDuc.setText(jsonObject.getString("TheDuc"));
//                        tvLichSu.setText(jsonObject.getString("LichSu"));

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

//        TextView SubjectTextView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_transcirpt_subject, null);
////        SubjectTextView.setLayoutParams(vparams);
//        SubjectTextView.setText(subject);
//
//        TextView ScoreTextView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_transcirpt_score, null);
////        ScoreTextView.setLayoutParams(vparams);
//        ScoreTextView.setText(score);

        LinearLayout linear = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_transcirpt, null);
        ((TextView)linear.findViewById(R.id.tvMon)).setText(subject);
        ((TextView)linear.findViewById(R.id.tvScore)).setText(score);
        listScore.addView(linear);
    }
}
