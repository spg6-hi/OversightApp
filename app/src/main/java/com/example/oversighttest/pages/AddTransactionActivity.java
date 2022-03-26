package com.example.oversighttest.pages;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.oversighttest.R;
import com.example.oversighttest.entities.Category;
import com.example.oversighttest.entities.Transaction;
import com.example.oversighttest.network.DummyNetwork;
import com.example.oversighttest.services.AddTransactionService;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddTransactionActivity extends AppCompatActivity {
    /*
    private DummyNetwork network = MainActivity.getDm();

    private AddTransactionService addTransactionService = new AddTransactionService(network);
     */

    private DatePickerDialog datePickerDialog;
    private EditText transactionAmount;
    private Button dateButton;
    private Button confirmButton;

    private LocalDate date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        LocalDate today = LocalDate.now();
        dateButton.setText(today.getYear()+ " " + today.getMonth() + " " + today.getDayOfMonth());

        transactionAmount = findViewById(R.id.mTransactionAmount);
        confirmButton = findViewById(R.id.confirmCreateTransaction);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = Integer.valueOf(transactionAmount.getText().toString());
                Category c = Category.CARS;
                try{
                    Intent intent = new Intent();
                    intent.putExtra("date", date);
                    intent.putExtra("amount", amount);
                    intent.putExtra("category", c);
                    setResult(RESULT_OK, intent);
                    finish();
                }catch (Exception e){
                    Intent data = new Intent();
                    setResult(RESULT_CANCELED, data);
                    finish();
                }


            }
        });
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month+=1;
                LocalDate d = LocalDate.of(year, month, day);
                date = d;
                dateButton.setText(d.getYear()+ " " + d.getMonth() + " " + d.getDayOfMonth());
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;

        datePickerDialog = new DatePickerDialog(this,style, dateSetListener, year, month, day);
    }

    public void openDatePicker(View view){
        datePickerDialog.show();
    }

    /*
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
     */
}
