package com.example.oversighttest.subclasses;

import android.content.Context;
import android.os.Build;
import android.text.Selection;
import android.text.Spannable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.autofill.AutofillValue;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
    This is a helper class, a subclass of EditText to help autofill the selected Date from mShowDate
    into the mEditTextDate widget in activity_add_transaction.xml
 */
public class AutoFillDateFormEditText extends androidx.appcompat.widget.AppCompatEditText {

    private static final String TAG = "tag";

    public AutoFillDateFormEditText(Context context) {
        super(context);

    }
    public AutoFillDateFormEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoFillDateFormEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getAutofillType(){
        return AUTOFILL_TYPE_DATE;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void autofill(AutofillValue value) {
        if (!value.isDate()) {
            //Toast.makeText(getContext(),value + " could not be autofilled into " + this, Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Ignoring autofill() because service sent a non-date value:" + value);
            return;
        }

        long autofilledValue = value.getDateValue();

        // First autofill it...
        setText(dateFormat.format(new Date(autofilledValue)));
        // ...then move cursor to the end.
        final CharSequence text = getText();
        if ((text != null)) {
            Selection.setSelection((Spannable) text, text.length());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public AutofillValue getAutofillValue() {
        final AutofillValue autofillValue = super.getAutofillValue();
        if ( autofillValue == null)
            return null;
        final Date date = tryParse(autofillValue.getTextValue().toString());
        return date != null ? AutofillValue.forDate(date.getTime()) : autofillValue;
    }

    private final DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");

    java.util.Date tryParse(String dateString)
    {
        try
        {
            return dateFormat.parse(dateString);
        }
        catch (ParseException e) {
            return null;
        }
    }
}
