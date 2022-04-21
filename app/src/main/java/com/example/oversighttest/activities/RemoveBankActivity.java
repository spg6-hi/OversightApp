package com.example.oversighttest.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oversighttest.R;

public class RemoveBankActivity extends AppCompatActivity {

    private EditText mFundsRemoved;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_bank);

        //text input for amount to be removed
        mFundsRemoved = findViewById(R.id.mFundsRemoved);

        //confirm button, tries to take value from text field and return it to parent activity
        Button confirmButton = findViewById(R.id.mConfirmRemoveFunds);
        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try{
                    int fundsRemoved = Integer.parseInt(mFundsRemoved.getText().toString());
                    Intent data = new Intent();
                    data.putExtra("funds removed", fundsRemoved);
                    setResult(RESULT_OK, data);
                    finish();
                }
                catch (Exception e){

                    //something went wrong, cancel

                    Intent data = new Intent();
                    setResult(RESULT_CANCELED, data);
                    finish();
                }
            }
        });

        // cancel button, returns CANCELED to parent activity
        Button cancelButton = findViewById(R.id.cancelRemoveBalance);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    public static Intent newIntent(Context packageContext){
        Intent i = new Intent(packageContext, RemoveBankActivity.class);
        return i;
    }
}
