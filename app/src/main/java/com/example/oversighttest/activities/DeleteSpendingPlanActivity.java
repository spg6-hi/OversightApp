package com.example.oversighttest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oversighttest.R;

public class DeleteSpendingPlanActivity extends AppCompatActivity {

    private Button confirmButton, denyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deletespendingplan);


        confirmButton = (Button)findViewById(R.id.confirmButton);
        denyButton = (Button)findViewById(R.id.denyButton);

        //confirm button, returns OK
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Delete the spending plan
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //cancel button, returns CANCELED
        denyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Returns the user back
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }
}
