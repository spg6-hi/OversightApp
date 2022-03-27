package com.example.oversighttest.network;

import com.example.oversighttest.entities.Category;
import com.example.oversighttest.entities.SpendingPlan;
import com.example.oversighttest.entities.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This is a dummy class and will be replaced by final release
 */
public class DummyNetwork {

    private String userName; //User is only represented as a username
    private ArrayList<Transaction> transactions; //Simply a list of all transactions
    private SpendingPlan spendingPlan; //The spending plan
    private int bankBalance;

    //Data for generating username
    private int leftLimit = 48;
    private int rightLimit = 122;
    private int userNameLength = 10;

    //amount of transactions
    private int maxTransactions = 50;
    private int minTransactions = 30;

    //How much a transaction costs
    private int maxAmount = 10000;
    private int minAmount = 500;

    //Max and min values for categories in spending plan
    private int maxSpendingPlan = 10000;
    private int minSpendingPlan = 1000;

    //max and min bank balance
    private int maxBankBalance = 100000;
    private int minBankBalance = 500;

    /**
     * Randomly generate all data
     */
    public DummyNetwork(){
        generateName();
        generateTransactions();
        generateSpendingPlan();
        generateBankBalance();
    }

    //Getters and setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public SpendingPlan getSpendingPlan() {
        return spendingPlan;
    }

    public void setSpendingPlan(SpendingPlan spendingPlan) {
        this.spendingPlan = spendingPlan;
    }

    public int getBankBalance() {
        return bankBalance;
    }

    public void setBankBalance(int bankBalance) {
        this.bankBalance = bankBalance;
    }

    public Transaction createTransaction(Transaction t){
        this.transactions.add(t);
        return t;
    }

    public void deleteTransaction(Transaction t){
        if (this.transactions.contains(t)){
            this.transactions.remove(t);
        }
    }

    public void updateBankBalance(int amount){
        this.bankBalance += amount;
    }

    /**
     * Generates a random 8 character string that acts as the username
     */
    private void generateName(){
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit+1)
                .filter(i -> (i <= 57 || i >= 65) && (i<=90 || i >= 97))
                .limit(userNameLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        this.userName = generatedString;
    }

    /**
     * Creates a random number of transactions
     */
    private void generateTransactions(){
        this.transactions = new ArrayList<Transaction>();
        int amountOfTransactions = (int)(Math.random()*(maxTransactions-minTransactions)+minTransactions);

        for (int i = 0; i<amountOfTransactions; i++){
            generateTransaction();
        }
    }

    /**
     * creates a single random transaction
     */
    private void generateTransaction(){
        //random amount
        int amount = (int)(Math.random()*(maxAmount-minAmount)+minAmount);

        //random category
        Category category = Category.getRandomCategory();

        //random date between Today and two years ago
        long max = LocalDate.of(2022, 12, 31).toEpochDay();
        long min = LocalDate.of(2021, 1, 1).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(min, max);
        LocalDate date = LocalDate.ofEpochDay(randomDay);

        Transaction t = new Transaction(amount, category, date);

        this.transactions.add(t);;
    }

    /**
     * Creates a random spending plan
     */
    private void generateSpendingPlan(){
        HashMap<Category, Integer> plan = new HashMap<Category, Integer>();
        Category[] categories = Category.values();
        for (Category c : categories){
            int amount = (int)(Math.random()*(maxSpendingPlan-minSpendingPlan)+minSpendingPlan);
            plan.put(c, amount);
        }
        this.spendingPlan = new SpendingPlan(plan);
    }

    /**
     * creates a random bank balance
     */
    private void generateBankBalance(){
        int balance = (int)(Math.random()*(maxBankBalance-minBankBalance)+minBankBalance);
        this.bankBalance = balance;
    }
}
