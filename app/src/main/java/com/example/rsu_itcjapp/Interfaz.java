package com.example.rsu_itcjapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.HashMap;

public class Interfaz {

    public static HashMap<String, String> setDatePicker(TextInputEditText textView, Context context){
        HashMap<String, String> fecha = new HashMap<>();

        final Calendar date = Calendar.getInstance();

        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String monthFinal = String.valueOf(month + 1);
                        String dayFinal = String.valueOf(day);

                        if(month + 1 < 10) monthFinal = "0" + monthFinal;

                        if(day < 10) dayFinal = "0" + dayFinal;

                        fecha.put("anho", String.valueOf(year));
                        fecha.put("mes", monthFinal);
                        fecha.put("dia", dayFinal);

                        textView.setText(dayFinal+"/"+monthFinal+"/"+year);
                    }
                }, year, month, day);
        datePickerDialog.show();

        return fecha;
    }

    public static void setTimePicker (TextInputEditText txtHoraInicio, Context context) {
        final Calendar time = Calendar.getInstance();
        int hour = time.get(Calendar.HOUR_OF_DAY);
        int minute = time.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int h, int m) {
                        String time = h+":"+m;
                        if(m < 10) time = h+":0"+m;
                        txtHoraInicio.setText(time);
                    }
                }, hour, minute, DateFormat.is24HourFormat(context));
        timePickerDialog.show();
    }
}
