package com.example.rsu_itcjapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;

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
}
