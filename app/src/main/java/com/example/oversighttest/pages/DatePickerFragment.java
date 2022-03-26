package com.example.oversighttest.pages;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

/*
    This Fragment class handles the dialog pop up for selecting the Date in activity_add_transaction
    when the mShowDate button is pressed
 */

//https://developer.android.com/guide/topics/ui/controls/pickers
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Intent i = new Intent();


    }

}
