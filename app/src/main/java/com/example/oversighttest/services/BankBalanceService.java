package com.example.oversighttest.services;

import com.example.oversighttest.network.DummyNetwork;

public class BankBalanceService {

    DummyNetwork dm;

    public BankBalanceService(DummyNetwork dm){
        this.dm = dm;
    }

    public int getBankBalance(){
        return dm.getBankBalance();
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
