package com.example.oversighttest.services;

import com.example.oversighttest.entities.Session;
import com.example.oversighttest.entities.SpendingPlan;

public class SpendingPlanService {


    public SpendingPlanService(){

    }

    public SpendingPlan getSpendingPlan(){
        return Session.getInstance().getSpendingPlan();
    }

    public void setSpendingPlan(SpendingPlan spendingPlan) {
        Session.getInstance().setSpendingPlan(spendingPlan);
    }
}
