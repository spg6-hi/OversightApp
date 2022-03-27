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

import java.util.ArrayList;
import java.util.HashMap;

public class CreateSpendingPlanPage extends AppCompatActivity {

    private static DummyNetwork network = MainActivity.getDm();

    private SpendingPlanService spendingPlanService = new SpendingPlanService(network);

    private HashMap<Category, Integer> spendingPlan;

    private ArrayList<EditText> categories;

    private int parseInt = 1;

    //FXML objects
    private Button saveButton, cancelButton;
    private EditText carsandtransportation, children, education, finesandfees, food,
            healthandbeauty, home, insurance, investmentsandsavings, leisuretime,
            shoppingandservices, other, vacationandtravel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createspendingplan);

        saveButton = (Button)findViewById(R.id.saveCreateSpendingPlan);
        cancelButton = (Button)findViewById(R.id.cancelCreateSpendingPlan);

        categories = new ArrayList<EditText>();
        spendingPlan = new HashMap<Category, Integer>();

        carsandtransportation = (EditText)findViewById(R.id.csp1);
        categories.add(carsandtransportation);
        children = (EditText)findViewById(R.id.csp2);
        categories.add(children);
        education = (EditText)findViewById(R.id.csp3);
        categories.add(education);
        finesandfees = (EditText)findViewById(R.id.csp4);
        categories.add(finesandfees);
        food = (EditText)findViewById(R.id.csp5);
        categories.add(food);
        healthandbeauty = (EditText)findViewById(R.id.csp6);
        categories.add(healthandbeauty);
        home = (EditText)findViewById(R.id.csp7);
        categories.add(home);
        insurance = (EditText)findViewById(R.id.csp8);
        categories.add(insurance);
        investmentsandsavings = (EditText)findViewById(R.id.csp9);
        categories.add(investmentsandsavings);
        leisuretime = (EditText)findViewById(R.id.csp10);
        categories.add(leisuretime);
        shoppingandservices = (EditText)findViewById(R.id.csp11);
        categories.add(shoppingandservices);
        other = (EditText)findViewById(R.id.csp12);
        categories.add(other);
        vacationandtravel = (EditText)findViewById(R.id.csp13);
        categories.add(vacationandtravel);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                int index = 0;
                Category[] cats = Category.getValues();
                for (EditText e : categories){
                    try {
                        Integer a = Integer.valueOf(e.getText().toString());
                        spendingPlan.put(cats[index], a);
                    }
                    catch (Exception r) {
                    }
                    index ++;
                }
                try {
                    Intent data = new Intent();
                    data.putExtra("new spending plan", spendingPlan);
                    setResult(RESULT_OK, data);
                    finish();
                }
                catch (Exception e){
                    Intent data = new Intent();
                    setResult(RESULT_CANCELED, data);
                    finish();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                setResult(RESULT_CANCELED, data);
                finish();
            }
        });
    }
}
