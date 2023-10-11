package com.example.rsu_itcjapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.rsu_itcjapp.db.DatabaseSGA;

public class MainActivity extends AppCompatActivity {


    private DatabaseSGA databaseSGA;
    private Button btnAlumno, btnCoordinador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseSGA = new DatabaseSGA(MainActivity.this);

        btnAlumno = (Button) findViewById(R.id.btn_alumno);
        btnCoordinador = (Button) findViewById(R.id.btn_coordinador);

        btnAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initLogin(Constantes.USUARIO, Constantes.USUARIO_ALUMNO);
            }
        });

        btnCoordinador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initLogin(Constantes.USUARIO, Constantes.USUARIO_DOCENTE);
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        if (databaseSGA.getUser() != null) {
            databaseSGA.obtenerUsuario(Constantes.USUARIO, Constantes.DATOS_USUARIO);
        }
    }

    public void initLogin(String usuario, String tipoUsuario) {
        Intent intent = new Intent(MainActivity.this, LoginUsuarios.class);
        intent.putExtra(usuario, tipoUsuario);
        startActivity(intent);
        finish();
    }
}