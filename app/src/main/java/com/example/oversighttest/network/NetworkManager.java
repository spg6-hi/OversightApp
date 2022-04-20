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
import com.example.oversighttest.entities.BankAccount;
import com.example.oversighttest.entities.Category;
import com.example.oversighttest.entities.SpendingPlan;
import com.example.oversighttest.entities.Transaction;
import com.example.oversighttest.entities.User;
import com.example.oversighttest.pages.BankPage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

    //USER STUFF

    /**
     * Logs in a user
     */
    public void loginUser(String userName, String password, final NetworkCallback<User> callback) {
        String url = BASE_URL + "loginUser";

        StringRequest request = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type type = new TypeToken<User>() {
                }.getType();
                User user = gson.fromJson(response, type);
                callback.onSuccess(user);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userName", userName);
                params.put("password", password);
                System.out.println(params);
                return params;
            }
        };
        mQueue.add(request);
    }

    /**
     * creates a user
     */
    public void createUser(String userName, String password, boolean generateData,final NetworkCallback<User> callback){
            String url = BASE_URL + "createAppUser";

            StringRequest request = new StringRequest(
                    Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println(response);
                    Gson gson = new Gson();
                    Type type = new TypeToken<User>() {
                    }.getType();
                    User user = gson.fromJson(response, type);
                    System.out.println(user);
                    user.setData();
                    callback.onSuccess(user);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onFailure(error.toString());
                }
            }
            ){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("userName", userName);
                    params.put("password", password);
                    if (generateData){
                        params.put("generate", "yes");
                    }
                    else{
                        params.put("generate", "no");
                    }
                    System.out.println(params);
                    return params;
                }
            };
        mQueue.add(request);
    }

    public void changePassword(String userName, String oldPass, String newPass, final NetworkCallback<User> callback){
        String url = BASE_URL +  "changeAppPassword";

        StringRequest request = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                Gson gson = new Gson();
                Type type = new TypeToken<User>() {
                }.getType();
                User user = gson.fromJson(response, type);
                System.out.println(user);
                if(user != null) {
                    callback.onSuccess(user);
                }
                else {
                    callback.onFailure("fail");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userName", userName);
                params.put("oldPassword", oldPass);
                params.put("newPassword", newPass);
                System.out.println(params);
                return params;
            }
        };
        mQueue.add(request);
    }

    public void deleteUser(String userName, String pass, String confirmPass, final NetworkCallback<String> callback){
        String url = BASE_URL +  "deleteAppUser";

        StringRequest request = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("true")) {
                    callback.onSuccess("deleted");
                }
                else {
                    callback.onFailure("fail");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userName", userName);
                params.put("password", pass);
                params.put("confirmPassword", confirmPass);
                System.out.println(params);
                return params;
            }
        };
        mQueue.add(request);
    }

    //TRANSACTION STUFF

    /**
     * get all transactions for logged in user
     * @param user logged in user
     * @param callback
     */
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

    /**
     * get all transactions for logged in user
     * @param user logged in user
     * @param callback
     */
    public void getTransactionsForMonth(User user, YearMonth month, NetworkCallback<List<Transaction>> callback){
        String url = BASE_URL + "getTransactionsForMonth";
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
                params.put("year", Integer.toString(month.getYear()));
                params.put("month", Integer.toString(month.getMonth().getValue()));
                System.out.println(params);
                return params;
            }
        };
        mQueue.add(request);
    }



    /**
     * get all transactions for logged in user
     * @param user logged in user
     * @param days how many days back to go
     * @param callback
     */
    public void getTransactionsForDays(User user, int days, NetworkCallback<List<Integer>> callback){
        String url = BASE_URL + "getTransactionsForDays";
        System.out.println("CALLING URL " + url);
        StringRequest request = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Integer>>(){}.getType();
                List<Integer> integers = gson.fromJson(response, listType);
                callback.onSuccess(integers);
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
                params.put("days", Integer.toString(days));
                System.out.println(params);
                return params;
            }
        };
        mQueue.add(request);
    }

    /**
     * Creates a new transaction for the logged in user
     * @param user logged in user
     * @param transaction transaction to be created
     * @param callback
     */
    public void createTransaction(User user, Transaction transaction, YearMonth month, NetworkCallback<List<Transaction>> callback){
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
                params.put("year", Integer.toString(month.getYear()));
                params.put("month", Integer.toString(month.getMonth().getValue()));
                if (transaction.getCategory() != null){
                    params.put("category", transaction.getCategory().getName());
                }
                System.out.println(params);
                return params;
            }
        };
        mQueue.add(request);
    }


    public void deleteTransaction(long id, YearMonth month, User user, NetworkCallback<List<Transaction>> callback){
        String url = BASE_URL + "deleteTransaction";
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
                params.put("id", Long.toString(id));
                params.put("userName", user.getUserName());
                params.put("year", Integer.toString(month.getYear()));
                params.put("month", Integer.toString(month.getMonth().getValue()));
                System.out.println(params);
                return params;
            }
        };
        mQueue.add(request);
    }



    //SPENDING PLAN STUFF

    /**
     * Creates a new spendingplan for the logged in user
     * @param user logged in user
     * @param sp spending plan to be created
     * @param callback
     */
    public void createSpendingPlan(User user, SpendingPlan sp, NetworkCallback<SpendingPlan> callback){
        String url = BASE_URL + "createAppSpendingPlan";
        System.out.println("CALLING URL " + url);
        StringRequest request = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type type = new TypeToken<SpendingPlan>() {
                }.getType();
                SpendingPlan sp = gson.fromJson(response, type);
                callback.onSuccess(sp);
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

                Iterator hm = sp.getPlan().entrySet().iterator();

                while (hm.hasNext()){
                    Map.Entry element = (Map.Entry)hm.next();
                    Category c = (Category)element.getKey();
                    String key = c.getName();
                    int value = (int)element.getValue();
                    String val = Integer.toString(value);
                    params.put(key, val);
                }

                System.out.println(params);
                return params;
            }
        };
        mQueue.add(request);
    }

    public void deleteSpendingPlan(User user, NetworkCallback<SpendingPlan> callback){
        String url = BASE_URL + "deleteSpendingPlan";
        System.out.println("CALLING URL " + url);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(new SpendingPlan());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userName", user.getUserName());
                params.put("password", user.getPassword());
                return params;
            }
        };
        mQueue.add(request);
    }

    /**
     * gets spending plan of logged in user
     * @param user logged in user
     * @param callback
     */
    public void getSpendingPlan(User user, NetworkCallback<SpendingPlan> callback){
        String url = BASE_URL + "getSpendingPlan";
        System.out.println("CALLING URL " + url);
        StringRequest request = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type type = new TypeToken<SpendingPlan>() {
                }.getType();
                SpendingPlan sp = gson.fromJson(response, type);
                callback.onSuccess(sp);

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
                return params;
            }
        };
        mQueue.add(request);
    }


    public void addBalance(int added, User user, NetworkCallback<BankAccount> callback){
        String url = BASE_URL+"addFunds";
        System.out.println("CALLING URL " + url);
        StringRequest request = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type type = new TypeToken<BankAccount>() {
                }.getType();
                BankAccount b = gson.fromJson(response, type);
                callback.onSuccess(b);

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
                params.put("added", Integer.toString(added));
                return params;
            }
        };
        mQueue.add(request);
    }

    public void getBalance(User user, NetworkCallback<BankAccount> callback){
        String url = BASE_URL+"getBankAccount";
        System.out.println("CALLING URL " + url);
        StringRequest request = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type type = new TypeToken<BankAccount>() {
                }.getType();
                BankAccount b = gson.fromJson(response, type);
                callback.onSuccess(b);

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
                return params;
            }
        };
        mQueue.add(request);
    }


    public void removeBalance(int removed, User user, NetworkCallback<BankAccount> callback){
        String url = BASE_URL+"removeFunds";
        System.out.println("CALLING URL " + url);
        StringRequest request = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type type = new TypeToken<BankAccount>() {
                }.getType();
                BankAccount b = gson.fromJson(response, type);
                callback.onSuccess(b);

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
                params.put("removed", Integer.toString(removed));
                return params;
            }
        };
        mQueue.add(request);
    }

}