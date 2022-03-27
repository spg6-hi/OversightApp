package com.example.oversighttest.services;
import com.example.oversighttest.entities.Transaction;
import com.example.oversighttest.network.DummyNetwork;
import com.example.oversighttest.entities.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransactionService {
    DummyNetwork dm;
    private Transaction t;
    private List<Transaction> mTransactionList;

    public TransactionService(DummyNetwork dm){
        this.dm = dm;
    }

    public Transaction addTransaction(Transaction t){
        return dm.createTransaction(t);
    }
    //todo seeTransactions(User user)
    public ArrayList<Transaction> seeTransactions(){
        return dm.getTransactions();
    }
    public void deleteTransaction(Transaction t){
        dm.deleteTransaction(t);
    }
}
