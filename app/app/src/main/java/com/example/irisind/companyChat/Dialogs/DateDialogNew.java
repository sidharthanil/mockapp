package com.example.irisind.companyChat.Dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.DatePicker;

import com.example.irisind.companyChat.Utilities.SetData;

import java.util.Calendar;

/**
 * Created by irisind on 23/1/17.
 */

public class DateDialogNew extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private Boolean shouldPickTime = true;
    private String key;

    public DateDialogNew() {

    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setKey(String key, Boolean shouldPickTime) {
        this.key = key;
        this.shouldPickTime = shouldPickTime;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth, 0, 0);
        if (!shouldPickTime) {
            ((SetData) getActivity()).setData(key, calendar);
            return;
        }
        TimeDialogNew dialog = new TimeDialogNew();
        dialog.setKey(key, calendar);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        dialog.show(fragmentTransaction, "TimePicker");

    }

}

