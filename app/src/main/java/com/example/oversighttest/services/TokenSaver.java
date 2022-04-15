package com.example.oversighttest.services;


import android.content.Context;
import android.content.SharedPreferences;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TokenSaver {
    private final static String SHARED_PREF_NAME = "com.example.oversighttest.SHARED_PREF_NAME";
    private final static String TOKEN_KEY = "com.example.oversighttest.TOKEN_KEY";

    private static final Base64.Encoder base64Encoder = Base64.getEncoder();

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

    public static String generateUserToken(String userEmail, String userPassword) {
        String combined = userEmail + "%" + userPassword; // we can use "%" because "%" is an illegal character in an email
        return base64Encoder.encodeToString(combined.getBytes(StandardCharsets.UTF_8));
    }

}