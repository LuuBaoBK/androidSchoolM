package com.example.longdinh.tabholder3.models;

/**
 * Created by long dinh on 13/05/2016.
 */
public class StudentClass {
    String image;
    String name;
    String sdt;
    String maHs;

    public StudentClass() {
    }

    public StudentClass(String image, String name, String sdt, String maHs) {
        this.image = image;
        this.name = name;
        this.sdt = sdt;
        this.maHs = maHs;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getMaHs() {
        return maHs;
    }

    public void setMaHs(String maHs) {
        this.maHs = maHs;
    }
}
