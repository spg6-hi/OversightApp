package com.example.oversighttest.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oversighttest.R;
import com.example.oversighttest.entities.Session;
import com.example.oversighttest.entities.User;

public class AccountActivity extends AppCompatActivity {

    private TextView mAccountUserName, mAccountCreated, mAccountTransactions;
    private Button mAccountChangePasswordButton, mAccountDeleteAccountButton, mAccountGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Session session = Session.getInstance();
        User user = session.getLoggedIn();

        System.out.println(user);

        mAccountUserName = (TextView)findViewById(R.id.mAccountUserName);
        mAccountCreated = (TextView) findViewById(R.id.mAccountCreated);
        mAccountTransactions = (TextView) findViewById(R.id.mAccountTransactions);

        //Sækja username og password og setja það hér
        mAccountUserName.setText(user.getUserName());

        String created = user.getCreated();
        if (created == null){
            if (user.getDateCreated() != null){
                created = user.getDateCreated().toString();
            }
            else{
                created = "no date available";
            }
        }
        mAccountCreated.setText(created);

        String totalTransactions = "";
        if (Session.getInstance().getTransactions() != null){
            totalTransactions = Integer.toString(Session.getInstance().getTransactions().size());
        }

        mAccountTransactions.setText(totalTransactions);

        mAccountChangePasswordButton = findViewById(R.id.mChangePasswordButton);

        mAccountChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });

        mAccountDeleteAccountButton = findViewById(R.id.mDeleteAccountButton);

        mAccountDeleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAccount();
            }
        });

        mAccountGoBack = findViewById(R.id.mAccountGoBack);

        mAccountGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }
    public void changePassword() {
        Intent intent = new Intent(this, ChangePasswordActivity.class);

        startActivity(intent);
    }

    public void deleteAccount() {
        Intent intent = new Intent(this, DeleteAccountActivity.class);

        startActivity(intent);
    }
}
