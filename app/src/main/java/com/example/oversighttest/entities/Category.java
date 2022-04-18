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

    public String getName(){
        return this.name();
    }


    /**
     * skilar lista af öllum categories
     * @return listi af öllum categories
     */
    public static Category[] getValues(){
        return Category.values();
    }

    /**
     * gefur random category
     * @return random category
     */
    public static Category getRandomCategory(){
        Category[] values = Category.values();
        int length = values.length;
        int randIndex = new Random().nextInt(length);
        return values[randIndex];
    }

    /**
     * SKilar arraylist af categories
     * @return
     */
    public static ArrayList<Category> getCategories(){
        ArrayList<Category> cats = new ArrayList<>();
        Category[] vals = Category.values();
        for (int i = 0; i<vals.length; i++){
            cats.add(vals[i]);
        }
        return cats;
    }

    public static String[] getListOfCategories(){
        String[] s = new String[Category.values().length+2];
        s[0] = "";
        s[Category.values().length+1] = "";
        Category[] list = Category.values();
        for (int i = 1; i<=Category.values().length; i++){
            s[i] = list[i-1].displayName;
        }
        return s;
    }

    /**
     * Bara til að prenta út nafnið aupveldlega
     * @return
     */
    public String toString(){
        return this.displayName;
    }
}