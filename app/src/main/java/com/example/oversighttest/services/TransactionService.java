package com.example.oversighttest.services;

import android.content.Context;

import com.example.oversighttest.entities.Category;
import com.example.oversighttest.entities.Session;
import com.example.oversighttest.entities.SpendingPlan;
import com.example.oversighttest.entities.Transaction;
import com.example.oversighttest.entities.User;
import com.example.oversighttest.network.DummyNetwork;
import com.example.oversighttest.network.NetworkCallback;
import com.example.oversighttest.network.NetworkManager;
import com.example.oversighttest.pages.TransactionsPage;
import com.google.gson.JsonParser;

import org.json.JSONArray;

import java.io.FileReader;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class TransactionService {
    private Transaction t;
    private ArrayList<Transaction> transactionList;

    public TransactionService(){
    }

    public Transaction addTransaction(Transaction t){
        return t;
    }


    public ArrayList<Transaction> seeTransactions(){
        return Session.getInstance().getTransactions();
    }

    public void parseListResult(String result){
        System.out.println("Hey");
    }

    public void deleteTransaction(Transaction t){
        //TODO
    }

    public void saveTransactions(List<Transaction> t){
        for (Transaction transaction: t){
            transaction.setData();
        }

        ArrayList<Transaction> transactions = new ArrayList<>(t);

        Session s = Session.getInstance();
        s.setTransactions(transactions);
    }


    /**
     * return a sorted list detemined by the code
     * @param code the code that determines how the list is sorted
     * @return a sorted list
     */
    public ArrayList<Transaction> seeSortedTransaction(int code){

        //get all transactions
        Session s = Session.getInstance();
        ArrayList<Transaction> transactions = s.getTransactions();

        //create a treemap, contains an key and the value is an arraylist og transactions
        TreeMap<Object, ArrayList<Transaction>> map = new TreeMap<>();


        if (code == TransactionsPage.SORT_BY_AMOUNT){
            //sort by amount
            for (Transaction t : transactions){
                ArrayList<Transaction> transactionsOfAmount = new ArrayList<>();
                if (map.containsKey(t.getAmount())){
                    //there is another transaction with the same amount
                    //update the arraylist so that it contains all transactions of that amount
                    transactionsOfAmount = map.get(t.getAmount());
                }
                //put the arraylist in the map
                transactionsOfAmount.add(t);
                map.put(t.getAmount(), transactionsOfAmount);
            }
        }
        else if (code == TransactionsPage.SORT_BY_CATEGORY){
            //sort by category
            //this is the same as by amount
            for (Transaction t : transactions){
                ArrayList<Transaction> transactionsOfCategory = new ArrayList<>();
                if (map.containsKey(t.getCategory())){
                    transactionsOfCategory = map.get(t.getCategory());
                }
                transactionsOfCategory.add(t);
                map.put(t.getCategory(), transactionsOfCategory);
            }
        }
        else{
            //sort by date
            //this is the same as by amount
            for (Transaction t : transactions){
                ArrayList<Transaction> transactionsOfDate = new ArrayList<>();
                if (map.containsKey(t.getDate())){
                    transactionsOfDate = map.get(t.getDate());
                }
                transactionsOfDate.add(t);
                map.put(t.getDate(), transactionsOfDate);
            }
        }

        //this is the sorted list
        ArrayList<Transaction> sorted = new ArrayList<>();

        //loop through treemap and for each arraylist in there, we put it in the sorted list
        //the nice thing about treemaps is that they automatically sort the data for you according to the key
        for (Object list: map.values()){
            for (Object o: (ArrayList<Object>)list){
                Transaction t = (Transaction) o;
                if (code == TransactionsPage.SORT_BY_AMOUNT  || code == TransactionsPage.SORT_BY_DATE){
                    sorted.add(0,t);
                }
                else{
                    sorted.add(t);
                }
            }
        }
        return sorted;
    }

    public int[] getTransactionBarChartData(){
        int[] data = new int[Category.getListOfCategories().length-2];
        HashMap<Category, Integer> map = new HashMap<>();
        ArrayList<Transaction> list = Session.getInstance().getTransactions();
        Category[] cats = Category.getValues();
        if (list == null) return data;

        for (Transaction t : list){
            int val = t.getAmount();
            Category c = t.getCategory();
            if (map.containsKey(c)){
                val+=map.get(c);
            }
            map.put(c, val);
        }

        for (int i = 0; i <data.length; i++){
            Category current = cats[i];
            if (map.containsKey(current)){
                data[i] = map.get(current);
            }
        }
        return data;
    }

    public int[] getSpendingPlanBarChartData(){
        int[] data = new int[Category.getListOfCategories().length-2];
        Category[] cats = Category.getValues();
        HashMap<Category, Integer> map = new HashMap<>();
        SpendingPlan sp = Session.getInstance().getSpendingPlan();
        if (sp != null){
            map = sp.getPlan();
        }
        for (int i = 0; i<data.length; i++){
            Category c = cats[i];
            int val = 0;
            if (map.containsKey(c)){
                val = map.get(c);
            }
            data[i] = val;
        }
        return data;
    }
}
