package com.example.longdinh.tabholder3.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.activities.MyApplication;
import com.example.longdinh.tabholder3.adapters.ListChildrenSpinnerAdapter;
import com.example.longdinh.tabholder3.adapters.SpinnerAdapter;
import com.example.longdinh.tabholder3.models.StudentItemSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by long dinh on 23/04/2016.
 */
public class Transcript_Show extends Fragment {

    List<StudentItemSpinner> listChildren;
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



    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.show_transcript, container, false);

        // xu li logic cho viec sem bang diem giua cha va con
        //1neu nhu khong co ham khoi tao thi do la cha
        //2con neu co ham khoi tao va co du lieu gui qua thi do la con
        app = (MyApplication) getActivity().getApplication();
        final Spinner childrenSpinner = (Spinner) v.findViewById(R.id.splistChildren);

        tvToan = (TextView) v.findViewById(R.id.tvToan);
                tvNguVan = (TextView) v.findViewById(R.id.tvNguVan);
        tvHoaHoc = (TextView) v.findViewById(R.id.tvHoaHoc);
                tvAnhVan = (TextView) v.findViewById(R.id.tvAnhVan);
        tvMyThuat = (TextView) v.findViewById(R.id.tvMyThuat);
                tvTinHoc = (TextView) v.findViewById(R.id.tvTinHoc);
        tvVatLy = (TextView) v.findViewById(R.id.tvVatLy);
                tvGDCD = (TextView) v.findViewById(R.id.tvGDCD);
        tvCongNghe = (TextView) v.findViewById(R.id.tvCongNghe);
                tvSinhHoc = (TextView) v.findViewById(R.id.tvSinhHoc);
        tvDiaLy = (TextView) v.findViewById(R.id.tvDiaLy);
                tvHatNhac = (TextView) v.findViewById(R.id.tvHatNhac);
        tvTheDuc = (TextView) v.findViewById(R.id.tvTheDuc);
                tvLichSu = (TextView) v.findViewById(R.id.tvLichSu);

        //set up for spinner get data from children

        listChildren = app.getListchildren();
        System.out.println(listChildren.get(1).getTen() + "------ten transcript");
        childrenAdapter = new ListChildrenSpinnerAdapter(getContext(), R.layout.items_children_pinner, listChildren);
        childrenSpinner.setAdapter(childrenAdapter);



        Spinner monthSpinner = (Spinner) v.findViewById(R.id.spMonth);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("month selected ----- " + position);
                if (position != 0 && childrenSpinner.getSelectedItemPosition() != 0){
                    new getStranscript().execute(listChildren.get(childrenSpinner.getSelectedItemPosition()).getMahs());
                    System.out.println("vi tri month" + position + ", child " + childrenSpinner.getSelectedItemPosition());
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        listMonth.clear();
        listMonth.add("Choose a month");
        for(int i = 1 ; i <= 8; i++){
            listMonth.add("Tháng " + (i+8)%12);
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
            String data ="{\"Toan\":\"1.2\",\"NguVan\":\"2.2\",\"HoaHoc\":\"3.2\",\"AnhVan\":\"4.2\",\"MyThuat\":\"5.2\",\"TinHoc\":\"6.2\",\"VatLy\":\"7.2\",\"GDCD\":\"8.2\",\"CongNghe\":\"9.2\",\"SinhHoc\":\"1.3\",\"DiaLy\":\"1.3\",\"HatNhac\":\"1.24\",\"TheDuc\":\"1.6\",\"LichSu\":\"1.3\"}";
            return data;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                tvToan.setText(jsonObject.getString("Toan"));
                        tvNguVan.setText(jsonObject.getString("NguVan"));
                tvHoaHoc.setText(jsonObject.getString("HoaHoc"));
                        tvAnhVan.setText(jsonObject.getString("AnhVan"));
                tvMyThuat.setText(jsonObject.getString("MyThuat"));
                        tvTinHoc.setText(jsonObject.getString("TinHoc"));
                tvVatLy.setText(jsonObject.getString("VatLy"));
                        tvGDCD.setText(jsonObject.getString("GDCD"));
                tvCongNghe.setText(jsonObject.getString("CongNghe"));
                        tvSinhHoc.setText(jsonObject.getString("SinhHoc"));
                tvDiaLy.setText(jsonObject.getString("DiaLy"));
                        tvHatNhac.setText(jsonObject.getString("HatNhac"));
                tvTheDuc.setText(jsonObject.getString("TheDuc"));
                        tvLichSu.setText(jsonObject.getString("LichSu"));

                System.out.println("da update diem--su--" + jsonObject.getString("LichSu"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return;

        }
    }
}
