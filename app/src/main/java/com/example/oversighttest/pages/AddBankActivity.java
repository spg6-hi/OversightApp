package com.example.oversighttest.pages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oversighttest.R;

import java.security.cert.Extension;

public class AddBankActivity extends AppCompatActivity {
    private EditText mFundsAdded;
    private Button confirmButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank);
        mFundsAdded = (EditText) findViewById(R.id.mFundsRemoved);
        confirmButton = (Button)findViewById(R.id.mConfirmRemoveFunds);
        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try{
                    int fundsAdded = Integer.valueOf(mFundsAdded.getText().toString());
                    Intent data = new Intent();
                    data.putExtra("funds added", fundsAdded);
                    setResult(RESULT_OK, data);
                    finish();
                }
                catch (Exception e){
                    Intent data = new Intent();
                    setResult(RESULT_CANCELED, data);
                    finish();
                }

            }
        });

        cancelButton = (Button) findViewById(R.id.cancelAddBalance);
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
        Intent i = new Intent(packageContext, AddBankActivity.class);
        return i;
    }

}
