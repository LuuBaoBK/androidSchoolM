package com.example.longdinh.tabholder3.inner_fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.SyncMail.SyncOutboxMail;
import com.example.longdinh.tabholder3.activities.MailContent;
import com.example.longdinh.tabholder3.activities.MyApplication;
import com.example.longdinh.tabholder3.activities.ReadMailAcitivity;
import com.example.longdinh.tabholder3.activities.RequestManager;
import com.example.longdinh.tabholder3.adapters.EmailItemAdapter;
import com.example.longdinh.tabholder3.models.EmailItem;
import com.software.shell.fab.ActionButton;

import java.util.List;

/**
 * Created by long dinh on 03/05/2016.
 */
public class Tab5Fragment extends Fragment{
    List<EmailItem> emailItemList;
    ListView lvEmailItem;
    EmailItemAdapter adapter;
    final int  EMAIL_COMPOSE_NEW = 101;
    MyApplication app ;
    String vitri = new String(-1 + "");
    SwipeRefreshLayout refreshLayout;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab1fragment, container, false);
        app = (MyApplication) getActivity().getApplication();

        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Syncing. Please wait...");

        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setEnabled(true);
                if(isOnline()){
                    new SyncOutboxMail(app, new RequestManager(), null,dialog).execute();
                }else{
                    Toast.makeText(getContext(), "No connection", Toast.LENGTH_SHORT).show();
                }
                refreshLayout.setEnabled(false);
            }
        });

        lvEmailItem = (ListView) v.findViewById(R.id.lvEmailItem);
        lvEmailItem.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvEmailItem.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkedCount = lvEmailItem.getCheckedItemCount();
                mode.setTitle(checkedCount + " Selected");
                adapter.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_delete, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itDelete:
                        SparseBooleanArray selected = adapter.getSelectedIds();

                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                EmailItem selecteditem = (EmailItem) adapter.getItem(selected.keyAt(i));
                                //thong tin ve mail mail box chi co o local nen ko can update len server
                                //nen can canh bao nguoi dung ve hanh vi xoa cua minh
                                Toast.makeText(getContext(), "outbox mail xoa vinh vien "+ selecteditem.getId(), Toast.LENGTH_SHORT).show();
                                adapter.remove(selecteditem);
                            }
                        }
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.removeSelection();
            }
        });

        ActionButton actionButton = (ActionButton) v.findViewById(R.id.action_button);
        actionButton.setImageResource(R.drawable.fab_plus_icon);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MailContent.class);
                intent.putExtra("type", "COMPOSE");
                intent.putExtra("id", -1);
                intent.putExtra("date", "");
                intent.putExtra("preview", "");
                intent.putExtra("sender", "");
                intent.putExtra("subject","");
                startActivityForResult(intent, EMAIL_COMPOSE_NEW);

            }
        });

//        createData();
//        emailItemList = new ArrayList<EmailItem>();
        emailItemList = app.getData_OutboxMailList();
        adapter = new EmailItemAdapter(getContext(), R.layout.item_email, emailItemList);
        lvEmailItem.setAdapter(adapter);
        lvEmailItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                vitri = position + "";
//                new showMailDetail().execute(emailItemList.get(position).getId()+ "");
                if(emailItemList.get(position).getIsRead()){
                    emailItemList.get(position).setIsRead(false);
                    app.addItem_InboxReadMail(emailItemList.get(position).getId() + "");
                    adapter.notifyDataSetChanged();
                    System.out.println("da them mot mail moi-----");
                    // cam kiem tra xem co mang khong hay co thuoc 100 mail dau tien khong
                }
                Intent intent = new Intent(getContext(), ReadMailAcitivity.class);
                intent.putExtra("id", emailItemList.get(position).getId()+ "");
                intent.putExtra("typeMail","outbox");
                startActivityForResult(intent, 700);
            }
        });

        return v;
    }






    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
//        new getListMailInbox().execute("");
//        Toast.makeText(getContext(), "da toi day", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //do nothing
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}


