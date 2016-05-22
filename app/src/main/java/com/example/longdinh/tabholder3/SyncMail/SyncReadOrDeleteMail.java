package com.example.longdinh.tabholder3.SyncMail;

import android.os.AsyncTask;

import com.example.longdinh.tabholder3.activities.MyApplication;
import com.example.longdinh.tabholder3.activities.RequestManager;
import com.example.longdinh.tabholder3.models.EmailItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by long dinh on 22/05/2016.
 */
public class SyncReadOrDeleteMail extends AsyncTask<Void, Void , String> {

    MyApplication app;
    RequestManager mgr;
    AsyncTask nextWork;

    public SyncReadOrDeleteMail(MyApplication app, RequestManager mgr, AsyncTask nextWork) {
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

        // thu tu update la gi
        //update raead cho mail inbox
        List<String >listReadMail = app.getInboxReadMail();
        String toStringReadMail = "";
        for(int i = 0; i < listReadMail.size(); i++){
            if(i == 0)
                toStringReadMail = listReadMail.get(i);
            else
                toStringReadMail = toStringReadMail+", " +listReadMail.get(i);
        }


        List<String >listDeleteMail = new ArrayList<String>();
        listDeleteMail.addAll(app.getDraftDeleteMail());
        listDeleteMail.addAll(app.getSendDeleteMail());
        listDeleteMail.addAll(app.getInboxDeleteMail());
        String toStringDeleteMail = "";
        for(int i = 0; i < listDeleteMail.size(); i++){
            if(i == 0)
                toStringDeleteMail = listDeleteMail.get(i);
            else
                toStringDeleteMail = toStringDeleteMail+", " +listDeleteMail.get(i);
        }


        String dataRequest = "listRead=("+toStringReadMail+")&listDelete=(" + listDeleteMail+")";

        mgr.postDataToServer("api/post/udpate/readAndDelete", app.getToken(), dataRequest);

        //problem: neu trong truong hop khong the update thi co the vo vong lap vo han
        //nhung tron trong hop nay thi hinh nhu du lieu dang cho cho toi tkhi nao viec gui 1 request len server thanh cong?
        // neu nhu vay thi se co van de neu nhu kong the gui dc.
        return null;//truong hop khong co trong mail ma cung khong the online
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(nextWork!= null)
            nextWork.execute();
    }
}
