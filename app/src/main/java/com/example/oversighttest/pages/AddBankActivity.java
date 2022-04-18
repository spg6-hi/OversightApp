package com.example.oversighttest.pages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oversighttest.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddBankActivity extends AppCompatActivity {

    private EditText mFundsAdded;
    private Button confirmButton;
    private Button cancelButton;
    private LineChart mLineChart;
    private RadioGroup mPeriodRadioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank);
        mFundsAdded = (EditText) findViewById(R.id.mFundsRemoved);
        //Linechart that displays bank balance over time
        mLineChart = findViewById(R.id.activity_add_bank_linechart);
        //Radio Buttons to select time interval for the line chart (1 day, 30 days,...)
        mPeriodRadioGroup = (RadioGroup) findViewById(R.id.activity_add_bank_period_radiogroup);
        mPeriodRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                View radioButton = mPeriodRadioGroup.findViewById(checkedId);
                int index = mPeriodRadioGroup.indexOfChild(radioButton);

                // Add logic here

                switch (index) {
                    case 0: // first button

                        Toast.makeText(getApplicationContext(), "Selected button number " + index, Toast.LENGTH_LONG).show();
                        break;
                    case 1: // secondbutton

                        Toast.makeText(getApplicationContext(), "Selected button number " + index, Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
        configureLineChart();
        /*
        Confirm button
        When pressed, get value from text field and return it
         */
        confirmButton = (Button)findViewById(R.id.mConfirmRemoveFunds);
        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    /*
                    Get the value from the text field and return it to parent activity
                     */
                    int fundsAdded = Integer.valueOf(mFundsAdded.getText().toString());
                    Intent data = new Intent();
                    data.putExtra("funds added", fundsAdded);
                    setResult(RESULT_OK, data);
                    finish();
                }
                catch (Exception e) {
                    /*
                    Something went wrong, cancel
                     */
                    Intent data = new Intent();
                    setResult(RESULT_CANCELED, data);
                    finish();
                }
            }
        });

        /*
        Cancel button, returns to parent activity
         */
        cancelButton = (Button) findViewById(R.id.mCancelAddBalance);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    private void configureLineChart() {
        Description desc = new Description();
        desc.setText(String.valueOf(R.string.mLineChart));
        desc.setTextSize(28);
        mLineChart.setDescription(desc);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float value) {
                long millis = (long) value * 1000L;
                return mFormat.format(new Date(millis));
            }
        });
    }

    public static Intent newIntent(Context packageContext){
        Intent i = new Intent(packageContext, AddBankActivity.class);
        return i;
    }
}
