package com.example.oversighttest.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;

import com.example.oversighttest.R;
import com.example.oversighttest.network.DummyNetwork;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    //this is the dummy network, other activities use this exact network, so that every activity uses the same data
    private static DummyNetwork dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dm = new DummyNetwork();

        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        //Three main pages; transactions, spending plan, and bank account
        PageAdapter tabManager = new PageAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        tabManager.addFragment(new TransactionsPage(), "Transactions");
        tabManager.addFragment(new SpendingPlanPage(), "Spending Plan");
        tabManager.addFragment(new BankPage(dm), "Bank");
        viewPager.setAdapter(tabManager);

    }

    /**
     * Allows any activity to access the dummy network
     * @return the dummy network
     */
    public static DummyNetwork getDm() {
        return dm;
    }
}