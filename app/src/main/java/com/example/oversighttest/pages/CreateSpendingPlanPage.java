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
import com.example.oversighttest.services.SpendingPlanService;

import java.util.HashMap;

public class CreateSpendingPlanPage extends AppCompatActivity {

    private static DummyNetwork network = MainActivity.getDm();

    private SpendingPlanService spendingPlanService = new SpendingPlanService(network);

    private HashMap<Category, Integer> spendingPlan;

    private int parseInt = 1;

    private Button saveButton, cancelButton;
    private EditText carsandtransportation, children, education, finesandfees, food,
            healthandbeauty, home, insurance, investmentsandsavings, leisuretime,
            shoppingandservices, other, vacationandtravel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createspendingplan);

        saveButton = (Button)findViewById(R.id.saveButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);

        carsandtransportation = (EditText)findViewById(R.id.csp1);
        children = (EditText)findViewById(R.id.csp2);
        education = (EditText)findViewById(R.id.csp3);
        finesandfees = (EditText)findViewById(R.id.csp4);
        food = (EditText)findViewById(R.id.csp5);
        healthandbeauty = (EditText)findViewById(R.id.csp6);
        home = (EditText)findViewById(R.id.csp7);
        insurance = (EditText)findViewById(R.id.csp8);
        investmentsandsavings = (EditText)findViewById(R.id.csp9);
        leisuretime = (EditText)findViewById(R.id.csp10);
        shoppingandservices = (EditText)findViewById(R.id.csp11);
        other = (EditText)findViewById(R.id.csp12);
        vacationandtravel = (EditText)findViewById(R.id.csp13);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                /*
                Category[] categories = Category.values();
                for(Category c : categories) {
                    if(parseInt == 1) {
                        spendingPlan.put(c, Integer.parseInt(carsandtransportation.getText().toString()));

                    }
                    if(parseInt == 2) {
                        spendingPlan.put(c, Integer.parseInt(carsandtransportation.getText().toString()));

                    }
                    if(parseInt == 3) {
                        spendingPlan.put(c, Integer.parseInt(carsandtransportation.getText().toString()));

                    }
                    if(parseInt == 4) {
                        spendingPlan.put(c, Integer.parseInt(carsandtransportation.getText().toString()));

                    }
                    if(parseInt == 5) {
                        spendingPlan.put(c, Integer.parseInt(carsandtransportation.getText().toString()));

                    }
                    if(parseInt == 6) {
                        spendingPlan.put(c, Integer.parseInt(carsandtransportation.getText().toString()));

                    }
                    if(parseInt == 7) {
                        spendingPlan.put(c, Integer.parseInt(carsandtransportation.getText().toString()));

                    }
                    if(parseInt == 8) {
                        spendingPlan.put(c, Integer.parseInt(carsandtransportation.getText().toString()));

                    }
                    if(parseInt == 9) {
                        spendingPlan.put(c, Integer.parseInt(carsandtransportation.getText().toString()));

                    }
                    if(parseInt == 10) {
                        spendingPlan.put(c, Integer.parseInt(carsandtransportation.getText().toString()));

                    }
                    if(parseInt == 11) {
                        spendingPlan.put(c, Integer.parseInt(carsandtransportation.getText().toString()));

                    }
                    if(parseInt == 12) {
                        spendingPlan.put(c, Integer.parseInt(carsandtransportation.getText().toString()));

                    }
                    if(parseInt == 13) {
                        spendingPlan.put(c, Integer.parseInt(carsandtransportation.getText().toString()));

                    }
                    parseInt++;
                }
                */
                //spendingPlanService.setSpendingPlan(spendingPlan);
                //spendingPlan.put()
                Intent intent = new Intent(CreateSpendingPlanPage.this, MainActivity.class);

                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(CreateSpendingPlanPage.this, MainActivity.class);

                startActivity(intent);
            }
        });
    }
}
