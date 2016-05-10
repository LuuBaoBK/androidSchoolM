package com.example.longdinh.tabholder3.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by long dinh on 05/05/2016.
 */
public class UserInfo {
    private String id;
    private String firstname;
    private String middlename;
    private String lastname;
    private String fullname;
    private String token;
    private String role;
    private String dateofbirth;
    private String gender;
    private String address;
    private String private_data;

    public UserInfo(String inputString) throws JSONException {
        JSONObject userinfo = new JSONObject(inputString);
        this.id          = userinfo.getString("id");
        this.firstname   = userinfo.getString("firstname");
        this.middlename  = userinfo.getString("middlename");
        this.lastname    = userinfo.getString("lastname");
        this.fullname    = userinfo.getString("fullname");
        this.token       = userinfo.getString("token");
        this.role        = userinfo.getString("role");
        this.dateofbirth = userinfo.getString("dateofbirth");
        this.gender      = userinfo.getString("gender");
        this.address     = userinfo.getString("address");
        switch (this.role){
            case "0":
                this.role = "Admin";
                this.private_data = userinfo.getString("admin");
                break;
            case "1":
                this.role = "Teacher";
                this.private_data = userinfo.getString("teacher");
                break;
            case "2":
                this.role = "Student";
                this.private_data = userinfo.getString("student");
                break;
            case "3":
                this.role = "Parent";
                this.private_data = userinfo.getString("parent");
                break;
        }

    }
    public String getId(){
        return this.id;
    }
    public String getFirstname(){
        return this.firstname;
    }
    public String getMiddlename(){
        return this.middlename;
    }
    public String getLastname(){
        return this.lastname;
    }
    public String getFullname(){
        return this.fullname;
    }
    public String getToken(){
        return this.token;
    }
    public String getRole(){
        return this.role;
    }
    public String getGender(){
        return this.gender;
    }
    public String getAddress(){
        return this.address;
    }
    public String getDateofbirth(){
        return this.dateofbirth;
    }
    public String getPrivateData(){
        return this.private_data;
    }
}

