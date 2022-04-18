package com.example.oversighttest.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oversighttest.R;

public class AccountActivity extends AppCompatActivity {

    private TextView mAccountUserName, mAccountPassword;
    private Button mAccountChangePasswordButton, mAccountDeleteAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAccountUserName = (TextView)findViewById(R.id.mAccountUserName);
        //mAccountPassword = (TextView) findViewById(R.id.mAccountPassword);

        //Sækja username og password og setja það hér
        //mAccountUserName.setText("a");
        //mAccountPassword.setText("a");

        mAccountChangePasswordButton = findViewById(R.id.mChangePasswordButton);

        mAccountChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AccountActivity.class);

                startActivity(intent);
            }
        });
    }
}
