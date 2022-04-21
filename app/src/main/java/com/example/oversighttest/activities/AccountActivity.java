package com.example.oversighttest.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.oversighttest.R;
import com.example.oversighttest.entities.Session;
import com.example.oversighttest.entities.User;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AccountActivity extends AppCompatActivity {

    private TextView mAccountUserName, mAccountCreated, mAccountTransactions;
    private Button mAccountChangePasswordButton, mAccountDeleteAccountButton, mAccountGoBack, mLogOut;

    private FloatingActionButton fab;
    private ExtendedFloatingActionButton fabone, fabtwo, fabthree;
    private Float translationYaxis = 100f;
    private Boolean menuOpen = false;
    private OvershootInterpolator interpolator = new OvershootInterpolator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Session session = Session.getInstance();
        User user = session.getLoggedIn();

        mAccountUserName = (TextView)findViewById(R.id.mAccountUserName);
        mAccountCreated = (TextView) findViewById(R.id.mAccountCreated);
        mAccountTransactions = (TextView) findViewById(R.id.mAccountTransactions);

        //Sækja username og password og setja það hér
        mAccountUserName.setText(user.getUserName());

        String created = user.getCreated();
        if (created == null){
            if (user.getDateCreated() != null){
                created = user.getDateCreated().toString();
            }

            else{
                created = "no date available";
            }
        }
        mAccountCreated.setText(created);

        String totalTransactions = Integer.toString(user.getAmountOfTransactions());

        mAccountTransactions.setText(totalTransactions);

        ShowMenu();

    }


    public void changePassword() {
        Intent intent = new Intent(this, ChangePasswordActivity.class);

        startActivity(intent);
    }

    public void deleteAccount() {
        Intent intent = new Intent(this, DeleteAccountActivity.class);

        startActivity(intent);
    }

    public void logOut(){
        deleteSharedPrefs();

        SharedPreferences prefs = getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
        String userName = prefs.getString(MainActivity.USER, "asdf");
        System.out.println(userName);

        Session.getInstance().setLoggedIn(null);
        Intent intent = LoginActivity.newIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //Floating action buttons
    private void ShowMenu() {
        fab = findViewById(R.id.mAccountFab);
        fabone = findViewById(R.id.mLogOut);
        fabtwo = findViewById(R.id.mChangePassword);
        fabthree = findViewById(R.id.mDeleteAccount);

        fabone.setAlpha(0f);
        fabtwo.setAlpha(0f);
        fabthree.setAlpha(0f);

        fabone.setTranslationY(translationYaxis);
        fabtwo.setTranslationY(translationYaxis);
        fabthree.setTranslationY(translationYaxis);

        ConstraintLayout spendingLayout = (ConstraintLayout) findViewById(R.id.accountLayout);

        spendingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFab();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menuOpen) {
                    closeFab();
                } else {
                    openFab();
                }
            }
        });

        fabone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFab();
                logOut();
            }
        });

        fabtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFab();
                changePassword();
            }
        });

        fabthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFab();
                deleteAccount();
            }
        });
    }

    //Closes floating action buttons
    private void closeFab() {
        menuOpen = !menuOpen;
        fab.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
        fabone.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabtwo.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabthree.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
    }

    //Opens floating action buttons
    private void openFab() {
        menuOpen = !menuOpen;
        fab.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        fabone.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabtwo.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabthree.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
    }

    public void deleteSharedPrefs(){
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MainActivity.USER, "");
        editor.putString(MainActivity.PASSWORD, "");
        editor.apply();
    }
}
