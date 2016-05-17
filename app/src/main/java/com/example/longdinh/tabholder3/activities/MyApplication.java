package com.example.longdinh.tabholder3.activities;

import android.app.Application;

import com.example.longdinh.tabholder3.models.EmailItem;
import com.example.longdinh.tabholder3.models.StudentItemSpinner;

import java.util.List;

/**
 * Created by long dinh on 03/05/2016.
 */
public class MyApplication extends Application {
    private List<EmailItem> InboxMailList;
    private List<EmailItem> SendMailList;
    private List<EmailItem> DraftMailList;
    private List<EmailItem> TrashMailList;

    public List<StudentItemSpinner> getListchildren() {
        return listchildren;
    }

    public void setListchildren(List<StudentItemSpinner> listchildren) {
        this.listchildren = listchildren;
    }

    private List<StudentItemSpinner> listchildren;
    private String id;
    private String token;
    private String fullName;
    private String currentchild;
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getCurrentchild() {
        return currentchild;
    }

    public void setCurrentchild(String currentchild) {
        this.currentchild = currentchild;
    }




    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<EmailItem> getData_InboxMailList() {return InboxMailList;}
    public void setData_InboxMailList(List<EmailItem> data) {this.InboxMailList = data;}
    public void removeItem_InboxMailList(int position){this.InboxMailList.remove(position);};
    public void addItem_InboxMailList(EmailItem item) {this.InboxMailList.add(0,item);};
    public int getSize_InboxMailList(){
        if(InboxMailList != null ) return InboxMailList.size();
        else return -1;
    };

    public List<EmailItem> getData_SendMailList() {return SendMailList;}
    public void setData_SendMailList(List<EmailItem> data) {this.SendMailList = data;}
    public void removeItem_SendMailList(int position){this.SendMailList.remove(position);};
    public void addItem_SendMailList(EmailItem item) {this.SendMailList.add(0,item);};
    public int getSize_SendMailList(){
        if(SendMailList != null ) return SendMailList.size();
        else return -1;
    };


    public List<EmailItem> getData_DraftMailList() {return DraftMailList;}
    public void setData_DraftMailList(List<EmailItem> data) {this.DraftMailList = data;}
    public void removeItem_DraftMailList(int position){this.DraftMailList.remove(position);};
    public void addItem_DraftMailList(EmailItem item) {this.DraftMailList.add(0,item);};
    public int getSize_DraftMailList(){
        if(DraftMailList != null ) return DraftMailList.size();
        else return -1;
    };

    public List<EmailItem> getData_TrashMailList() {return TrashMailList;}
    public void setData_TrashMailList(List<EmailItem> data) {this.TrashMailList = data;}
    public void removeItem_TrashMailList(int position){this.TrashMailList.remove(position);};
    public void addItem_TrashMailList(EmailItem item) {this.TrashMailList.add(0,item);};
    public int getSize_TrashMailList(){
        if(TrashMailList != null ) return TrashMailList.size();
        else return -1;
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
