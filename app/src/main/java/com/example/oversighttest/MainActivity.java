package com.example.oversighttest;

import androidx.appcompat.app.AppCompatActivity;
//import com.example.overesighttest.databinding.ActivityMainBinding;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {


    //private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(R.layout.activity_main);
    }
}