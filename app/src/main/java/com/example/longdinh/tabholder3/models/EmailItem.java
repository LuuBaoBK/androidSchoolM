package com.example.longdinh.tabholder3.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by long dinh on 08/04/2016.
 */
public class EmailItem {
    int id;
    String subject;
    String date;
    String sender;
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

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getPreview() {
        return preview;
    }

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
}
