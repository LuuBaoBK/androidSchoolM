package com.example.longdinh.tabholder3.activities;

import com.example.longdinh.tabholder3.models.EmailItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by long dinh on 02/05/2016.
 */
 public final class ManageMailData {
    public static final List<EmailItem> InboxMailList  = new ArrayList<EmailItem>();
    public static final List <EmailItem> SendMailList  = new ArrayList<EmailItem>();
    public static final List<EmailItem> DraftMailList  = new ArrayList<EmailItem>();
    public static final List<EmailItem> TrashMailList  = new ArrayList<EmailItem>();

    public static void main(String[] args){
//        InboxMailList.add(new EmailItem(1, "Thu moi hop 1", "Feb 28", "vanminh@hostmail.com", "Kinh moi quy phu huynh..."));
//        InboxMailList.add(new EmailItem("Thu moi hop 1", "Feb 28", "vanminh@hostmail.com", "Kinh moi quy phu huynh..."));
//        InboxMailList.add(new EmailItem("Thu hoc phi 1", "Jan 21", "giaovien_@van.com", "Thong bao nop hoc phi..."));
//        InboxMailList.add(new EmailItem("Hop hoi dong 1", "July 8", "hoidong@gmail.com", "Sap co thoi khoa bieu moi..."));
//
//
//
//        SendMailList.add(new EmailItem("Thu moi hop 2", "Feb 28", "vanminh@hostmail.com", "Kinh moi quy phu huynh..."));
//        SendMailList.add(new EmailItem("Thu hoc phi 2", "Jan 21", "giaovien_@van.com", "Thong bao nop hoc phi..."));
//        SendMailList.add(new EmailItem("Hop hoi dong 2", "July 8", "hoidong@gmail.com", "Sap co thoi khoa bieu moi..."));
//
//
//        DraftMailList.add(new EmailItem("Thu moi hop 3", "Feb 28", "vanminh@hostmail.com", "Kinh moi quy phu huynh..."));
//        DraftMailList.add(new EmailItem("Thu hoc phi 3", "Jan 21", "giaovien_@van.com", "Thong bao nop hoc phi..."));
//        DraftMailList.add(new EmailItem("Hop hoi dong 3", "July 8", "hoidong@gmail.com", "Sap co thoi khoa bieu moi..."));
//
//        TrashMailList.add(new EmailItem("Thu moi hop 4", "Feb 28", "vanminh@hostmail.com", "Kinh moi quy phu huynh..."));
//        TrashMailList.add(new EmailItem("Thu hoc phi 4", "Jan 21", "giaovien_@van.com", "Thong bao nop hoc phi..."));
//        TrashMailList.add(new EmailItem("Hop hoi dong 4", "July 8", "hoidong@gmail.com", "Sap co thoi khoa bieu moi..."));
    }


    public static final List<EmailItem> getInboxMailList() {
        return InboxMailList;
    }


    public static final List<EmailItem> getSendMailList() {
        return SendMailList;
    }

    public static final List<EmailItem> getDraftMailList() {
        return DraftMailList;
    }

    public static final List<EmailItem> getTrashMailList() {
        return TrashMailList;
    }

}
