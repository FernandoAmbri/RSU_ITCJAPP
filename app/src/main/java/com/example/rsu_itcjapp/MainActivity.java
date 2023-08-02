package com.example.rsu_itcjapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnAlumno, btnCoordinador;
    public static final String [] USUARIOS = {"Coordinador", "Alumnos"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAlumno = (Button) findViewById(R.id.btn_alumno);
        btnCoordinador = (Button) findViewById(R.id.btn_coordinador);

        btnAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginUsuarios.class);
                intent.putExtra(LoginUsuarios.OPCION, USUARIOS[1]);
                startActivity(intent);
            }
        });

        btnCoordinador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginUsuarios.class);
                intent.putExtra(LoginUsuarios.OPCION, USUARIOS[0]);
                startActivity(intent);
            }
        });

    }
}