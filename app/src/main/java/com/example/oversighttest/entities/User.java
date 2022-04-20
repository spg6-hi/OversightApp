package com.example.oversighttest.entities;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
    @SerializedName("userName")
    private String userName;

    @SerializedName("password")
    private String password;

    @SerializedName("created")
    private String created;

    private LocalDate dateCreated;

    private User(String userName, String password, String created){
        this.userName = userName;
        this.password = password;
        this.created = created;
    }

    private User(String userName, String password){
        this.userName = userName;
        this.password = password;
        this.dateCreated = LocalDate.now();
    }

    public void setData(){
        if(this.dateCreated != null) return;
        this.dateCreated = LocalDate.parse(this.created);
    }

    public String toString(){
        return userName + " / " + password +  "/ " + created;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
