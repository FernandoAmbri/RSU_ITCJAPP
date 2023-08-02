package com.example.rsu_itcjapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rsu_itcjapp.datos.Alumno;
import com.example.rsu_itcjapp.listView.DataList;
import com.example.rsu_itcjapp.listView.ListAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class MenuUsuarios extends AppCompatActivity {

    ArrayList<DataList> coordinadorAdapter;
    private static ListAdapter listAdapterCoordinador;

    ArrayList<DataList> alumnoAdapter;
    private static ListAdapter listAdapterAlumno;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_usuario);

        String usuarioSeleccionado = (String) getIntent().getExtras().get(LoginUsuarios.OPCION);

        // Alumno adapter

        alumnoAdapter = new ArrayList<>();

        alumnoAdapter.add(new DataList("Reciclaje", R.drawable.ic_baseline_assignment_24));
        alumnoAdapter.add(new DataList("Marcadores y pilas", R.drawable.ic_baseline_assignment_24));
        alumnoAdapter.add(new DataList("Residuos peligrosos", R.drawable.ic_baseline_assignment_24));
        alumnoAdapter.add(new DataList("Sistema de riego", R.drawable.ic_baseline_assignment_24));
        alumnoAdapter.add(new DataList("Enviar correo", R.drawable.ic_baseline_email_24));
        alumnoAdapter.add(new DataList("Ver cuenta", R.drawable.ic_baseline_account_circle_24));

        listAdapterAlumno = new ListAdapter(this, alumnoAdapter);

        AdapterView.OnItemClickListener itemClickListenerAlumno = new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long id) {
                Intent opcionMenu = new Intent(getApplicationContext(), OpcionesMenuAlumno.class);
                opcionMenu.putExtra(OpcionesMenuAlumno.OPCION_MENU, (int) id);
                startActivity(opcionMenu);
            }
        };

        //Coordinador adapter

        coordinadorAdapter = new ArrayList<>();

        coordinadorAdapter.add(new DataList("Generar aviso", R.drawable.ic_baseline_assignment_24));
        coordinadorAdapter.add(new DataList("Reporte alumnos bimestral", R.drawable.ic_baseline_assignment_24));
        coordinadorAdapter.add(new DataList("Reporte alumnos final", R.drawable.ic_baseline_assignment_24));


        listAdapterCoordinador = new ListAdapter(this, coordinadorAdapter);

        AdapterView.OnItemClickListener itemClickListenerCoord = new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long id){
               Intent opcionMenuCoord = new Intent(getApplicationContext(), OpcionesMenuCoord.class);
               opcionMenuCoord.putExtra(OpcionesMenuCoord.OPCION_MENU, (int) id);
               startActivity(opcionMenuCoord);
           }
        };

        listView = (ListView) findViewById(R.id.list_view);

        if(usuarioSeleccionado.equals(MainActivity.USUARIOS[0])) {

            listView.setAdapter(listAdapterCoordinador);
            listView.setOnItemClickListener(itemClickListenerCoord);

        } else if(usuarioSeleccionado.equals(MainActivity.USUARIOS[1])) {

            listView.setAdapter(listAdapterAlumno);
            listView.setOnItemClickListener(itemClickListenerAlumno);

        }

    }
}