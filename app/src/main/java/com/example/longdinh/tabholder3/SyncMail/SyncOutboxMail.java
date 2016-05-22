package com.example.longdinh.tabholder3.SyncMail;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.longdinh.tabholder3.activities.MyApplication;
import com.example.longdinh.tabholder3.activities.RequestManager;
import com.example.longdinh.tabholder3.models.EmailItem;

import java.util.List;

/**
 * Created by long dinh on 22/05/2016.
 */
public class SyncOutboxMail extends AsyncTask<Void, Void , String> {

    MyApplication app;
    RequestManager mgr;
    SyncDraftMail nextWork;
    ProgressDialog dialog;

    public SyncOutboxMail(MyApplication app, RequestManager mgr, SyncDraftMail nextWork,ProgressDialog dialog ) {
        this.app = app;
        this.mgr = mgr;
        this.dialog = dialog;
        this.nextWork = nextWork;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(dialog!= null)
            dialog.show();

    }

    @Override
    protected String doInBackground(Void... params) {
        List<EmailItem> outBoxMailList = app.getData_OutboxMailList();
        return null;
//        while(outBoxMailList.size() > 0){//update for each mail when all mail is updated
//            mgr.postDataToServer("api/post/mailbox/update/outbox_mail", app.getToken(), outBoxMailList.get(0).toData());
//            outBoxMailList.remove(0);
//            publishProgress();// dung de update lai giao dien ngay khi co thay doi de tranh loi ve ui
//        }
//        return null;//truong hop khong co trong mail ma cung khong the online
    }

    protected void onProgressUpdate(String... progress) {
        app.notifyChangeOutbox();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(dialog!= null)
            dialog.dismiss();
        if(nextWork!= null)
            nextWork.execute();
    }
}
