package com.example.oversighttest.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oversighttest.R;
import com.example.oversighttest.entities.Category;
import com.example.oversighttest.network.DummyNetwork;
import com.example.oversighttest.services.BankBalanceService;
import com.example.oversighttest.services.SpendingPlanService;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EditSpendingPlanPage extends AppCompatActivity {

    private static DummyNetwork network = MainActivity.getDm();

    private Button saveButton, cancelButton;
    private EditText carsandtransportation, children, education, finesandfees, food,
            healthandbeauty, home, insurance, investmentsandsavings, leisuretime,
            shoppingandservices, other, vacationandtravel;

    private SpendingPlanService spendingPlanService = new SpendingPlanService(network);

    private int parseInt = 1;

    private HashMap<Category, Integer> spendingPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editspendingplan);

        spendingPlan = spendingPlanService.getSpendingPlan();

        saveButton = (Button)findViewById(R.id.saveButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);

        carsandtransportation = (EditText)findViewById(R.id.esp1);
        children = (EditText)findViewById(R.id.esp2);
        education = (EditText)findViewById(R.id.esp3);
        finesandfees = (EditText)findViewById(R.id.esp4);
        food = (EditText)findViewById(R.id.esp5);
        healthandbeauty = (EditText)findViewById(R.id.esp6);
        home = (EditText)findViewById(R.id.esp7);
        insurance = (EditText)findViewById(R.id.esp8);
        investmentsandsavings = (EditText)findViewById(R.id.esp9);
        leisuretime = (EditText)findViewById(R.id.esp10);
        shoppingandservices = (EditText)findViewById(R.id.esp11);
        other = (EditText)findViewById(R.id.esp12);
        vacationandtravel = (EditText)findViewById(R.id.esp13);


        for(Map.Entry<Category, Integer> plan : spendingPlan.entrySet()) {
            if(parseInt == 1) {
                carsandtransportation.setText(Integer.toString(plan.getValue()));
            }
            if(parseInt == 2) {
                children.setText(Integer.toString(plan.getValue()));
            }
            if(parseInt == 3) {
                education.setText(Integer.toString(plan.getValue()));
            }
            if(parseInt == 4) {
                finesandfees.setText(Integer.toString(plan.getValue()));
            }
            if(parseInt == 5) {
                food.setText(Integer.toString(plan.getValue()));
            }
            if(parseInt == 13) {
                healthandbeauty.setText(Integer.toString(plan.getValue()));
            }
            if(parseInt == 6) {
                home.setText(Integer.toString(plan.getValue()));
            }
            if(parseInt == 7) {
                insurance.setText(Integer.toString(plan.getValue()));
            }
            if(parseInt == 8) {
                investmentsandsavings.setText(Integer.toString(plan.getValue()));
            }
            if(parseInt == 9) {
                leisuretime.setText(Integer.toString(plan.getValue()));
            }
            if(parseInt == 10) {
                shoppingandservices.setText(Integer.toString(plan.getValue()));
            }
            if(parseInt == 11) {
                other.setText(Integer.toString(plan.getValue()));
            }
            if(parseInt == 12) {
                vacationandtravel.setText(Integer.toString(plan.getValue()));
            }
            parseInt++;
        };


        //Put values of spending plan into EditText boxes

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                //Save the edited version
                Intent intent = new Intent(EditSpendingPlanPage.this, MainActivity.class);

                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(EditSpendingPlanPage.this, MainActivity.class);

                startActivity(intent);
            }
        });
    }
}
