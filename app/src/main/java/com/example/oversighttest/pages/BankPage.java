package com.example.oversighttest.pages;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oversighttest.R;
import com.example.oversighttest.entities.BankAccount;
import com.example.oversighttest.entities.Session;
import com.example.oversighttest.network.NetworkCallback;
import com.example.oversighttest.network.NetworkManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.example.oversighttest.services.BankBalanceService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BankPage extends Fragment {
    private TextView mBankBalance;
    private Button mAddFunds;
    private Button mRemoveFunds;
    private FloatingActionButton mAccountButton;
    private NetworkManager nm;
    private int[] bankBalanceHistory;
    private View v;

    //request codes
    private static final int ADD_BANK_BALANCE = 0;
    private static final int REMOVE_BANK_BALANCE = 1;

    //gets displayed when trying to remove more funds than you have
    private static final String INSUFFICIENT_FUNDS = "insufficient funds";

    private LineChart mLineChart;
    private RadioGroup mPeriodRadioGroup;


    /**
     * Constructor
     */
    public BankPage(){
        this.bankBalanceHistory = new int[10];
        this.nm = NetworkManager.getInstance(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bank_page, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        v =  getView();
        AccountButton();

        System.out.println("v: " + v);
        //Linechart that displays bank balance over time
        mLineChart = v.findViewById(R.id.mLineChart);
        //Radio Buttons to select time interval for the line chart (1 day, 30 days,...)
        mPeriodRadioGroup = (RadioGroup) v.findViewById(R.id.periodRadiogroup);
        mPeriodRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                View radioButton = mPeriodRadioGroup.findViewById(checkedId);
                int index = mPeriodRadioGroup.indexOfChild(radioButton);

                // Add logic here
                Toast.makeText(getContext(), "Selected button number " + index, Toast.LENGTH_LONG).show();
                System.out.println("radio id: " + checkedId + " " +index);


            }
        });
        configureLineChart();
        setLineChartData();

        //get temporary fake bank balance and display it
        Session.getInstance().setBankAccount(new BankAccount(0,0));
        mBankBalance = (TextView) v.findViewById(R.id.mBankBalance);
        mBankBalance.setText("" + Session.getInstance().getBankAccount().getBalance());

        //fetch the actual balance from the network, it will update when fetched
        nm.getBalance(Session.getInstance().getLoggedIn(), new NetworkCallback<BankAccount>() {
            @Override
            public void onSuccess(BankAccount result) {
                Session.getInstance().setBankAccount(result);
                mBankBalance.setText("" + Session.getInstance().getBankAccount().getBalance());
                configureLineChart();
                setLineChartData();
            }
            @Override
            public void onFailure(String errorString) {
                System.out.println("failed to fetch bank account");
            }
        });

        //add funds button, opens an activity to add funds
        mAddFunds = (Button) v.findViewById(R.id.mAddFunds);
        mAddFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = AddBankActivity.newIntent(getActivity());
                startActivityForResult(i, ADD_BANK_BALANCE);
            }
        });

        //remove funds button, opens an activity to remove funds
        mRemoveFunds = (Button) v.findViewById(R.id.mRemoveFunds);
        mRemoveFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = RemoveBankActivity.newIntent(getActivity());
                startActivityForResult(i, REMOVE_BANK_BALANCE);
            }
        });

        nm.getTransactionsForDays(Session.getInstance().getLoggedIn(), 365, new NetworkCallback<List<Integer>>() {
            @Override
            public void onSuccess(List<Integer> result) {
                int balance = Session.getInstance().getBankAccount().getBalance();

                if (result != null){
                    System.out.println(result);
                    bankBalanceHistory = new int[result.size()];
                    int index = 0;
                    for (Integer i: result){
                        bankBalanceHistory[index++] = i+balance;
                    }
                    configureLineChart();
                    setLineChartData();
                }
            }

            @Override
            public void onFailure(String errorString) {

            }
        });

    }

    /**
     * This gets called when a child activity returns some result
     * @param requestCode what kind of request was this, i.e. what activity is this
     * @param resultCode what is the result of the call, i.e. OK, CANCELLED etc
     * @param data the data return by the child activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode == ADD_BANK_BALANCE) {
                //add funds
                if (data != null) {
                    addBalance(data);
                }
            }
            else if (requestCode == REMOVE_BANK_BALANCE) {
                // remove funds
                if (data != null) {
                    removeBalance(data);
                }
            }
        }
    }

    private void addBalance(Intent data){
        int fundsAdded = data.getIntExtra("funds added", 0);
        if (fundsAdded <= 0) return;


        //pseudo update the session spending plan, it will be overwritten when the network call is finished
        Session s = Session.getInstance();
        BankAccount current = s.getBankAccount();
        current.setBalance(current.getBalance()+fundsAdded);
        s.setBankAccount(current);

        //Call network to update the real spending plan
        nm.addBalance(fundsAdded, s.getLoggedIn(), new NetworkCallback<BankAccount>() {
            @Override
            public void onSuccess(BankAccount result) {
                Session.getInstance().setBankAccount(result);
                mBankBalance.setText(""+result.getBalance());
                setLineChartData();
            }
            @Override
            public void onFailure(String errorString) {

            }
        });

        //bankService.addFunds(fundsAdded);
        mBankBalance.setText(""+current.getBalance());
    }

    private void removeBalance(Intent data){
        int fundsRemoved = data.getIntExtra("funds removed", 0);
        if (fundsRemoved > Session.getInstance().getBankAccount().getBalance()) {
            Toast.makeText(getActivity(), INSUFFICIENT_FUNDS, Toast.LENGTH_SHORT).show();
            return;
        }

        //Pseudo update session temporarily
        Session s = Session.getInstance();
        BankAccount current = s.getBankAccount();
        current.setBalance(current.getBalance() - fundsRemoved);
        s.setBankAccount(current);

        //call network and update session with real data
        nm.removeBalance(fundsRemoved, s.getLoggedIn(), new NetworkCallback<BankAccount>() {
            @Override
            public void onSuccess(BankAccount result) {
                Session.getInstance().setBankAccount(result);
                mBankBalance.setText(""+result.getBalance());
                setLineChartData();
            }

            @Override
            public void onFailure(String errorString) {

            }
        });
        mBankBalance.setText(""+current.getBalance());
    }

    private void AccountButton() {
        mAccountButton = v.findViewById(R.id.mAccountButton);

        mAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AccountActivity.class);

                startActivity(intent);
            }
        });
    }

    private void configureLineChart() {
        Description desc = new Description();
        desc.setText(String.valueOf(R.string.mLineChart));
        desc.setTextSize(28);
        mLineChart.setDescription(desc);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float value) {
                long millis = (long) value * 1000L;
                return mFormat.format(new Date(millis));
            }
        });
    }
    private void setLineChartData(){
        v = getView();
        mLineChart = (LineChart) v.findViewById(R.id.mLineChart);
        //mLineChart.setDrawLineShadow(false);
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setPinchZoom(false);
        mLineChart.setDrawGridBackground(false);
        // empty labels so that the names are spread evenly
        //String[] labels = Category.getListOfCategories();
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(15);
        xAxis.setAxisLineColor(Color.BLACK);
        xAxis.setAxisMinimum(1f);
        xAxis.setLabelCount(5);
        //xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getLegend().setEnabled(true);
        mLineChart.getLegend().setTextColor(Color.WHITE);
        mLineChart.getLegend().setTextSize(12f);

        int[] valOne = bankBalanceHistory;
        //ts.getTransactionBarChartData();
        //int[] valTwo = {0,150,100,150,150,100,90,20,80,70};//ts.getSpendingPlanBarChartData();

        ArrayList<Entry> lineOne = new ArrayList<>();
        //ArrayList<Entry> lineTwo = new ArrayList<>();
        for (int i = 0; i < valOne.length; i++) {
            lineOne.add(new Entry(i, valOne[i]));
            //lineTwo.add(new Entry(i, valTwo[i]));
            //System.out.println(i);
        }

        LineDataSet set1 = new LineDataSet(lineOne, "Your spending");
        set1.setColor(getResources().getColor(R.color.overSightOrange));
        //LineDataSet set2 = new LineDataSet(lineTwo, "spending plan");
        //set2.setColor(getResources().getColor(R.color.overSightGreen));

        set1.setHighlightEnabled(false);
        //set2.setHighlightEnabled(false);
        set1.setDrawCircles(false);
        //set2.setDrawCircles(false);
        set1.setLineWidth(3);
        //set2.setLineWidth(3);
        set1.setDrawValues(false);
        //set2.setDrawValues(false);

        LineData data = new LineData(set1);

        // so that the entire chart is shown when scrolled from right to left
        xAxis.setAxisMaximum(valOne.length);
        mLineChart.setData(data);
        mLineChart.setScaleEnabled(false);
        mLineChart.animateX(500);

        mLineChart.setDescription(new Description());
        mLineChart.getDescription().setText(getResources().getString(R.string.mLineChartDescription));

        //setting the color of the axis values
        mLineChart.getAxisLeft().setTextColor(getResources().getColor(R.color.white)); // left y-axis
        mLineChart.getXAxis().setTextColor(getResources().getColor(R.color.white));
        mLineChart.getLegend().setTextColor(getResources().getColor(R.color.overSightOrange));
        mLineChart.getDescription().setTextColor(getResources().getColor(R.color.softOrange));
        mLineChart.invalidate();
    }

}