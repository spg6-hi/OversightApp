package com.example.oversighttest.services;

import android.net.Network;

import com.example.oversighttest.entities.Session;
import com.example.oversighttest.network.DummyNetwork;
import com.example.oversighttest.network.NetworkManager;

public class BankBalanceService {

    DummyNetwork dm;
    NetworkManager nm;

    public BankBalanceService(){}

    public int getBankBalance(){
        return Session.getInstance().getBankAccount().getBalance();
    }

    public void addFunds(int added){
        int balance = dm.getBankBalance();
        balance += added;
        dm.setBankBalance(balance);
    }

    public void removeFunds(int removed){
        int balance = dm.getBankBalance();
        balance -= removed;
        dm.setBankBalance(balance);
    }
}
