package com.example.oversighttest.pages;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oversighttest.R;
import com.example.oversighttest.entities.Category;
import com.example.oversighttest.entities.Transaction;
import com.example.oversighttest.network.DummyNetwork;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


public class SpendingPlanPage extends Fragment {

    private static DummyNetwork network;
    private PieChart pieChart;
    private View v;
    private HashMap<Category, Integer> spendingPlan;
    private ArrayList<Transaction> listSpendingPlan = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity a = (MainActivity) getActivity();
        network = a.getDm();
        spendingPlan = network.getSpendingPlan();

        //skítafix til að legend a piechart sjáist
        long max = LocalDate.of(2022, 12, 31).toEpochDay();
        long min = LocalDate.of(2021, 1, 1).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(min, max);
        LocalDate date = LocalDate.ofEpochDay(randomDay);

        for ( Map.Entry<Category, Integer> entry : spendingPlan.entrySet()){
            listSpendingPlan.add(new Transaction(entry.getValue(), entry.getKey(), date));
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spending_plan_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        v = getView();
        pieChart = v.findViewById(R.id.pieChart);

        setupPieChart();
        loadPieChartData(spendingPlan);
    }

    private void setupPieChart(){
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

    private void  loadPieChartData(HashMap<Category, Integer> spendingPlan){
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Transaction t : listSpendingPlan) {
            entries.add(new PieEntry(t.getAmount(), t.getCategory().getDisplayName()));
        }


        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }
        for (int color: ColorTemplate.VORDIPLOM_COLORS){
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
}