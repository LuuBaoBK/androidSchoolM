package com.example.longdinh.tabholder3.models;

/**
 * Created by long dinh on 12/04/2016.
 */
public class NavItem {

    private String title;
    private int resIcon;
    private int num;

    public NavItem(String title, int resIcon) {
        this.title = title;
        this.resIcon = resIcon;
        this.num = 0;
    }

    public NavItem(String title, int resIcon, int num) {
        this.title = title;
        this.resIcon = resIcon;
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}

