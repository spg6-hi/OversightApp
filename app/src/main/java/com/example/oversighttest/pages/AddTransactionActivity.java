package com.example.oversighttest.pages;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.oversighttest.R;
import com.example.oversighttest.entities.Category;
import com.example.oversighttest.network.DummyNetwork;
import com.example.oversighttest.services.AddTransactionService;

import java.util.HashMap;

public class AddTransactionActivity extends AppCompatActivity {
    private DummyNetwork network = MainActivity.getDm();

    private AddTransactionService addTransactionService = new AddTransactionService(network);


    // private HashMap<Category, Integer> spendingplan;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);


    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
