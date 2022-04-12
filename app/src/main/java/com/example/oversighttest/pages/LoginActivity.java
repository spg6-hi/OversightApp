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
import com.example.oversighttest.entities.User;
import com.example.oversighttest.network.NetworkCallback;
import com.example.oversighttest.network.NetworkManager;
import com.example.oversighttest.services.TokenSaver;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText mUserName;
    private EditText mPassword;
    private Button mConfirmLogin;
    private static final Base64.Encoder base64Encoder = Base64.getEncoder();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserName = (EditText)findViewById(R.id.mUserName);
        mPassword = (EditText)findViewById(R.id.mPassword);

        mConfirmLogin = (Button)findViewById(R.id.mconfirmLogin);
        mConfirmLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = mUserName.getText().toString();
                String password = mPassword.getText().toString();

                String userToken = generateUserToken(userName, password);
                TokenSaver.setToken(getApplicationContext(), userToken);

                NetworkManager nm = NetworkManager.getInstance(getApplicationContext());
                nm.loginUser(userToken, new NetworkCallback<User>(){
                    @Override
                    public void onSuccess(User result){
                        if (result != null){
                            openMainActivity();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(String errorString){
                        System.out.println(errorString);
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void openMainActivity() {
        Intent intent = MainActivity.newIntent(this);
        startActivity(intent);
    }

    public static String generateUserToken(String userEmail, String userPassword) {
        String combined = userEmail + "%" + userPassword; // we can use "%" because "%" is an illegal character in an email
        return base64Encoder.encodeToString(combined.getBytes(StandardCharsets.UTF_8));
    }

}
