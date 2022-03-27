package com.example.oversighttest.services;

import com.example.oversighttest.entities.SpendingPlan;
import com.example.oversighttest.network.DummyNetwork;

public class SpendingPlanService {

    DummyNetwork dm;

    public SpendingPlanService(DummyNetwork dm){
        this.dm = dm;
    }

    public SpendingPlan getSpendingPlan(){
        return dm.getSpendingPlan();
    }

    public void setSpendingPlan(SpendingPlan spendingPlan) {
        dm.setSpendingPlan(spendingPlan);
    }
}
