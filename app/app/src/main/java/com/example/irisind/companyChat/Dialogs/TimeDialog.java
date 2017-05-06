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
 * Created by sid on 24-09-2016
 */
public class TimeDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


    private Calendar calendar;
    private String key;

    public TimeDialog() {

    }


    public void setKey(String key) {

        this.key = key;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current end as the default values for the picker
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        // Do something with the end chosen by the user

        //datetime.setTimeInMillis((hourOfDay*60+minute)*60*1000);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
        ((SetData) getActivity()).setData(key, calendar);

    }
}