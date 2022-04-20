package com.example.oversighttest.pages;

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

public class DeleteAccountActivity extends AppCompatActivity {

    private Button mAccountDeleteConfirmButton, mAccountDeleteDenyButton;
    private EditText mAccountDeletePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_delete);


        mAccountDeleteConfirmButton = (Button)findViewById(R.id.mAccountDeleteConfirmButton);
        mAccountDeleteDenyButton = (Button)findViewById(R.id.mAccountDeleteDenyButton);

        mAccountDeletePassword = (EditText)findViewById(R.id.mAccountDeletePassword);

        //confirm button, returns OK
        mAccountDeleteConfirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteAccount();
            }
        });

        //cancel button, returns CANCELED
        mAccountDeleteDenyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Bæta við
                //Returns the user back
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    public void deleteAccount() {
        NetworkManager nm = NetworkManager.getInstance(this.getApplicationContext());
        Session session = Session.getInstance();
        User user = session.getLoggedIn();
        nm.deleteUser(user.getUserName(), user.getPassword(), UserService.get_SHA_512(mAccountDeletePassword.getText().toString()), new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                deleteAccountAgain();
            }

            @Override
            public void onFailure(String errorString) {
                makeToast();
            }
        });
    }

    public void deleteAccountAgain() {
        deleteSharedPrefs();

        Intent intent = LoginActivity.newIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    public void deleteSharedPrefs(){
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MainActivity.USER, "");
        editor.putString(MainActivity.PASSWORD, "");
        editor.apply();
    }

    public void makeToast() {
        Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show();
    }
}
