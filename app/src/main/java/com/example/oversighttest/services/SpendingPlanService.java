package com.example.oversighttest.services;

import com.example.oversighttest.entities.Category;
import com.example.oversighttest.network.DummyNetwork;

import java.util.HashMap;

public class SpendingPlanService {

    DummyNetwork dm;

    public SpendingPlanService(DummyNetwork dm){
        this.dm = dm;
    }

    public HashMap<Category, Integer> getSpendingPlan(){
        return dm.getSpendingPlan();
    }

    public void setSpendingPlan(HashMap<Category, Integer> spendingPlan) {
        dm.setSpendingPlan(spendingPlan);
    }
}
