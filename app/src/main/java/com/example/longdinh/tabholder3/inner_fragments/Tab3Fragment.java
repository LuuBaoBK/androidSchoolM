package com.example.longdinh.tabholder3.inner_fragments;

/**
 * Created by long dinh on 12/04/2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.activities.MailContent;
import com.example.longdinh.tabholder3.activities.MyApplication;
import com.example.longdinh.tabholder3.adapters.EmailItemAdapter;
import com.example.longdinh.tabholder3.models.EmailItem;
import com.software.shell.fab.ActionButton;

import java.util.ArrayList;
import java.util.List;

public class Tab3Fragment extends Fragment {
    List<EmailItem> emailItemList;
    ListView lvEmailItem;
    EmailItemAdapter adapter;
    final int EMAIL_EDIT_DRAFT = 100;
    final int  EMAIL_COMPOSE_NEW = 101;
    final int EMAIL_VIEW_OLD = 102;
    String vitri = new String(-1 + "");
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab1fragment, container, false);
        lvEmailItem = (ListView) v.findViewById(R.id.lvEmailItem);

        ActionButton actionButton = (ActionButton) v.findViewById(R.id.action_button);
        actionButton.setImageResource(R.drawable.fab_plus_icon);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MailContent.class);
                startActivityForResult(intent, EMAIL_COMPOSE_NEW);
            }
        });

        createData();
        adapter = new EmailItemAdapter(getContext(), R.layout.item_email, emailItemList);

        lvEmailItem.setAdapter(adapter);
        lvEmailItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), MailContent.class);
                vitri = position + "";
                intent.putExtra("type", "EDIT");
                intent.putExtra("date", emailItemList.get(position).getDate());
                intent.putExtra("preview", emailItemList.get(position).getPreview());
                intent.putExtra("sender", emailItemList.get(position).getSender());
                intent.putExtra("subject", emailItemList.get(position).getSubject());
                startActivityForResult(intent, EMAIL_EDIT_DRAFT);
            }
        });
        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == EMAIL_EDIT_DRAFT){
                Toast.makeText(getContext(), "Message send", Toast.LENGTH_SHORT).show();
                if(Integer.parseInt(vitri) == -1)
                    return;
                EmailItem item = emailItemList.get(Integer.parseInt(vitri));
                MyApplication app = (MyApplication) this.getContext().getApplicationContext();
                app.removeItem_DraftMailList(Integer.parseInt(vitri));
                app.addItem_SendMailList(item);
                adapter.notifyDataSetChanged();

            }else if(requestCode == EMAIL_COMPOSE_NEW){
                Toast.makeText(getContext(), "New message send", Toast.LENGTH_SHORT).show();
//                EmailItem item = new EmailItem("", "", "", "");
//                item.setDate(data.getStringExtra("date"));
//                item.setPreview(data.getStringExtra("preview"));
//                item.setSender(data.getStringExtra("sender"));
//                item.setSubject(data.getStringExtra("subject"));
//                emailItemList.add(item);
//                adapter.notifyDataSetChanged();
            }
        }else {

        };
    }

    private void createData(){
        emailItemList = new ArrayList<EmailItem>();
//        emailItemList.add(new EmailItem("Thu moi hop", "Feb 28", "vanminh@hostmail.com", "Kinh moi quy phu huynh..."));
//        emailItemList.add(new EmailItem("Thu hoc phi", "Jan 21", "giaovien_@van.com", "Thong bao nop hoc phi..."));
//        emailItemList.add(new EmailItem("Hop hoi dong", "July 8", "hoidong@gmail.com", "Sap co thoi khoa bieu moi..."));
//        emailItemList = ManageMailData.getTrashMailList();
//
//
//        SharedPreferences mSharedPreference1 =   PreferenceManager.getDefaultSharedPreferences(getContext());
//
//        int size = mSharedPreference1.getInt("draft_count", 0);
//
//        for(int i=0;i<size;i++)
//        {
//            String subject = (mSharedPreference1.getString("Draft_Sub_" + i, null));
//            String sender = (mSharedPreference1.getString("Draft_Sender_" + i, null));
//            String preview = (mSharedPreference1.getString("Draft_Preview_" + i, null));
//            String date = (mSharedPreference1.getString("Draft_Date_" + i, null));
//            emailItemList.add(new EmailItem(subject, date, sender, preview));
//        }
        MyApplication app = (MyApplication) this.getContext().getApplicationContext();
        emailItemList = app.getData_DraftMailList();
    };
}

