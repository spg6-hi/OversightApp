package com.example.oversighttest.entities;

import android.content.Context;

import com.example.oversighttest.network.NetworkManager;

import java.util.ArrayList;
import java.util.List;


/**
 * This class keeps track of information on the logged in user, so that you can access his data without fetching
 * it from the network each time
 *
 * It is a singleton object and to use it you call : Session.getInstance()
 */
public class Session {

    private static User loggedIn;
    private static Session instance;
    private static ArrayList<Transaction> transactions;
    private static SpendingPlan sp;
    private static BankAccount b;
    private static long toDelete;

    public static synchronized Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public BankAccount getBankAccount(){
        return b;
    }

    public void setBankAccount(BankAccount b) {
        this.b = b;
    }

    public SpendingPlan getSpendingPlan() {
        return sp;
    }

    public void setSpendingPlan(SpendingPlan sp) {
        Session.sp = sp;
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

    public void setToDelete(long id){
        this.toDelete = id;
    }

    public long getToDelete(){
        return this.toDelete;
    }
}
