package com.example.irisind.companyChat.Dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;


import com.example.irisind.companyChat.Utilities.SetData;

import java.util.Calendar;

/**
 * Created by sid on 24-09-2016
 */
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private String key;

    public DateDialog() {

    }

    public void setKey(String key) {
        //txtdate = (TextView)view;
        this.key = key;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Log.e("Datedialog","created");
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth, 0, 0);
        ((SetData) getActivity()).setData(key,calendar);



    }

}
