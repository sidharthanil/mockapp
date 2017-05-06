package com.example.irisind.companyChat.Dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.example.irisind.companyChat.Utilities.SetData;

import java.util.Calendar;

/**
 * Created by irisind on 23/1/17.
 */

public class TimeDialogNew extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


    private Calendar datetime, date = null, time = null;
    private String key;


    public void setKey(String key, Calendar date) {
        this.datetime = date;
        this.key = key;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the current end as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        // Do something with the end chosen by the user

        //datetime.setTimeInMillis((hourOfDay*60+minute)*60*1000);
        datetime.set(datetime.get(Calendar.YEAR), datetime.get(Calendar.MONTH), datetime.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

        ((SetData) getActivity()).setData(key, datetime);
    }
}