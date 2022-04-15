package com.example.oversighttest.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.oversighttest.R;
import com.example.oversighttest.entities.Category;
import com.example.oversighttest.entities.Session;
import com.example.oversighttest.entities.SpendingPlan;
import com.example.oversighttest.entities.Transaction;
import com.example.oversighttest.entities.User;
import com.example.oversighttest.network.DummyNetwork;
import com.example.oversighttest.network.NetworkCallback;
import com.example.oversighttest.network.NetworkManager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    //this is the dummy network, other activities use this exact network, so that every activity uses the same data
    private static DummyNetwork dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dm = new DummyNetwork();

        Session session = Session.getInstance();
        User loggedIn = session.getLoggedIn();
        System.out.println("Currently logged in: " + loggedIn.getUserName());

        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        //Three main pages; transactions, spending plan, and bank account
        PageAdapter tabManager = new PageAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        tabManager.addFragment(new TransactionsPage(), "Transactions");
        tabManager.addFragment(new SpendingPlanPage(), "Spending Plan");
        tabManager.addFragment(new BankPage(), "Bank");
        viewPager.setAdapter(tabManager);

    }


    public static Intent newIntent(Context packageContext){
        Intent i = new Intent(packageContext, MainActivity.class);
        return i;
    }
}