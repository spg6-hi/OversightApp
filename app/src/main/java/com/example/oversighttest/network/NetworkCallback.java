package com.example.oversighttest.network;

public interface NetworkCallback<T> {

    void onSuccess(T result);

    void onFailure(String errorString);
}