package com.example.oversighttest.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
//import com.example.overesighttest.databinding.ActivityMainBinding;
import android.os.Bundle;

import com.example.oversighttest.R;
import com.example.oversighttest.network.DummyNetwork;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;

    private static DummyNetwork dm;

    //private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dm = new DummyNetwork();
        //binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        PageAdapter tabManager = new PageAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        tabManager.addFragment(new TransactionsPage(), "Transactions");
        tabManager.addFragment(new SpendingPlanPage(), "Spending Plan");
        tabManager.addFragment(new BankPage(dm), "Bank");
        viewPager.setAdapter(tabManager);

    }

    public DummyNetwork getDm() {
        return dm;
    }
}