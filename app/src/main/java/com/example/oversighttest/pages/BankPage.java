package com.example.oversighttest.pages;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.oversighttest.R;
import com.example.oversighttest.network.DummyNetwork;
import com.example.oversighttest.services.BankBalanceService;
import com.github.mikephil.charting.charts.PieChart;

public class BankPage extends Fragment {

    private BankBalanceService bankService;

    private TextView mBankBalance;
    private Button mAddFunds;
    private Button mRemoveFunds;
    private View v;

    public BankPage(DummyNetwork dm){
        this.bankService = new BankBalanceService(dm);
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
        mBankBalance = (TextView) v.findViewById(R.id.mBankBalance);
        mBankBalance.setText("Bank Balance: " + bankService.getBankBalance());

        mAddFunds = (Button) v.findViewById(R.id.mAddFunds);
        mAddFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bankService.addFunds();
                mBankBalance.setText("Bank Balance: " + bankService.getBankBalance());
            }
        });

        mRemoveFunds = (Button) v.findViewById(R.id.mRemoveFunds);
        mRemoveFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bankService.removeFunds();
                mBankBalance.setText("Bank Balance: " + bankService.getBankBalance());
            }
        });

    }


}