package com.example.oversighttest.pages;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oversighttest.R;
import com.example.oversighttest.entities.Category;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddTransactionActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private EditText transactionAmount;
    private Button dateButton;
    private Button confirmButton;
    private Button cancelButton;
    private Spinner spinnerCategory;

    private LocalDate date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        initDatePicker();
        date = LocalDate.now();
        dateButton = findViewById(R.id.datePickerButton);
        LocalDate today = LocalDate.now();
        dateButton.setText(today.getMonth() + " " + today.getDayOfMonth() + " " +today.getYear());

        transactionAmount = findViewById(R.id.mTransactionAmount);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        List<Category> categoryList = Category.getCategories();
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategory.setAdapter(adapter);


        confirmButton = findViewById(R.id.confirmCreateTransaction);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = transactionAmount.getText().toString();
                int amount = 0;
                if(!text.matches("")){
                    amount = Integer.valueOf(text);
                }

                Category c = (Category)spinnerCategory.getSelectedItem();

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
        cancelButton = findViewById(R.id.cancelCreateTransaction);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
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
                dateButton.setText(d.getMonth() + " " + d.getDayOfMonth() + " " + d.getYear());
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

}
