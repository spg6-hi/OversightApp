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
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.github.mikephil.charting.renderer.Renderer;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class TransactionsPage extends Fragment {

    private PieChart pieChart;
    private View v;
    private DummyNetwork network;
    private ArrayList<Transaction> transactions;
    private HashMap<String, Integer> pieEntries;
    private Renderer r;
    ArrayList<Integer> colors;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity a = (MainActivity) getActivity();
        network = a.getDm();
        transactions = network.getTransactions();
        pieEntries = new HashMap<>();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transactions_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v =  getView();
        pieChart = (PieChart) v.findViewById(R.id.pieChart);
        r = pieChart.getRenderer();
        colors = new ArrayList<>();
        setupPieChart();
        loadPieChartData();
    }


    private void setupPieChart(){
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.rgb(34, 34, 34));
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Spending by Category");
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


    private void deleteTransaction(Transaction t){
        PieEntry entryToChange = new PieEntry(pieEntries.get(t.getCategory().getDisplayName()), t.getCategory().getDisplayName());
        transactions.remove(t);
        PieDataSet ds = (PieDataSet) pieChart.getData().getDataSet();
        ArrayList<PieEntry> entries = new ArrayList<>(ds.getValues());
        entries.remove(entryToChange);
        entryToChange = new PieEntry(((int) entryToChange.getValue())-t.getAmount(), entryToChange.getLabel());
        entries.add(entryToChange);
        pieEntries.put(entryToChange.getLabel(), (int) entryToChange.getValue());
        network.setTransactions(transactions);
        updatePieChart(entries);
    }

    private void updatePieChart(ArrayList<PieEntry> entries){
        PieDataSet dataSet = new PieDataSet(entries, "Expense Category");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);

        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void  loadPieChartData(){
        ArrayList<PieEntry> entries = new ArrayList<>();

        Collections.sort(transactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                String t1Cat = t1.getCategory().getDisplayName();
                String t2Cat = t2.getCategory().getDisplayName();
                return t1Cat.compareTo(t2Cat);
            }
        });

        String currCategory = transactions.get(0).getCategory().getDisplayName();
        int currValue = 0;

        //adds all Transactions from network to pieChart
        for (Transaction t : transactions) {
            //makes single PieEntry for each category to prevent duplicates
            if (!t.getCategory().getDisplayName().equals(currCategory)){
                entries.add(new PieEntry(currValue, currCategory));
                pieEntries.put(currCategory, currValue);
                currValue = t.getAmount();
                currCategory = t.getCategory().getDisplayName();
            } else{
                currValue += t.getAmount();
            }
        }

        for (int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        updatePieChart(entries);
    }
}