package com.example.oversighttest.entities;

import com.google.gson.annotations.SerializedName;

public class BankAccount {
    @SerializedName("ID")
    private long id;

    @SerializedName("balance")
    private int balance;

    public BankAccount(long id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
