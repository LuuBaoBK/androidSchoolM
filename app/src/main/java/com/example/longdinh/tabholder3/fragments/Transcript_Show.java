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
import com.example.longdinh.tabholder3.adapters.ListChildrenSpinnerAdapter;
import com.example.longdinh.tabholder3.adapters.SpinnerAdapter;
import com.example.longdinh.tabholder3.models.ItemSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by long dinh on 23/04/2016.
 */
public class Transcript_Show extends Fragment {

    List<ItemSpinner> listChildren;
    List<String> listMonth = new ArrayList<>();
    ListChildrenSpinnerAdapter childrenAdapter;
    SpinnerAdapter monthAdapter;
    private MyApplication app;
    String mahs = null;
    View v;
    LinearLayout listScore;




    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.show_transcript, container, false);

        // xu li logic cho viec sem bang diem giua cha va con
        //1neu nhu khong co ham khoi tao thi do la cha
        //2con neu co ham khoi tao va co du lieu gui qua thi do la con
        app = (MyApplication) getActivity().getApplication();
        final Spinner childrenSpinner = (Spinner) v.findViewById(R.id.splistChildren);
        listScore = (LinearLayout) v.findViewById(R.id.listScore);



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
            String data ="{\"tb\":[\"0.00\",\"0.00\",\"0.00\",\"0.00\",\"0.00\",\"0.00\",\"0.00\",\"0.00\",\"0.00\",\"0.00\",\"0.00\",\"0.00\",\"0.00\",\"0.00\"],\"subject_list\":[\"To\\u00e1n\",\"Ng\\u1eef V\\u0103n\",\"V\\u1eadt L\\u00fd\",\"H\\u00f3a H\\u1ecdc\",\"Sinh H\\u1ecdc\",\"L\\u1ecbch S\\u1eed\",\"\\u0110\\u1ecba L\\u00fd\",\"\\u00c2m nh\\u1ea1c\",\"GDCD\",\"Th\\u1ec3 D\\u1ee5c\",\"Tin H\\u1ecdc\",\"Anh V\\u0103n\",\"M\\u1ef9 thu\\u1eadt\",\"C\\u00f4ng ngh\\u1ec7\"]}";
            return data;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
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
