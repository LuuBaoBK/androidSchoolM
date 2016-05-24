package com.example.longdinh.tabholder3.models;

import android.text.Html;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by long dinh on 08/04/2016.
 */
public class EmailItem {
    int id;
    String subject;
    String date;
    String sender;
    String receiver;
    String preview;
    Boolean isRead;
    Boolean isSync;


//    public EmailItem(int id , String subject, String date, String sender, String preview) {
//        this.id = id;
//        this.subject = subject;
//        this.date = date;
//        this.sender = sender;
//        this.preview = preview;
//        this.isRead = false;
//        this.isSync = true;
//    }
//
//    public EmailItem(int id, String subject, String date, String sender, String preview, Boolean isRead) {
//        this.id = id;
//        this.subject = subject;
//        this.date = date;
//        this.sender = sender;
//        this.preview = preview;
//        this.isRead = isRead;
//        this.isSync = true;
//    }
//
//    public EmailItem(int id, String subject, String date, String sender, String receiver, String preview, Boolean isRead) {
//        this.id = id;
//        this.subject = subject;
//        this.date = date;
//        this.sender = sender;
//        this.receiver = receiver;
//        this.preview = preview;
//        this.isRead = isRead;
//    }


    public EmailItem(int id, String subject, String date, String sender, String receiver, String preview, Boolean isRead, Boolean isSync) {
        this.id = id;
        this.subject = subject;
        this.date = date;
        this.sender = sender;
        this.receiver = receiver;
        this.preview = preview;
        this.isRead = isRead;
        this.isSync = isSync;
    }

    public EmailItem (String data){
        try {
            JSONObject  email  = new JSONObject(data);
            this.id = email.getInt("id");
            this.subject = email.getString("subject");
            this.date = email.getString("date");
            this.sender = email.getString("sender");
            this.receiver = email.getString("receiver");
            this.preview = email.getString("preview");
            this.isRead = email.getBoolean("isRead");
            this.isSync = email.getBoolean("isSync");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String getReceiver() {
        return Html.fromHtml(receiver).toString();
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getPreview() {
        preview = Html.fromHtml(preview).toString();
        if(preview.length() < 30){
            return preview;
        }else{
            return preview.substring(0,30);
        }
    }


    public String getContent(){
        return preview;
    };

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }


    public Boolean getIsSync() {
        return isSync;
    }

    public void setIsSync(Boolean isSync) {
        this.isSync = isSync;
    }

    public String toJsonString(){// nhu vay cai nay dung trnng truong hop minh muon luu data xuong local
        return "{\"id\":"+id+",\"content\":\""+preview+"\",\"title\":\""+subject+"\",\"date_time\":\""+date+"\",\"author\":\""+sender+"\",\"receiver\":\""+receiver+"\",\"isRead\":"+isRead+",\"isSync\":"+isSync+"}";
//      {"id":1,"content":"Noi dung khong quan trong chay dung la dc","title":"Mail sent to server","date_time":"Apr 29","author":"t0001@schoolm.com","receiver":"t_000002@schoolm.com","isRead":false,"isSync":true}
    };

    public String toData(){// chi dung cho truong hop gui data len server
        return "id:"+id+"&content="+preview+"&title="+subject+"&date_time="+date+"&author="+sender+"&receiver="+receiver;
    }
}
