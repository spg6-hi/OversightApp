package com.example.oversighttest.entities;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SpendingPlan {
    @SerializedName("map")
    private HashMap<Category, Integer> plan;

    @SerializedName("ID")
    private long ID;

    public SpendingPlan(HashMap<Category, Integer> plan){
        this.plan = plan;
    }

    public SpendingPlan(){
        this.plan = new HashMap<Category, Integer>();
    }

    public void setID(long id){this.ID = id;}

    public long getID(){
        return this.ID;
    }

    public HashMap<Category, Integer> getPlan() {
        return plan;
    }

    public void setPlan(HashMap<Category, Integer> plan) {
        this.plan = plan;
    }

    public String toString(){
        String s = "";
        Iterator m = this.plan.entrySet().iterator();

        while (m.hasNext()){
            Map.Entry entry = (Map.Entry)m.next();
            s += entry.getKey() + " : " +  entry.getValue() + "\n";
        }
        return s;
    }
}
