package com.example.oversighttest.entities;

import java.util.HashMap;

public class SpendingPlan {
    private HashMap<Category, Integer> plan;

    public SpendingPlan(HashMap<Category, Integer> plan){
        this.plan = plan;
    }

    public SpendingPlan(){
        this.plan = new HashMap<Category, Integer>();
    }

    public HashMap<Category, Integer> getPlan() {
        return plan;
    }

    public void setPlan(HashMap<Category, Integer> plan) {
        this.plan = plan;
    }
}
