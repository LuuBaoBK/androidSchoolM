package com.example.longdinh.tabholder3.SyncMail;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.longdinh.tabholder3.activities.MyApplication;
import com.example.longdinh.tabholder3.activities.RequestManager;

/**
 * Created by long dinh on 22/05/2016.
 */
public class SyncDraftMail extends AsyncTask<Void, Void , String> {

    MyApplication app;
    RequestManager mgr;
    AsyncTask nextWork;


    public SyncDraftMail(MyApplication app, RequestManager mgr, AsyncTask nextWork, ProgressDialog dialog) {
        this.app = app;
        this.mgr = mgr;
        this.nextWork = nextWork;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
//        while(app.getDraftNewMail().size() > 0){//update for each mail when all mail is updated
//            List<EmailItem> draftMailList = app.getData_DraftMailList();
//            for(int i = 0 ;i < draftMailList.size();i++){
//                if(app.getDraftNewMail().equals(draftMailList.get(i).getId())){
//                    mgr.postDataToServer("api/update/draft_mail", app.getToken(), draftMailList.get(i).toData());
//                }
//            }
//            System.out.println("update draft id: " + app.getDraftNewMail().get(0));
//            app.getDraftNewMail().remove(0);
//            publishProgress();// dung de update lai giao dien ngay khi co thay doi de tranh loi ve ui
//        }
        return null;//truong hop khong co trong mail ma cung khong the online
    }

    protected void onProgressUpdate() {
        app.notifyChangeDraft();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(nextWork!= null)
            nextWork.execute();
    }
}
