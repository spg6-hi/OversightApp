package com.example.oversighttest.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oversighttest.R;

public class DeleteAccountActivity extends AppCompatActivity {

    private Button AccountDeleteConfirmButton, AccountDeleteDenyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_delete);


        AccountDeleteConfirmButton = (Button)findViewById(R.id.mAccountDeleteConfirmButton);
        AccountDeleteDenyButton = (Button)findViewById(R.id.mAccountDeleteDenyButton);

        //confirm button, returns OK
        AccountDeleteConfirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Bæta við
                //Delete the user
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //cancel button, returns CANCELED
        AccountDeleteDenyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Bæta við
                //Returns the user back
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }
}
