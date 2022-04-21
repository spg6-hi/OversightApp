package com.example.oversighttest.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oversighttest.R;

public class AddBankActivity extends AppCompatActivity {

    private EditText mFundsAdded;
    private Button confirmButton;
    private Button cancelButton;




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank);
        mFundsAdded = (EditText) findViewById(R.id.mFundsRemoved);

        /*
        Confirm button
        When pressed, get value from text field and return it
         */
        confirmButton = (Button)findViewById(R.id.mConfirmRemoveFunds);
        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    /*
                    Get the value from the text field and return it to parent activity
                     */
                    int fundsAdded = Integer.valueOf(mFundsAdded.getText().toString());
                    Intent data = new Intent();
                    data.putExtra("funds added", fundsAdded);
                    setResult(RESULT_OK, data);
                    finish();
                }
                catch (Exception e) {
                    /*
                    Something went wrong, cancel
                     */
                    Intent data = new Intent();
                    setResult(RESULT_CANCELED, data);
                    finish();
                }
            }
        });

        /*
        Cancel button, returns to parent activity
         */
        cancelButton = (Button) findViewById(R.id.mCancelAddBalance);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    /**
     * for other activities to call this activity
     * @param packageContext
     * @return
     */
    public static Intent newIntent(Context packageContext){
        Intent i = new Intent(packageContext, AddBankActivity.class);
        return i;
    }
}
