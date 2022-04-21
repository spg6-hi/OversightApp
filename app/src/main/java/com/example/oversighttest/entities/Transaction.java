package com.example.oversighttest.entities;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class Transaction {

    @SerializedName("id")
    private long ID;

    @SerializedName("amount")
    private int amount;

    @SerializedName("category")
    private Category category;

    //So that you can send the date through the restAPI, for some reason it doesn't allow LocalDates
    @SerializedName("date")
    private String dateString;

    private LocalDate dateOfPurchase;



    public Transaction(int amount, Category category, LocalDate date, Long id){
        this.amount = amount;
        this.category = category;
        this.dateOfPurchase = date;
        this.ID = id;
    }

    public Transaction(int amount, Category category, LocalDate date){
        this.amount = amount;
        this.category = category;
        this.dateOfPurchase = date;
    }

    public void setData(){
        this.dateOfPurchase = LocalDate.parse(this.dateString);
    }

    public void setId(long ID){this.ID = ID;}

    public long getID(){return this.ID;}

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return dateOfPurchase;
    }

    public void setDate(LocalDate date) {
        this.dateOfPurchase = date;
    }

    public String toString(){
        return this.dateOfPurchase + " : " + this.category + " : " + this.amount + " : " + this.ID;
    }
}
