package com.iskcon.pfh.folkcalling;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by i308830 on 7/8/17.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current date as the default date in the picker



        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        // Create a new instance of DatePickerDialog and return it
        TimePickerDialog dialog = new TimePickerDialog(getActivity(),this,hour,minute,true);



        return dialog;
        // return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    @Override
    public void onTimeSet(TimePicker TP, int hour, int minutes) {

        // Do something with the date chosen by the user

        try {
            ((Operations)getActivity()).getSelectedTime(hour,minutes);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
