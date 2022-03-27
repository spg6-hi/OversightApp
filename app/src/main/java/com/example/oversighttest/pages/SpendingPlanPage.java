package com.example.oversighttest.pages;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.example.oversighttest.R;
import com.example.oversighttest.entities.Category;
import com.example.oversighttest.entities.Transaction;
import com.example.oversighttest.entities.SpendingPlan;
import com.example.oversighttest.network.DummyNetwork;
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
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


public class SpendingPlanPage extends Fragment {

    //request codes
    private static final int CREATE_SPENING_PLAN = 0;
    private static final int DELETE_SPENDING_PLAN = 1;
    private static final int CHANGE_SPENDING_PLAN = 2;

    private static DummyNetwork network;

    private PieChart pieChart;
    private View v;
    private ArrayList<Transaction> listSpendingPlan = new ArrayList<>();
    private SpendingPlan spendingPlan;
    private boolean spendingPlanExists;

    private FloatingActionButton fab;
    private ExtendedFloatingActionButton fabone, fabtwo, fabthree;
    private Float translationYaxis = 100f;
    private Boolean menuOpen = false;
    private OvershootInterpolator interpolator = new OvershootInterpolator();

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button newcontantpopup_cancel, newcontantpopup_save;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get access to network
        MainActivity a = (MainActivity) getActivity();
        network = a.getDm();

        //get spending plan
        spendingPlan = network.getSpendingPlan();

        //skítafix til að legend a piechart sjáist
        LocalDate date = LocalDate.now();

        for ( Map.Entry<Category, Integer> entry : spendingPlan.getPlan().entrySet()){
            listSpendingPlan.add(new Transaction(entry.getValue(), entry.getKey(), date));
        }
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
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Budget by Category");
        pieChart.setCenterTextSize(24);
        pieChart.setCenterTextColor(Color.WHITE);
        pieChart.getDescription().setEnabled(false);
        pieChart.setRotationEnabled(false);
        pieChart.setDrawEntryLabels(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);

    }

    /**
     * load data from spending plan for pie chart to display
     */
    private void loadPieChartData() {
        LocalDate date = LocalDate.now();
        listSpendingPlan = new ArrayList<Transaction>();
        for ( Map.Entry<Category, Integer> entry : spendingPlan.getPlan().entrySet()){
            listSpendingPlan.add(new Transaction(entry.getValue(), entry.getKey(), date));
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Transaction t : listSpendingPlan) {
            entries.add(new PieEntry(t.getAmount(), t.getCategory().getDisplayName()));

        }


        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }
        for (int color : ColorTemplate.VORDIPLOM_COLORS) {
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

    private void ShowMenu() {
        fab = v.findViewById(R.id.fab);
        fabone = v.findViewById(R.id.fab_one);
        fabtwo = v.findViewById(R.id.fab_two);
        fabthree = v.findViewById(R.id.fab_three);

        fabone.setAlpha(0f);
        fabtwo.setAlpha(0f);
        fabthree.setAlpha(0f);

        fabone.setTranslationY(translationYaxis);
        fabtwo.setTranslationY(translationYaxis);
        fabthree.setTranslationY(translationYaxis);

        ConstraintLayout spendinglayout = (ConstraintLayout) v.findViewById(R.id.frameLayout);

        spendinglayout.setOnClickListener(new View.OnClickListener() {
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
                closeFab();
                if(spendingPlanExists) {
                    Toast.makeText(getActivity(),"Spending Plan Already Exists",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(getActivity(), CreateSpendingPlanPage.class);

                    startActivityForResult(intent, CREATE_SPENING_PLAN);
                }
            }
        });

        fabtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFab();
                if(spendingPlanExists) {
                    Intent intent = new Intent(getActivity(), DeleteSpendingPlanPage.class);

                    startActivityForResult(intent, DELETE_SPENDING_PLAN);
                }
                else {
                    Toast.makeText(getActivity(),"No Spending Plan To Delete",Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFab();
                if(spendingPlanExists) {
                    Intent intent = new Intent(getActivity(), EditSpendingPlanPage.class);

                    startActivityForResult(intent, CHANGE_SPENDING_PLAN);
                }
                else {
                    Intent intent = new Intent(getActivity(), CreateSpendingPlanPage.class);

                    startActivityForResult(intent, CREATE_SPENING_PLAN);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == CREATE_SPENING_PLAN || requestCode == CREATE_SPENING_PLAN){
                if (data != null){
                    spendingPlanExists = true;
                    HashMap<Category, Integer> plan = (HashMap<Category, Integer>)data.getExtras().getSerializable("new spending plan");
                    spendingPlan = new SpendingPlan(plan);
                    network.setSpendingPlan(spendingPlan);
                    loadPieChartData();
                }
            }
            else if(requestCode == DELETE_SPENDING_PLAN){
                spendingPlanExists = false;
                network.setSpendingPlan(new SpendingPlan(new HashMap<Category, Integer>()));
                spendingPlan = new SpendingPlan(new HashMap<Category, Integer>());
                loadPieChartData();
                System.out.println("okei ég eyddi því");
            }
        }
    }

    private void closeFab(){
        menuOpen = !menuOpen;
        fab.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
        fabone.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabtwo.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabthree.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void openFab(){
        menuOpen = !menuOpen;
        fab.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        fabone.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabtwo.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabthree.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
    }
}