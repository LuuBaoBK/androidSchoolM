package com.example.longdinh.tabholder3.models;

import android.text.Html;

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


    public EmailItem(int id , String subject, String date, String sender, String preview) {
        this.id = id;
        this.subject = subject;
        this.date = date;
        this.sender = sender;
        this.preview = preview;
        this.isRead = false;
    }

    public EmailItem(int id, String subject, String date, String sender, String preview, Boolean isRead) {
        this.id = id;
        this.subject = subject;
        this.date = date;
        this.sender = sender;
        this.preview = preview;
        this.isRead = isRead;
    }

    public EmailItem(int id, String subject, String date, String sender, String receiver, String preview, Boolean isRead) {
        this.id = id;
        this.subject = subject;
        this.date = date;
        this.sender = sender;
        this.receiver = receiver;
        this.preview = preview;
        this.isRead = isRead;
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


    public String toString(){
        return "{\"id\":"+id+",\"content\": \""+preview+"\",\"title\": \""+subject+"\",\"date_time\": \""+date+"\",\"author\": \""+sender+"\", \"receiver\":" + ((receiver==null)?("\"\""):("\""+receiver+"\""))+"}";
//        { "id": 1,"content": "Noi dung khong quan trong chay dung la dc","title": "Mail sent to server","date_time": "Apr 29","author": "t0001@schoolm.com", "receiver":"t_000002@schoolm.com"}
    };
}
