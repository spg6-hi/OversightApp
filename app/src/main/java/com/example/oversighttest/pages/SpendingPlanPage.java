package com.example.oversighttest.pages;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;

import com.example.oversighttest.R;
import com.example.oversighttest.entities.Category;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SpendingPlanPage extends Fragment {

    private static DummyNetwork network;
    private PieChart pieChart;
    private View v;
    private HashMap<Category, Integer> spendingPlan;

    private FloatingActionButton fab;
    private ExtendedFloatingActionButton fabone, fabtwo, fabthree;
    private Float translationYaxis = 100f;
    private Boolean menuOpen = true;
    private OvershootInterpolator interpolator = new OvershootInterpolator();

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button newcontantpopup_cancel, newcontantpopup_save;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity a = (MainActivity) getActivity();
        network = a.getDm();
        spendingPlan = network.getSpendingPlan();

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
        loadPieChartData(spendingPlan);
    }

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

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);

    }

    private void loadPieChartData(HashMap<Category, Integer> spendingPlan) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<Category, Integer> entry : spendingPlan.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menuOpen) {
                    menuOpen = !menuOpen;
                    fab.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    fabone.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
                    fabtwo.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
                    fabthree.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
                } else {
                    menuOpen = !menuOpen;
                    fab.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                    fabone.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                    fabtwo.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                    fabthree.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                }
            }
        });

        fabone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createContactDialog();
            }
        });
    }

    public void createContactDialog() {
        dialogBuilder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_DeviceDefault_NoActionBar);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.createspendingplan_popup, null);

        newcontantpopup_save = (Button) contactPopupView.findViewById(R.id.saveButton);
        newcontantpopup_cancel = (Button) contactPopupView.findViewById(R.id.cancelButton);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        newcontantpopup_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Define save button
                dialog.dismiss();
            }
        });

        newcontantpopup_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Define cancel button
                dialog.dismiss();
            }
        });
    }
}