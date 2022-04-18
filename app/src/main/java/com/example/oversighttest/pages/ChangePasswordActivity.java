package com.example.oversighttest.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oversighttest.R;

public class ChangePasswordActivity extends AppCompatActivity {

    private Button ChangePasswordConfirmButton, ChangePasswordCancelButton;
    private EditText ChangePasswordOldPassword, ChangePasswordNewPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_change_password);


        ChangePasswordConfirmButton = (Button)findViewById(R.id.mChangePasswordButtonConfirm);
        ChangePasswordCancelButton = (Button)findViewById(R.id.mChangePasswordButtonCancel);

        ChangePasswordOldPassword = (EditText)findViewById(R.id.mChangePasswordOldPassword);
        ChangePasswordNewPassword = (EditText)findViewById(R.id.mChangePasswordNewPassword);

        //confirm button, returns OK
        ChangePasswordConfirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Bæta við
                //change password
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //cancel button, returns CANCELED
        ChangePasswordCancelButton.setOnClickListener(new View.OnClickListener() {
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
