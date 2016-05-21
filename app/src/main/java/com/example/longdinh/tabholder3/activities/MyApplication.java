package com.example.longdinh.tabholder3.activities;

import android.app.Application;


import com.example.longdinh.tabholder3.adapters.EmailItemAdapter;
import com.example.longdinh.tabholder3.adapters.MyExpandableListAdapter;
import com.example.longdinh.tabholder3.models.EmailItem;
import com.example.longdinh.tabholder3.models.ItemClassDate;
import com.example.longdinh.tabholder3.adapters.MyExpandableListAdapter;
import com.example.longdinh.tabholder3.models.NavItemChild;
import com.example.longdinh.tabholder3.models.NoticeBoardItem;
import com.example.longdinh.tabholder3.models.StudentItemSpinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by long dinh on 03/05/2016.
 */
public class MyApplication extends Application {
    private List<EmailItem> InboxMailList;
    List<String>        InboxReadMail = new ArrayList<String>();
    List<String>        InboxDeleteMail = new ArrayList<String>();

    private List<EmailItem> SendMailList;
    List<String>        SendDeleteMail = new ArrayList<String>();

    private List<EmailItem> DraftMailList;
    List<String>        DraftDeleteMail = new ArrayList<String>();
    List<String>        DraftNewMail = new ArrayList<String>();


    private List<EmailItem> TrashMailList;


    private List<NoticeBoardItem> NoticeListT2;
    private List<NoticeBoardItem> NoticeListT3;
    private List<NoticeBoardItem> NoticeListT4;
    private List<NoticeBoardItem> NoticeListT5;
    private List<NoticeBoardItem> NoticeListT6;
    private List<NoticeBoardItem> NoticeListT7;


    private List<EmailItem> OutboxMailList;


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


    public List<EmailItem> getData_OutboxMailList() {return OutboxMailList;}
    public void setData_OutboxMailList(List<EmailItem> data) {this.OutboxMailList = data;}
    public void removeItem_OutboxMailList(int position){this.OutboxMailList.remove(position);};
    public void addItem_OutboxhMailList(EmailItem item) {this.OutboxMailList.add(0,item);};
    public int getSize_OutboxMailList(){
        if(OutboxMailList != null ) return OutboxMailList.size();
        else return -1;
    };



    // xu li cho inbox mail

    public List<EmailItem> getData_InboxMailList() {return InboxMailList;}
    public void setData_InboxMailList(List<EmailItem> data) {this.InboxMailList = data;}
    public void removeItem_InboxMailList(int position){this.InboxMailList.remove(position);};
    public void addItem_InboxMailList(EmailItem item) {this.InboxMailList.add(0,item);};
    public int getSize_InboxMailList(){
        if(InboxMailList != null ) return InboxMailList.size();
        else return -1;
    };


    public List<String> getInboxReadMail() {
        return InboxReadMail;
    }

    public void setInboxReadMail(List<String> inboxReadMail) {
        InboxReadMail = inboxReadMail;
    }
    public void removeItem_InboxReadMail(int position){this.InboxReadMail.remove(position);};

    public void addItem_InboxReadMail(String item) {this.InboxReadMail.add(0,item);};




    public List<String> getSendDeleteMail() {
        return SendDeleteMail;
    }

    public void setSendDeleteMail(List<String> SendDeleteMail) {
        this.SendDeleteMail = SendDeleteMail;
    }

    public void removeItem_SendDeleteMail(int position){this.SendDeleteMail.remove(position);};

    public void addItem_SendDeleteMail(String item) {this.SendDeleteMail.add(0, item);};



    public List<String> getInboxDeleteMail() {
        return InboxDeleteMail;
    }
    public void setInboxDeleteMail(List<String> inboxDeleteMail) {
        InboxDeleteMail = inboxDeleteMail;
    }

    public void removeItem_InboxDeleteMail(int position){this.InboxDeleteMail.remove(position);};

    public void addItem_InboxDeleteMail(String item) {this.InboxDeleteMail.add(0, item);};




    public List<String> getDraftDeleteMail() {
        return DraftDeleteMail;
    }

    public void setDraftDeleteMail(List<String> draftDeleteMail) {
        DraftDeleteMail = draftDeleteMail;
    }
    public void removeItem_DraftDeleteMail(int position){this.DraftDeleteMail.remove(position);};

    public void addItem_DraftDeleteMail(String item) {this.DraftDeleteMail.add(0, item);};



    public List<String> getDraftNewMail() {
        return DraftNewMail;
    }

    public void setDraftNewMail(List<String> draftNewMail) {
        DraftNewMail = draftNewMail;
    }
    public void removeItem_DraftNewMail(int position){this.DraftNewMail.remove(position);};

    public void addItem_DraftNewMail(String item) {this.DraftNewMail.add(0, item);};




    //end thong tin xu li cho inbox mail
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


    // vung xu li cho notice cua hoc sinh
    public List<NoticeBoardItem> getData_NoticeListT2() {return NoticeListT2;}
    public void setData_NoticeListT2(List<NoticeBoardItem> data) {this.NoticeListT2 = data;}
    public void removeItem_NoticeListT2(int position){this.NoticeListT2.remove(position);};
    public void addItem_NoticeListT2(NoticeBoardItem item) {this.NoticeListT2.add(0,item);};
    public int getSize_NoticeListT2(){
        if(NoticeListT2 != null ) return NoticeListT2.size();
        else return -1;
    };

    public List<NoticeBoardItem> getData_NoticeListT3() {return NoticeListT3;}
    public void setData_NoticeListT3(List<NoticeBoardItem> data) {this.NoticeListT3 = data;}
    public void removeItem_NoticeListT3(int position){this.NoticeListT3.remove(position);};
    public void addItem_NoticeListT3(NoticeBoardItem item) {this.NoticeListT3.add(0,item);};
    public int getSize_NoticeListT3(){
        if(NoticeListT3 != null ) return NoticeListT3.size();
        else return -1;
    };

    public List<NoticeBoardItem> getData_NoticeListT4() {return NoticeListT4;}
    public void setData_NoticeListT4(List<NoticeBoardItem> data) {this.NoticeListT4 = data;}
    public void removeItem_NoticeListT4(int position){this.NoticeListT4.remove(position);};
    public void addItem_NoticeListT4(NoticeBoardItem item) {this.NoticeListT4.add(0,item);};
    public int getSize_NoticeListT4(){
        if(NoticeListT4 != null ) return NoticeListT4.size();
        else return -1;
    };

    public List<NoticeBoardItem> getData_NoticeListT5() {return NoticeListT5;}
    public void setData_NoticeListT5(List<NoticeBoardItem> data) {this.NoticeListT5 = data;}
    public void removeItem_NoticeListT5(int position){this.NoticeListT5.remove(position);};
    public void addItem_NoticeListT5(NoticeBoardItem item) {this.NoticeListT5.add(0,item);};
    public int getSize_NoticeListT5(){
        if(NoticeListT5 != null ) return NoticeListT5.size();
        else return -1;
    };


    public List<NoticeBoardItem> getData_NoticeListT6() {return NoticeListT6;}
    public void setData_NoticeListT6(List<NoticeBoardItem> data) {this.NoticeListT6 = data;}
    public void removeItem_NoticeListT6(int position){this.NoticeListT6.remove(position);};
    public void addItem_NoticeListT6(NoticeBoardItem item) {this.NoticeListT6.add(0,item);};
    public int getSize_NoticeListT6(){
        if(NoticeListT6 != null ) return NoticeListT6.size();
        else return -1;
    };

    public List<NoticeBoardItem> getData_NoticeListT7() {return NoticeListT7;}
    public void setData_NoticeListT7(List<NoticeBoardItem> data) {this.NoticeListT7 = data;}
    public void removeItem_NoticeListT7(int position){this.NoticeListT7.remove(position);};
    public void addItem_NoticeListT7(NoticeBoardItem item) {this.NoticeListT7.add(0,item);};
    public int getSize_NoticeListT7(){
        if(NoticeListT7 != null ) return NoticeListT7.size();
        else return -1;
    };

    //cac ham su dung hoc viec notify khico m1 message mo xuat hien
    NavItemChild numMailinbox = null;
    MyExpandableListAdapter listAdapter;

    public NavItemChild getNumMailinbox() {
        return numMailinbox;
    }

    public void setNumMailinbox(NavItemChild numMailinbox) {
        this.numMailinbox = numMailinbox;
    }

    public MyExpandableListAdapter getListAdapter() {
        return listAdapter;
    }

    public void setListAdapter(MyExpandableListAdapter listAdapter) {
        this.listAdapter = listAdapter;
    }

    public void notifyChangeNumInbox(){
        this.listAdapter.notifyDataSetChanged();
    };


    ///end using for notice




}
