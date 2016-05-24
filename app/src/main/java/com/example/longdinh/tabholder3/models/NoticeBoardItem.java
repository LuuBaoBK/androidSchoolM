package com.example.longdinh.tabholder3.models;

import android.text.Html;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by long dinh on 13/05/2016.
 */
public class NoticeBoardItem {
    String id;
    String subject;
    String notice;
    String level;
    String deadline;
    String datewrote;
    String title ;
    String author;

    public NoticeBoardItem(String id, String subject, String notice, String level, String deadline, String date_wrote, String title, String author) {
        this.id = id;
        this.subject = subject;
        this.notice = notice;
        this.level = level;
        this.deadline = deadline;
        this.datewrote = date_wrote;
        this.title = title;
        this.author = author;
    }

    public NoticeBoardItem() {
        this.id = "";
        this.subject = "";
        this.notice = "";
        this.level = "";
        this.deadline = "";
        this.datewrote = "";
        this.title = "";
        this.author = "";
    }

    public NoticeBoardItem (String data){
        try {
            JSONObject item  = new JSONObject(data);
            this.id = item.getString("id");
            this.subject = item.getString("subject");
            this.notice = item.getString("notice");
            this.level = item.getString("level");
            this.deadline = item.getString("deadline");
            this.datewrote =item.getString("datewrote");
            this.title = item.getString("title");
            this.author =item.getString("author");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getPreference(){
        if(notice.length() < 30){
            return notice;
        }else{
            return notice.substring(0,30);
        }
    };

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDatewrote() {
        return datewrote;
    }

    public void setDatewrote(String datewrote) {
        this.datewrote = datewrote;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String toJsonString(){
        return "{\"id\":\" "+id+" \",\"subject\":\"  "+subject+" \",\"notice\":\" "+notice+" \",\"level\":\" "+level+" \",\"deadline\":\" "+deadline+" \",\"datewrote\":\" "+datewrote+" \",\"title\":\" "+title+" \",\"author\":\" "+author+" \"}";
//        return "{\"id\":\""+id+"\",\"subject\":\""+subject+"\",\"notice\":\""+notice+"\",\"level\":\""+level+"\",\"deadline\":\""+deadline+"\"}";
    };


}
