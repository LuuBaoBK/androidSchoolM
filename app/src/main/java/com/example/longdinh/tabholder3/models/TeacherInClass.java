package com.example.longdinh.tabholder3.models;

/**
 * Created by long dinh on 13/05/2016.
 */
public class TeacherInClass {
    String image;
    String name;
    String subject;
    String email;

    public TeacherInClass(String image, String name, String subject, String email) {
        this.image = image;
        this.name = name;
        this.subject = subject;
        this.email = email;
    }

    public TeacherInClass() {
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
