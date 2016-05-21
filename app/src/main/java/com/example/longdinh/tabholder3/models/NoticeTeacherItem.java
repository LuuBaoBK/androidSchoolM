package com.example.longdinh.tabholder3.models;

import java.util.List;

/**
 * Created by long dinh on 21/05/2016.
 */
public class NoticeTeacherItem {

    String subject;
    String title;
    String level;
    String datewrote;
    List<ItemClassDate> listclass;
    String content;

    public NoticeTeacherItem(String subject, String title, String level, String datewrote, List<ItemClassDate> listclass, String content) {
        this.subject = subject;
        this.title = title;
        this.level = level;
        this.datewrote = datewrote;
        this.listclass = listclass;
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDatewrote() {
        return datewrote;
    }

    public void setDatewrote(String datewrote) {
        this.datewrote = datewrote;
    }

    public List<ItemClassDate> getListclass() {
        return listclass;
    }

    public void setListclass(List<ItemClassDate> listclass) {
        this.listclass = listclass;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
