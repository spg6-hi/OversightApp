package com.example.oversighttest.entities;

public class Session {

    private static User loggedIn;

    public static User getLoggedIn(){
        return loggedIn;
    }

    public static void setLoggedIn(User user){
        loggedIn = user;
    }
}
