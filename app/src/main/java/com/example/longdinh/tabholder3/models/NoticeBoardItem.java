package com.example.longdinh.tabholder3.models;

/**
 * Created by long dinh on 13/05/2016.
 */
public class NoticeBoardItem {
    String id;
    String subject;
    String notice;
    String level;
    String deadline;

    public NoticeBoardItem(String id, String subject, String notice, String level, String deadline) {
        this.id = id;
        this.subject = subject;
        this.notice = notice;
        this.level = level;
        this.deadline = deadline;
    }

    public NoticeBoardItem() {
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
}
