package com.example.longdinh.tabholder3.models;

/**
 * Created by long dinh on 12/04/2016.
 */
public class NavItemChild {

    private String title;
    private int num;
    private int resIcon;

    public NavItemChild(String title, int resIcon) {
        super();
        this.title = title;
        this.resIcon = resIcon;
        this.num = 0;
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

