package com.example.oversighttest.network;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.oversighttest.entities.Transaction;
import com.example.oversighttest.entities.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkManager {

    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private static final String TAG = "NetworkManager";
    private static NetworkManager mInstance;
    private static RequestQueue mQueue;
    private Context mContext;

    public static synchronized NetworkManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkManager(context);
        }
        return mInstance;
    }

    private NetworkManager(Context context) {
        mContext = context;
        mQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mQueue;
    }

    public void loginUser(String userToken, final NetworkCallback<User> callback) {
        String url = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath("loginUser")
                .appendPath(userToken)
                .build().toString();

        StringRequest request = new StringRequest(
                Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type type = new TypeToken<User>() {
                }.getType();
                User user = gson.fromJson(response, type);
                System.out.println(user);
                callback.onSuccess(user);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        }
        );
        mQueue.add(request);
    }

    public void createUser(String userToken, final NetworkCallback<User> callback) {
        String url = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath("createAppUser")
                .appendPath(userToken)
                .build().toString();

        StringRequest request = new StringRequest(
                Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                Gson gson = new Gson();
                Type type = new TypeToken<User>() {
                }.getType();
                User user = gson.fromJson(response, type);
                System.out.println(user);
                callback.onSuccess(user);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        }
        );
        mQueue.add(request);
    }


    public void getTransactions(User user, NetworkCallback<List<Transaction>> callback){
        String url = BASE_URL + "getTransactions";
        System.out.println("CALLING URL " + url);
        StringRequest request = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Transaction>>(){}.getType();
                List<Transaction> transactions = gson.fromJson(response, listType);
                callback.onSuccess(transactions);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userName", user.getUserName());
                params.put("password", user.getPassword());
                System.out.println(params);
                return params;
            }
        };
        mQueue.add(request);
    }

    public void createTransaction(User user, Transaction transaction, NetworkCallback<List<Transaction>> callback){
        String url = BASE_URL + "createTransaction";
        System.out.println("CALLING URL " + url);
        StringRequest request = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Transaction>>(){}.getType();
                List<Transaction> transactions = gson.fromJson(response, listType);
                callback.onSuccess(transactions);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userName", user.getUserName());
                params.put("password", user.getPassword());
                params.put("amount", Integer.toString(transaction.getAmount()));
                params.put("date", transaction.getDate().toString());
                params.put("category", transaction.getCategory().getName());
                System.out.println(params);
                return params;
            }
        };
        mQueue.add(request);
    }



    /**
     * test function, useless
     * @param callback
     */
    public void testPost(final NetworkCallback<Boolean> callback){
        System.out.println("testPost");;
        String url = BASE_URL + "testPost";
        System.out.println(url);

        StringRequest request = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("cool");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("fail");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String , String> params = new HashMap<>();
                params.put("userName", "asdf");
                params.put("password",  "asd");
                System.out.println(params);
                return params;
            }
        };
        mQueue.add(request);
    }

}