package com.example.oversighttest.entities;

import java.time.LocalDate;

public class Transaction {

    private int amount;
    private Category category;
    private LocalDate date;
    private long id;

    public Transaction(int amount, Category category, LocalDate date){
        this.amount = amount;
        this.category = category;
        this.date = date;
    }
    public long getId(){return id;}

    public void setId(long id){this.id = id;}

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
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String toString(){
        return this.date + " : " + this.category + " : " + this.amount;
    }
}
