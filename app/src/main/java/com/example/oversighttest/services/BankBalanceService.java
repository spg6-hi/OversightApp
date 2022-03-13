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

    public void addFunds(){
        int balance = dm.getBankBalance();
        balance += 1000;
        dm.setBankBalance(balance);
    }

    public void removeFunds(){
        int balance = dm.getBankBalance();
        balance -= 1000;
        dm.setBankBalance(balance);
    }

}
