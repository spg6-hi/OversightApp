package com.example.oversighttest.pages;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.oversighttest.R;
import com.example.oversighttest.adapters.RecyclerTransactionAdapter;
import com.example.oversighttest.entities.Category;
import com.example.oversighttest.entities.Transaction;
import com.example.oversighttest.network.DummyNetwork;
import com.example.oversighttest.services.TransactionService;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.renderer.Renderer;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class TransactionsPage extends Fragment {

    private static final int CREATE_TRANSACTION = 0;

    //public static final String EXTRA_CONTACT = ;
    private PieChart pieChart;
    private Button mAddTransaction;
    //private RecyclerView mTransactionList;
    private View v;
    private DummyNetwork network;
    private TransactionService ts;
    private ArrayList<Transaction> transactions;

    private RecyclerView recyclerView;

    private Renderer r;
    //TransactionAdapter adapter;
    //ArrayAdapter<String> arrayAdapter;

    /*public TransactionsPage(Button addTransaction, ListView transactionList) {
        mAddTransaction = addTransaction;
        mTransactionList = transactionList;
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity a = (MainActivity) getActivity();
        final FragmentActivity tPFA = getActivity();

        ts = new TransactionService(a.getDm());

        transactions = ts.seeTransactions();

        // https://stackoverflow.com/questions/26621060/display-a-recyclerview-in-fragment
        /*
            Using a RecyclerView and an Adapter class to display a list of Transactions in the fragment_transactions_page.xml
         */
        final View rootView = inflater.inflate(R.layout.fragment_transactions_page,container, false);
        // 1. get a reference to recyclerView
        recyclerView = (RecyclerView) rootView.findViewById(R.id.mTransactionList);
        // 2. set layoutManager
        recyclerView.setLayoutManager((new LinearLayoutManager(getContext()))); //getContext()
        // 3. create and set the adapter
        //recyclerView.setAdapter(new RecyclerTransactionAdapter(getContext(), transactions));

        setlist();

        /*
            Defining what happens when the mAddTransaction button is pressed in fragment_transactions_page.xml
         */
        mAddTransaction = (Button) rootView.findViewById(R.id.mAddTransaction);
        mAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //using Intent to bind the two activities TransactionPage and AddTransactionActivity. This intent starts the AddTransactionActivity.
                Intent intent = new Intent(getActivity(), AddTransactionActivity.class);
                startActivityForResult(intent, CREATE_TRANSACTION);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
        //return inflater.inflate(R.layout.fragment_transactions_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v =  getView();
        pieChart = (PieChart) v.findViewById(R.id.pieChart);
        r = pieChart.getRenderer();
        setupPieChart();
        loadPieChartData();
        v = getView();
    }

    private void setlist(){
        FragmentActivity tPFA = getActivity();
        final RecyclerTransactionAdapter adapter = new RecyclerTransactionAdapter(getContext(), transactions);
        tPFA.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(adapter);
            }
        });

    }


    private void setupPieChart(){
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.rgb(34, 34, 34));
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.WHITE);
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

        //Hash map that keeps track of int value for each category
        HashMap<Category, Integer> values = new HashMap<>();

        //adds all Transactions from network to pieChart
        for (Transaction t : transactions) {
            Category c = t.getCategory();
            int v = t.getAmount();
            if (values.containsKey(c)){
                //the category is already defined, so we add the values together
                int planVal = values.get(c);
                values.put(c, v+planVal);
            }
            else{
                //the category is not defined, so we put the value for that category in the map
                values.put(c, v);
            }
        }

        //loop through hash map and put in entries
        for (Map.Entry<Category, Integer> e : values.entrySet()){
            entries.add(new PieEntry(e.getValue(), e.getKey()));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == CREATE_TRANSACTION){
                if (data != null){
                    int amount = data.getIntExtra("amount", 0);
                    Category cat = (Category)data.getExtras().getSerializable("category");
                    LocalDate date = (LocalDate) data.getExtras().getSerializable("date");
                    Transaction t = new Transaction(amount, cat, date);
                    setlist();
                    ts.addTransaction(t);
                    loadPieChartData();
                }
            }
        }
    }
}