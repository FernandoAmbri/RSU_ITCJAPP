package com.example.rsu_itcjapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class OpcionesMenuCoord extends AppCompatActivity {

    public static final String OPCION_MENU = "opcionId";

    int opciones [] = {
            R.layout.layout_generar_aviso, R.layout.layout_reporte_alumnos_bimestral,
            R.layout.layout_reporte_alumnos_final
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int opcionSeleccionada = (Integer) getIntent().getExtras().get(OPCION_MENU);
        setContentView(opciones[opcionSeleccionada]);


    }
}