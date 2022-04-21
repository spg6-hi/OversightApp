package com.example.oversighttest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oversighttest.R;
import com.example.oversighttest.entities.Session;
import com.example.oversighttest.entities.User;
import com.example.oversighttest.network.NetworkCallback;
import com.example.oversighttest.network.NetworkManager;
import com.example.oversighttest.services.UserService;

public class ChangePasswordActivity extends AppCompatActivity {

    private Button mChangePasswordConfirmButton, mChangePasswordCancelButton;
    private EditText mChangePasswordOldPassword, mChangePasswordNewPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_change_password);


        mChangePasswordConfirmButton = (Button)findViewById(R.id.mChangePasswordButtonConfirm);
        mChangePasswordCancelButton = (Button)findViewById(R.id.mChangePasswordButtonCancel);

        mChangePasswordOldPassword = (EditText)findViewById(R.id.mChangePasswordOldPassword);
        mChangePasswordNewPassword = (EditText)findViewById(R.id.mChangePasswordNewPassword);

        //confirm button, returns OK
        mChangePasswordConfirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changePassword();
            }
        });

        //cancel button, returns CANCELED
        mChangePasswordCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Bæta við
                //Returns the user back
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    public void changePassword() {
        NetworkManager nm = NetworkManager.getInstance(this.getApplicationContext());
        Session session = Session.getInstance();
        User user = session.getLoggedIn();
        nm.changePassword(user.getUserName(), UserService.get_SHA_512(mChangePasswordOldPassword.getText().toString()),
                UserService.get_SHA_512(mChangePasswordNewPassword.getText().toString()), new NetworkCallback<User>() {
            @Override
            public void onSuccess(User result) {
                changePasswordAgain();
            }

            @Override
            public void onFailure(String errorString) {
                makeToast();
            }
        });
    }

    public void changePasswordAgain() {
        Intent intent = LoginActivity.newIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void makeToast() {
        Toast.makeText(this, "Wrong Old Password", Toast.LENGTH_SHORT).show();
    }
}
