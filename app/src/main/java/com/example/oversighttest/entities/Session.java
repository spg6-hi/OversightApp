package com.example.oversighttest.entities;

import android.content.Context;

import com.example.oversighttest.network.NetworkManager;

import java.util.ArrayList;
import java.util.List;

public class Session {

    private static User loggedIn;
    private static Session instance;
    private static ArrayList<Transaction> transactions;

    public static synchronized Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        Session.transactions = transactions;
    }

    public User getLoggedIn(){
        return loggedIn;
    }

    public void setLoggedIn(User user){
        loggedIn = user;
    }

}
