package com.example.oversighttest.entities;

import java.util.ArrayList;
import java.util.Random;

public enum Category {
    CARS("Cars & transportation"),
    CHILDREN("Children"),
    EDUCATION("Education"),
    FINES("Fines & Fees"),
    FOOD("Food"),
    HEALTH("health & Beauty"),
    HOME("Home"),
    INSURANCE("Insurance"),
    INVESTMENTS("Investments & Savings"),
    LEISURE("Leisure"),
    MISC("Miscellaneous"),
    SHOPPING("Shopping & Services"),
    TRAVEL("Vacation and Travel");


    private final String displayName;

    Category(final String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }

    public static Category getRandomCategory(){
        Category[] values = Category.values();
        int length = values.length;
        int randIndex = new Random().nextInt(length);
        return values[randIndex];
    }

    public static ArrayList<Category> getCategories(){
        ArrayList<Category> cats = new ArrayList<>();
        Category[] vals = Category.values();
        for (int i = 0; i<vals.length; i++){
            cats.add(vals[i]);
        }
        return cats;
    }

    public String toString(){
        return this.displayName;
    }

}