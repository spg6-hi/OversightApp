package com.example.oversighttest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oversighttest.R;
import com.example.oversighttest.entities.Category;
import com.example.oversighttest.entities.Session;
import com.example.oversighttest.entities.SpendingPlan;

import java.util.ArrayList;
import java.util.HashMap;

public class EditSpendingPlanActivity extends AppCompatActivity {
    private Button saveButton, cancelButton;
    private EditText carsandtransportation, children, education, finesandfees, food,
            healthandbeauty, home, insurance, investmentsandsavings, leisuretime,
            shoppingandservices, other, vacationandtravel;


    private ArrayList<EditText> categories;

    private int parseInt = 1; //index for putting data in spending plan

    private SpendingPlan spendingPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editspendingplan);


        spendingPlan = Session.getInstance().getSpendingPlan();

        saveButton = (Button)findViewById(R.id.saveCreateSpendingPlan);
        cancelButton = (Button)findViewById(R.id.cancelCreateSpendingPlan);

        categories = new ArrayList<EditText>();

        /*
        connect to views
        also add each view to a list to loop through later
         */
        carsandtransportation = (EditText)findViewById(R.id.esp1);
        categories.add(carsandtransportation);
        children = (EditText)findViewById(R.id.esp2);
        categories.add(children);
        education = (EditText)findViewById(R.id.esp3);
        categories.add(education);
        finesandfees = (EditText)findViewById(R.id.esp4);
        categories.add(finesandfees);
        food = (EditText)findViewById(R.id.esp5);
        categories.add(food);
        healthandbeauty = (EditText)findViewById(R.id.esp6);
        categories.add(healthandbeauty);
        home = (EditText)findViewById(R.id.esp7);
        categories.add(home);
        insurance = (EditText)findViewById(R.id.esp8);
        categories.add(insurance);
        investmentsandsavings = (EditText)findViewById(R.id.esp9);
        categories.add(investmentsandsavings);
        leisuretime = (EditText)findViewById(R.id.esp10);
        categories.add(leisuretime);
        shoppingandservices = (EditText)findViewById(R.id.esp11);
        categories.add(shoppingandservices);
        other = (EditText)findViewById(R.id.esp12);
        categories.add(other);
        vacationandtravel = (EditText)findViewById(R.id.esp13);
        categories.add(vacationandtravel);

        /*
        Set values of text fields
         */
        ArrayList<Category> cats = Category.getCategories();
        HashMap<Category, Integer> plan = spendingPlan.getPlan();
        for(Category c : cats) {
            if(parseInt == 1) {
                carsandtransportation.setText(Integer.toString(plan.get(c)));
            }
            if(parseInt == 2) {
                children.setText(Integer.toString(plan.get(c)));
            }
            if(parseInt == 3) {
                education.setText(Integer.toString(plan.get(c)));
            }
            if(parseInt == 4) {
                finesandfees.setText(Integer.toString(plan.get(c)));
            }
            if(parseInt == 5) {
                food.setText(Integer.toString(plan.get(c)));
            }
            if(parseInt == 13) {
                healthandbeauty.setText(Integer.toString(plan.get(c)));
            }
            if(parseInt == 6) {
                home.setText(Integer.toString(plan.get(c)));
            }
            if(parseInt == 7) {
                insurance.setText(Integer.toString(plan.get(c)));
            }
            if(parseInt == 8) {
                investmentsandsavings.setText(Integer.toString(plan.get(c)));
            }
            if(parseInt == 9) {
                leisuretime.setText(Integer.toString(plan.get(c)));
            }
            if(parseInt == 10) {
                shoppingandservices.setText(Integer.toString(plan.get(c)));
            }
            if(parseInt == 11) {
                other.setText(Integer.toString(plan.get(c)));
            }
            if(parseInt == 12) {
                vacationandtravel.setText(Integer.toString(plan.get(c)));
            }
            parseInt++;
        };

        /*
        get each value and put in hash map to create a spending plan
         */
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                HashMap<Category, Integer> plan = new HashMap<Category, Integer>();
                int index = 0;
                Category[] cats = Category.getValues();
                for (EditText e : categories){
                    try {
                        //get value and add it to the plan
                        Integer a = Integer.valueOf(e.getText().toString());
                        plan.put(cats[index], a);
                    }
                    catch (Exception r){
                        //no value, so do nothing
                    }
                    index ++;
                }
                try {
                    //return the plan
                    Intent data = new Intent();
                    data.putExtra("edited spending plan", plan);
                    setResult(RESULT_OK, data);
                    finish();
                }
                catch (Exception e){
                    //return CANCELED
                    Intent data = new Intent();
                    setResult(RESULT_CANCELED, data);
                    finish();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }
}
