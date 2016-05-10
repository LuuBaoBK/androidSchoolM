package com.example.longdinh.tabholder3.models;

/**
 * Created by long dinh on 12/04/2016.
 */
public class NavItem {

    private String title;
    private int resIcon;

    public NavItem(String title, int resIcon) {
        super();
        this.title = title;
        this.resIcon = resIcon;
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

}

