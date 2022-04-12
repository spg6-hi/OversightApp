package com.example.oversighttest.services;


import android.content.Context;
import android.content.SharedPreferences;

public class TokenSaver {
    private final static String SHARED_PREF_NAME = "com.example.oversighttest.SHARED_PREF_NAME";
    private final static String TOKEN_KEY = "com.example.oversighttest.TOKEN_KEY";

    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(TOKEN_KEY, "");
    }

    public static void setToken(Context context, String token) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }
}