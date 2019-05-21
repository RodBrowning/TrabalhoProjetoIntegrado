package com.example.tesouradouradaapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment {
    Calendar calendar;

    public DatePickerFragment(Calendar calendar) {
        this.calendar = calendar;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        Dialog datePickerDialog = new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, date);
        ((DatePickerDialog) datePickerDialog).getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return datePickerDialog;
    }
}
