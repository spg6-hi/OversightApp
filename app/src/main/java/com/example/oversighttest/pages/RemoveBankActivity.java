package com.example.oversighttest.pages;

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
    private Button confirmButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_bank);
        mFundsRemoved = (EditText) findViewById(R.id.mFundsRemoved);
        confirmButton = (Button)findViewById(R.id.mConfirmRemoveFunds);
        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try{
                    int fundsRemoved = Integer.valueOf(mFundsRemoved.getText().toString());
                    Intent data = new Intent();
                    data.putExtra("funds removed", fundsRemoved);
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
        cancelButton = (Button) findViewById(R.id.cancelRemoveBalance);
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
