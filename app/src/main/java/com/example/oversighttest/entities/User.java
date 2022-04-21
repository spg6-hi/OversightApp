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

    //So that we can get the date from the rest API
    @SerializedName("amountOfTransactions")
    private int amountOfTransactions;

    private LocalDate dateCreated;


    public void setData(){
        if(this.dateCreated != null) return;
        this.dateCreated = LocalDate.parse(this.created);
    }

    public int getAmountOfTransactions() {
        return amountOfTransactions;
    }

    public void setAmountOfTransactions(int amountOfTransactions) {
        this.amountOfTransactions = amountOfTransactions;
    }

    public String toString(){
        return userName + " / " + password +  "/ " + created + " / " +  amountOfTransactions;
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
