package com.example.longdinh.tabholder3.models;

import android.widget.ArrayAdapter;

/**
 * Created by long dinh on 14/05/2016.
 */
public class StudentItemSpinner{
    String mahs;
    String Ten;

    public StudentItemSpinner(String mahs, String ten) {
        this.mahs = mahs;
        Ten = ten;
    }

    public StudentItemSpinner() {
    }

    public String getMahs() {
        return mahs;
    }

    public void setMahs(String mahs) {
        this.mahs = mahs;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }
}
