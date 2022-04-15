package com.example.oversighttest.pages;

import android.content.Context;
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

public class SignUpActivity extends AppCompatActivity {
    private Button mConfirmSignup, mLogIn;
    private EditText mNewUserName, mNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mNewUserName = (EditText) findViewById(R.id.mNewUserName);
        mNewPassword = (EditText) findViewById(R.id.mNewPassword);

        mConfirmSignup = (Button) findViewById(R.id.mconfirmSignup);
        mConfirmSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmSignup();
            }
        });

        mLogIn = (Button) findViewById(R.id.mLogIn);
        mLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    public void confirmSignup(){
        String userName = mNewUserName.getText().toString();
        String password = mNewPassword.getText().toString();

        String hashed = UserService.get_SHA_512(password);


        NetworkManager nm = NetworkManager.getInstance(this);
        nm.createUser(userName, hashed, new NetworkCallback<User>() {
            @Override
            public void onSuccess(User result) {
                signupSuccess(result);
            }

            @Override
            public void onFailure(String errorString) {
                signupFailure(errorString);
            }
        });
    }

    public void login(){
        Intent intent = LoginActivity.newIntent(this);
        startActivity(intent);
    }

    private void signupSuccess(User result){
        if (result != null){
            Session session = Session.getInstance();
            session.setLoggedIn(result);
            openMainActivity();
        }
        else{
            Toast.makeText(getApplicationContext(), "Username is taken", Toast.LENGTH_SHORT).show();
        }
    }

    private void signupFailure(String errorString){
        System.out.println(errorString);
    }

    public void openMainActivity() {
        Intent intent = MainActivity.newIntent(this);
        startActivity(intent);
    }

    public static Intent newIntent(Context packageContext){
        Intent i = new Intent(packageContext, SignUpActivity.class);
        return i;
    }

}
