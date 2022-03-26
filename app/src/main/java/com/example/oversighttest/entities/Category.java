package com.example.oversighttest.entities;

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

    public static Category[] getValues(){
        return Category.values();
    }

    public static Category getRandomCategory(){
        Category[] values = Category.values();
        int length = values.length;
        int randIndex = new Random().nextInt(length);
        return values[randIndex];
    }

}