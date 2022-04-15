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
import com.example.oversighttest.entities.BankAccount;
import com.example.oversighttest.entities.Session;
import com.example.oversighttest.network.DummyNetwork;
import com.example.oversighttest.network.NetworkCallback;
import com.example.oversighttest.network.NetworkManager;
import com.example.oversighttest.services.BankBalanceService;

public class BankPage extends Fragment {
    private TextView mBankBalance;
    private Button mAddFunds;
    private Button mRemoveFunds;

    private NetworkManager nm;

    private View v;

    //request codes
    private static final int ADD_BANK_BALANCE = 0;
    private static final int REMOVE_BANK_BALANCE = 1;

    //gets displayed when trying to remove more funds than you have
    private static final String INSUFFICIENT_FUNDS = "insufficient funds";



    /**
     * Constructor
     */
    public BankPage(){
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
            }

            @Override
            public void onFailure(String errorString) {

            }
        });
        mBankBalance.setText(""+current.getBalance());
    }
}