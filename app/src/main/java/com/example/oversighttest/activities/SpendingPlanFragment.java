package com.example.oversighttest.activities;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.example.oversighttest.R;
import com.example.oversighttest.entities.Category;
import com.example.oversighttest.entities.Session;
import com.example.oversighttest.entities.Transaction;
import com.example.oversighttest.entities.SpendingPlan;
import com.example.oversighttest.entities.User;
import com.example.oversighttest.network.NetworkCallback;
import com.example.oversighttest.network.NetworkManager;
import com.example.oversighttest.services.SpendingPlanService;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class SpendingPlanFragment extends Fragment {

    //request codes
    private static final int CREATE_SPENDING_PLAN = 0;
    private static final int DELETE_SPENDING_PLAN = 1;
    private static final int CHANGE_SPENDING_PLAN = 2;

    private NetworkManager nm;

    private PieChart pieChart;
    private View v;
    private ArrayList<Transaction> listSpendingPlan = new ArrayList<>();
    private SpendingPlan spendingPlan;
    private boolean spendingPlanExists;
    private SpendingPlanService ss;

    private FloatingActionButton fab;
    private ExtendedFloatingActionButton fabone, fabtwo, fabthree;
    private Float translationYaxis = 100f;
    private Boolean menuOpen = false;
    private OvershootInterpolator interpolator = new OvershootInterpolator();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        spendingPlan = new SpendingPlan();
        ss = new SpendingPlanService();
        User loggedIn = Session.getInstance().getLoggedIn();

        nm = NetworkManager.getInstance(this.getContext());

        nm.getSpendingPlan(loggedIn, new NetworkCallback<SpendingPlan>() {
            @Override
            public void onSuccess(SpendingPlan result) {
                Session.getInstance().setSpendingPlan(result);
                spendingPlan = result;
                spendingPlanExists = true;
                setupPieChart();
                loadPieChartData();
            }

            @Override
            public void onFailure(String errorString) {
            }
        });

        spendingPlanExists = (!spendingPlan.getPlan().isEmpty());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spending_plan_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v = getView();
        pieChart = v.findViewById(R.id.pieChart);
        ShowMenu();
        setupPieChart();
        loadPieChartData();


    }


    /**
     * set up pie chart look
     */
    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.rgb(34, 34, 34));
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(20);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Budget by Category");
        pieChart.setCenterTextSize(24);
        pieChart.setCenterTextColor(Color.WHITE);
        pieChart.getDescription().setEnabled(false);
        pieChart.setRotationEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.animateXY(1000,1000);
        /*
        TODO: FIX legend
         */
        Legend l = pieChart.getLegend();
        l.setTextSize(10f);
        l.setTextColor(Color.WHITE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);

    }

    /**
     * load data from spending plan for pie chart to display
     */
    private void loadPieChartData() {
        LocalDate date = LocalDate.now();
        spendingPlan = Session.getInstance().getSpendingPlan();
        if(spendingPlan == null) {
            spendingPlan = new SpendingPlan();
        }
        listSpendingPlan = new ArrayList<Transaction>();
        /*
        for ( Map.Entry<Category, Integer> entry : spendingPlan.getPlan().entrySet()){
            listSpendingPlan.add(new Transaction(entry.getValue(), entry.getKey(), date));
        }
         */


        ArrayList<PieEntry> entries = new ArrayList<>();
        Iterator map = spendingPlan.getPlan().entrySet().iterator();
        while (map.hasNext()) {
            Map.Entry entry = (Map.Entry) map.next();
            int amount = (int) entry.getValue();
            String cat = ((Category) entry.getKey()).getDisplayName();
            if (amount != 0) {
                entries.add(new PieEntry(amount, cat));
            }
        }

        //set colours
        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        for (int color: ColorTemplate.COLORFUL_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expense Category");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);

        pieChart.setData(data);
        pieChart.invalidate();
    }

    //Floating action buttons
    private void ShowMenu() {
        fab = v.findViewById(R.id.mAccountFab);
        fabone = v.findViewById(R.id.mDeleteAccount);
        fabtwo = v.findViewById(R.id.mChangePassword);
        fabthree = v.findViewById(R.id.mLogOut);

        fabone.setAlpha(0f);
        fabtwo.setAlpha(0f);
        fabthree.setAlpha(0f);

        fabone.setTranslationY(translationYaxis);
        fabtwo.setTranslationY(translationYaxis);
        fabthree.setTranslationY(translationYaxis);

        ConstraintLayout spendingLayout = (ConstraintLayout) v.findViewById(R.id.frameLayout);

        spendingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFab();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menuOpen) {
                    closeFab();
                } else {
                    openFab();
                }
            }
        });

        fabone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!menuOpen) return;
                closeFab();
                if (spendingPlanExists) {
                    Toast.makeText(getActivity(), "Spending Plan Already Exists", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), CreateSpendingPlanActivity.class);

                    startActivityForResult(intent, CREATE_SPENDING_PLAN);
                }
            }
        });

        fabtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!menuOpen) return;
                closeFab();
                if (spendingPlanExists) {
                    Intent intent = new Intent(getActivity(), DeleteSpendingPlanActivity.class);

                    startActivityForResult(intent, DELETE_SPENDING_PLAN);
                } else {
                    Toast.makeText(getActivity(), "No Spending Plan To Delete", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!menuOpen) return;
                closeFab();
                if (spendingPlanExists) {
                    Intent intent = new Intent(getActivity(), EditSpendingPlanActivity.class);

                    startActivityForResult(intent, CHANGE_SPENDING_PLAN);
                } else {
                    Intent intent = new Intent(getActivity(), CreateSpendingPlanActivity.class);

                    startActivityForResult(intent, CREATE_SPENDING_PLAN);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CREATE_SPENDING_PLAN) {
                if (data != null) {
                    createSpendingPlan(data);
                }
            } else if (requestCode == CHANGE_SPENDING_PLAN) {
                if (data != null) {
                    changeSpendingPlan(data);
                }

            } else if (requestCode == DELETE_SPENDING_PLAN) {
                deleteSpendingPlan();
            }
        }
    }

    private void createSpendingPlan(Intent data) {
        spendingPlanExists = true;
        HashMap<Category, Integer> plan = (HashMap<Category, Integer>) data.getExtras().getSerializable("new spending plan");

        saveSpendingPlan(plan);
    }

    private void changeSpendingPlan(Intent data) {
        spendingPlanExists = true;
        HashMap<Category, Integer> plan = (HashMap<Category, Integer>) data.getExtras().getSerializable("edited spending plan");

        saveSpendingPlan(plan);
    }

    private void deleteSpendingPlan() {
        spendingPlanExists = false;

        Session s = Session.getInstance();

        s.setSpendingPlan(new SpendingPlan());
        nm.deleteSpendingPlan(s.getLoggedIn(), new NetworkCallback<SpendingPlan>() {
            @Override
            public void onSuccess(SpendingPlan result) {
            }

            @Override
            public void onFailure(String errorString) {
            }
        });
        spendingPlan = new SpendingPlan(new HashMap<Category, Integer>());
        loadPieChartData();
    }

    private void saveSpendingPlan(HashMap<Category, Integer> plan) {
        spendingPlan = new SpendingPlan(plan);
        Session.getInstance().setSpendingPlan(spendingPlan);
        nm.createSpendingPlan(Session.getInstance().getLoggedIn(), spendingPlan, new NetworkCallback<SpendingPlan>() {
            @Override
            public void onSuccess(SpendingPlan result) {
                Session.getInstance().setSpendingPlan(result);
                loadPieChartData();
            }

            @Override
            public void onFailure(String errorString) {
            }
        });

        //network.setSpendingPlan(spendingPlan);
        loadPieChartData();
    }

    //Closes floating action buttons
    private void closeFab() {
        menuOpen = !menuOpen;
        fab.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
        fabone.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabtwo.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabthree.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
    }

    //Opens floating action buttons
    private void openFab() {
        menuOpen = !menuOpen;
        fab.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        fabone.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabtwo.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabthree.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
    }

}