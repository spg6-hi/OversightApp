package com.example.oversighttest.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.oversighttest.R;
import com.example.oversighttest.entities.Session;
import com.example.oversighttest.entities.SpendingPlan;
import com.example.oversighttest.entities.Transaction;
import com.example.oversighttest.entities.User;
import com.example.oversighttest.network.NetworkCallback;
import com.example.oversighttest.network.NetworkManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TransactionsPage tp;
    private SpendingPlanPage sp;
    private BankPage bp;
    private FloatingActionButton mAccountButton;

    public static final String SHARED_PREFS = "Shared preferences";
    public static final String USER = "userName";
    public static final String PASSWORD = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Session session = Session.getInstance();
        User loggedIn = session.getLoggedIn();

        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);
        AccountButton();
        //Three main pages; transactions, spending plan, and bank account
        tp = new TransactionsPage();
        sp = new SpendingPlanPage();
        bp = new BankPage();
        PageAdapter tabManager = new PageAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        tabManager.addFragment(tp, "Transactions");
        tabManager.addFragment(sp, "Spending Plan");
        tabManager.addFragment(bp, "Bank");
        viewPager.setAdapter(tabManager);
        NetworkManager nm = NetworkManager.getInstance(this.getApplicationContext());
        nm.getTransactionsForMonth(loggedIn, YearMonth.now(),new NetworkCallback<List<Transaction>>() {
            @Override
            public void onSuccess(List<Transaction> result) {
                session.setTransactions(new ArrayList<>(result));
                tp.loadData();
            }

            @Override
            public void onFailure(String errorString) {
            }
        });
        nm.getSpendingPlan(loggedIn, new NetworkCallback<SpendingPlan>() {
            @Override
            public void onSuccess(SpendingPlan result) {
                session.setSpendingPlan(result);
                tp.loadData();
            }

            @Override
            public void onFailure(String errorString) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Session s = Session.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER, s.getLoggedIn().getUserName());
        editor.putString(PASSWORD, s.getLoggedIn().getPassword());
        editor.apply();
    }

    private void AccountButton() {
        mAccountButton = findViewById(R.id.mAccount);
        mAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplication(), AccountActivity.class);
                startActivity(i);
            }
        });
    }


    public static Intent newIntent(Context packageContext){
        Intent i = new Intent(packageContext, MainActivity.class);
        return i;
    }


}