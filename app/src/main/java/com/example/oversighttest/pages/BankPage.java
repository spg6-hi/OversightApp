package com.example.oversighttest.pages;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oversighttest.R;
import com.example.oversighttest.network.DummyNetwork;
import com.example.oversighttest.services.BankBalanceService;

public class BankPage extends Fragment {

    private BankBalanceService bankService;

    private TextView mBankBalance;
    private Button mAddFunds;
    private Button mRemoveFunds;
    private View v;

    private static final int ADD_BANK_BALANCE = 0;
    private static final int REMOVE_BANK_BALANCE = 1;

    private static final String INSUFFICIENT_FUNDS = "insufficient funds";

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
        mBankBalance.setText("" + bankService.getBankBalance());

        mAddFunds = (Button) v.findViewById(R.id.mAddFunds);
        mAddFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = AddBankActivity.newIntent(getActivity());
                startActivityForResult(i, ADD_BANK_BALANCE);
            }
        });

        mRemoveFunds = (Button) v.findViewById(R.id.mRemoveFunds);
        mRemoveFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = RemoveBankActivity.newIntent(getActivity());
                startActivityForResult(i, REMOVE_BANK_BALANCE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if (requestCode == ADD_BANK_BALANCE){
                if (data != null){
                    int fundsAdded = data.getIntExtra("funds added", 0);
                    bankService.addFunds(fundsAdded);
                    mBankBalance.setText(""+bankService.getBankBalance());
                }
            }
            else if (requestCode == REMOVE_BANK_BALANCE){
                if (data != null){
                    int fundsRemoved = data.getIntExtra("funds removed", 0);
                    if (fundsRemoved > bankService.getBankBalance()){
                        Toast.makeText(getActivity(), INSUFFICIENT_FUNDS, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    bankService.removeFunds(fundsRemoved);
                    mBankBalance.setText(""+bankService.getBankBalance());
                }
            }
        }
    }
}