package com.example.longdinh.tabholder3.models;

/**
 * Created by long dinh on 21/05/2016.
 */
public class ItemClassDate {

    String classname;
    String date;

    public ItemClassDate(String classname, String date) {
        this.classname = classname;
        this.date = date;
    }


    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
