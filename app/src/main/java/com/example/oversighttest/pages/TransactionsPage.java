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
import android.widget.TextView;
import android.widget.Toast;

import com.example.oversighttest.R;
import com.example.oversighttest.adapters.RecyclerTransactionAdapter;
import com.example.oversighttest.entities.Category;
import com.example.oversighttest.entities.Session;
import com.example.oversighttest.entities.Transaction;
import com.example.oversighttest.entities.User;
import com.example.oversighttest.network.DummyNetwork;
import com.example.oversighttest.network.NetworkCallback;
import com.example.oversighttest.network.NetworkManager;
import com.example.oversighttest.services.TransactionService;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.Renderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionsPage extends Fragment {

    private static final int CREATE_TRANSACTION = 0;

    public static final int SORT_BY_AMOUNT = 0;
    public static final int SORT_BY_CATEGORY = 1;
    public static final int SORT_BY_DATE = 2;

    private PieChart pieChart;
    private HorizontalBarChart barChart;
    private BarData barData;
    private BarDataSet barDataSet;
    private ArrayList<BarEntry> barEntries;

    private Button mAddTransaction;

    private View v;
    private DummyNetwork network;
    private TransactionService ts;
    private ArrayList<Transaction> transactions;

    private TextView amount;
    private TextView category;
    private TextView date;

    private RecyclerView recyclerView;

    private Renderer r;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity a = (MainActivity) getActivity();
        final FragmentActivity tPFA = getActivity();

        ts = new TransactionService();

        transactions = new ArrayList<Transaction>();


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

        /*
        TODO: functionality to delete a transaction
         */
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

        Session s = Session.getInstance();
        User loggedIn = s.getLoggedIn();

        NetworkManager nm = NetworkManager.getInstance(this.getContext());
        nm.getTransactions(loggedIn, new NetworkCallback<List<Transaction>>() {
            @Override
            public void onSuccess(List<Transaction> result) {
                ts.saveTransactions(result);
                transactions = Session.getInstance().getTransactions();
                setlist();
                setupPieChart();
                GroupBarChart();
                loadPieChartData();
            }

            @Override
            public void onFailure(String errorString) {

            }
        });


        amount = (TextView) rootView.findViewById(R.id.sortAmount);
        amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transactions = ts.seeSortedTransaction(SORT_BY_AMOUNT);
                setlist();
            }
        });

        category = (TextView) rootView.findViewById(R.id.sortCategory);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transactions = ts.seeSortedTransaction(SORT_BY_CATEGORY);
                setlist();
            }
        });

        date = (TextView) rootView.findViewById(R.id.sortDate);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transactions = ts.seeSortedTransaction(SORT_BY_DATE);
                setlist();
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

        GroupBarChart();

        v = getView();
    }

    /**
     * This function updates the list view
     */
    private void setlist(){
        FragmentActivity tPFA = getActivity();
        final RecyclerTransactionAdapter adapter = new RecyclerTransactionAdapter(getContext(), transactions, this);
        tPFA.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(adapter);
            }
        });

    }

    public void deleteTransaction(long id){
        NetworkManager nm = NetworkManager.getInstance(this.getContext());
        nm.deleteTransaction(id, Session.getInstance().getLoggedIn(), new NetworkCallback<List<Transaction>>() {
            @Override
            public void onSuccess(List<Transaction> result) {
                ts.saveTransactions(result);
                loadData();
                Toast.makeText(getContext(), "Deleted Transaction", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(String errorString) {
            }
        });
    }

    public void loadData(){
        transactions = Session.getInstance().getTransactions();
        setlist();
        setupPieChart();
        loadPieChartData();
        GroupBarChart();
    }

    /**
     * set up pie chart look
     */
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
        pieChart.animateXY(1000,1000);

        /*
        TODO: Make pie chart legend work
        TODO: Let user see value for each item in pie chart
        */
        Legend l = pieChart.getLegend();
        l.setTextColor(Color.WHITE);
        l.setTextSize(10f);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }



    public void GroupBarChart(){
        barChart = (HorizontalBarChart) v.findViewById(R.id.barChart);
        barChart.setDrawBarShadow(false);
        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        // empty labels so that the names are spread evenly
        String[] labels = Category.getListOfCategories();
        XAxis xAxis = barChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(15);
        xAxis.setAxisLineColor(Color.BLACK);
        xAxis.setAxisMinimum(1f);
        xAxis.setLabelCount(labels.length-2);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(true);
        barChart.getLegend().setTextColor(Color.WHITE);
        barChart.getLegend().setTextSize(12f);

        int[] valOne = ts.getTransactionBarChartData();
        int[] valTwo = ts.getSpendingPlanBarChartData();

        ArrayList<BarEntry> barOne = new ArrayList<>();
        ArrayList<BarEntry> barTwo = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            barOne.add(new BarEntry(i, valOne[i]));
            barTwo.add(new BarEntry(i, valTwo[i]));
            System.out.println(i);
        }

        BarDataSet set1 = new BarDataSet(barOne, "Your spending");
        set1.setColor(Color.parseColor("#be440c"));
        BarDataSet set2 = new BarDataSet(barTwo, "spending plan");
        set2.setColor(Color.parseColor("#009879"));

        set1.setHighlightEnabled(false);
        set2.setHighlightEnabled(false);
        set1.setDrawValues(false);
        set2.setDrawValues(false);

        BarData data = new BarData(set1, set2);
        float groupSpace = 0.4f;
        float barSpace = 0f;
        float barWidth = 0.3f;
        // (barSpace + barWidth) * 2 + groupSpace = 1
        data.setBarWidth(barWidth);
        // so that the entire chart is shown when scrolled from right to left
        xAxis.setAxisMaximum(labels.length - 1.1f);
        barChart.setData(data);
        barChart.setScaleEnabled(false);
        barChart.animateY(1000);
        barChart.groupBars(1f, groupSpace, barSpace);
        barChart.invalidate();

    }

    /**
     * load data from transaction for pie chart to display
     */
    private void  loadPieChartData(){
        if (transactions == null){
            transactions = new ArrayList<>();
        }

        ArrayList<PieEntry> entries = new ArrayList<>();

        Collections.sort(transactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                String t1Cat = t1.getCategory().getDisplayName();
                String t2Cat = t2.getCategory().getDisplayName();
                return t1Cat.compareTo(t2Cat);
            }
        });

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
            entries.add(new PieEntry(e.getValue(), e.getKey().getName()));
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
                    NetworkManager nm = NetworkManager.getInstance(this.getContext());
                    nm.createTransaction(Session.getInstance().getLoggedIn(), t, new NetworkCallback<List<Transaction>>() {
                        @Override
                        public void onSuccess(List<Transaction> result) {
                            ts.saveTransactions(result);
                            loadData();
                        }

                        @Override
                        public void onFailure(String errorString) {

                        }
                    });
                    ts.addTransaction(t);
                    loadPieChartData();
                }
            }
        }
    }
}