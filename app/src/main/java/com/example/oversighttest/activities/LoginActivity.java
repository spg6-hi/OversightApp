package com.example.oversighttest.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.Base64;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText mUserName;
    private EditText mPassword;
    private Button mConfirmLogin;
    private Button mSignUp;
    private static final Base64.Encoder base64Encoder = Base64.getEncoder();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if (loadData()){
            return;
        }

        setContentView(R.layout.activity_login);

        mUserName = (EditText)findViewById(R.id.mNewUserName);
        mPassword = (EditText)findViewById(R.id.mNewPassword);

        mConfirmLogin = (Button)findViewById(R.id.mConfirmSignup);
        mConfirmLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmLogin();
            }
        });

        mSignUp = (Button)findViewById(R.id.mLogIn);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }


    public boolean loadData(){
        System.out.println("LOADING DATA...");
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
        String userName = sharedPreferences.getString(MainActivity.USER, "");
        String password = sharedPreferences.getString(MainActivity.PASSWORD, "");
        System.out.println("current user: "+ userName);
        //Check if there is a user logged in
        if (!userName.equals("")){
            NetworkManager nm = NetworkManager.getInstance(getApplicationContext());
            nm.loginUser(userName, password, new NetworkCallback<User>(){
                @Override
                public void onSuccess(User result){
                    if (result != null){
                        Session s = Session.getInstance();
                        s.setLoggedIn(result);
                        openMainActivity();

                    }
                    else{
                        SharedPreferences sh = getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sh.edit();
                        editor.putString(MainActivity.USER, "");
                        editor.putString(MainActivity.PASSWORD, "");
                        editor.apply();
                        Toast.makeText(getApplicationContext(), "This user does not exist", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(String errorString){
                }
            });
            return true;
        }
        return false;
    }

    //go to main screen
    public void openMainActivity() {
        Intent intent = MainActivity.newIntent(this);
        startActivity(intent);
    }

    public static Intent newIntent(Context packageContext){
        Intent i = new Intent(packageContext, LoginActivity.class);
        return i;
    }


    public void confirmLogin(){
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();

        String hashed = UserService.get_SHA_512(password);

        NetworkManager nm = NetworkManager.getInstance(getApplicationContext());
        nm.loginUser(userName, hashed, new NetworkCallback<User>(){
            @Override
            public void onSuccess(User result){
                if (result != null){
                    if (result.getPassword().equals("wrong password")){
                        Toast.makeText(getApplicationContext(), "Check password", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Session s = Session.getInstance();
                        s.setLoggedIn(result);
                        openMainActivity();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "This user does not exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(String errorString){
                System.out.println(errorString);
                Toast.makeText(getApplicationContext(), "this user does not exists", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void signUp(){
        Intent intent = SignUpActivity.newIntent(this);
        startActivity(intent);
    }

}
