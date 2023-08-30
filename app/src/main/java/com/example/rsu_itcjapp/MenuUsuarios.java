package com.example.rsu_itcjapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.rsu_itcjapp.datos.Alumno;
import com.example.rsu_itcjapp.datos.DatabaseSGA;
import com.example.rsu_itcjapp.datos.Usuario;
import com.example.rsu_itcjapp.listView.DataList;
import com.example.rsu_itcjapp.listView.ListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuUsuarios extends AppCompatActivity {

    private ArrayList<DataList> coordinadorAdapter;
    private ListAdapter listAdapterCoordinador;

    private ArrayList<DataList> alumnoAdapter;
    private ListAdapter listAdapterAlumno;

    private ListView listView;

    private HashMap<String, Integer> layouts;
    private DatabaseSGA databaseSGA;
    private Usuario usuario;
    private Alumno alumno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_usuario);

        databaseSGA = new DatabaseSGA();

        Bundle bundle = getIntent().getExtras();
        String usuarioSeleccionado = (String) bundle.get(Constantes.USUARIO);
        usuario = (Usuario) bundle.getSerializable(Constantes.USER_DATA);
        listView = (ListView) findViewById(R.id.list_view);

        if (usuarioSeleccionado.equals(Constantes.USUARIO_ALUMNO)) {
            alumno = (Alumno) bundle.getSerializable(Constantes.USER_DATA);
            crearMenuAlumno(listView);
        } else if (usuarioSeleccionado.equals(Constantes.USUARIO_DOCENTE)) {
            crearMenuCoordinador(listView);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        if(databaseSGA.getUser() == null) {
            Intent pantallaInicio = new Intent(MenuUsuarios.this, MainActivity.class);
            startActivity(pantallaInicio);
        }
    }


    public void crearMenuAlumno(ListView listAlumno) {
        layouts = new HashMap<>();
        alumnoAdapter = new ArrayList<>();

        layouts.put(Constantes.REC, R.layout.layout_reciclaje);
        layouts.put(Constantes.MPT, R.layout.layout_marcadores_pilas);
        layouts.put(Constantes.RSP, R.layout.layout_residuos_peligrosos);
        layouts.put(Constantes.STAR, R.layout.layout_sistema_riego);

        alumnoAdapter.add(new DataList(alumno.getArea(), R.drawable.ic_baseline_assignment_24));
        alumnoAdapter.add(new DataList(Constantes.EMAIL, R.drawable.ic_baseline_email_24));
        alumnoAdapter.add(new DataList(Constantes.PERFIL, R.drawable.ic_baseline_account_circle_24));

        listAdapterAlumno = new ListAdapter(this, alumnoAdapter);

        AdapterView.OnItemClickListener itemClickListenerAlumno = new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long id) {
                Integer layout = layouts.get(alumno.getArea());

                if(layout == null) {
                    layout = R.layout.layout_residuos_peligrosos;
                }

                Intent opcionMenu = new Intent(getApplicationContext(), OpcionesMenuAlumno.class);
                opcionMenu.putExtra(Constantes.LAYOUT, layout);
                opcionMenu.putExtra(Constantes.OPCION_MENU_ID, (int) id);
                opcionMenu.putExtra(Constantes.USUARIO_ALUMNO, alumno);
                startActivity(opcionMenu);
            }
        };

        listAlumno.setAdapter(listAdapterAlumno);
        listAlumno.setOnItemClickListener(itemClickListenerAlumno);
    }

    public void crearMenuCoordinador(ListView listCoordinador) {
        coordinadorAdapter = new ArrayList<>();

        coordinadorAdapter.add(new DataList(Constantes.AVISO, R.drawable.ic_baseline_assignment_24));
        coordinadorAdapter.add(new DataList(Constantes.REPORTE_BIMESTRAL, R.drawable.ic_baseline_assignment_24));
        coordinadorAdapter.add(new DataList(Constantes.PERFIL, R.drawable.ic_baseline_account_circle_24));

        listAdapterCoordinador = new ListAdapter(this, coordinadorAdapter);

        AdapterView.OnItemClickListener itemClickListenerCoord = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long id){
                Intent opcionMenuCoord = new Intent(getApplicationContext(), OpcionesMenuCoord.class);
                opcionMenuCoord.putExtra(Constantes.OPCION_MENU_ID, (int) id);
                opcionMenuCoord.putExtra(Constantes.USUARIO_DOCENTE, usuario);
                startActivity(opcionMenuCoord);
            }
        };

        listCoordinador.setAdapter(listAdapterCoordinador);
        listCoordinador.setOnItemClickListener(itemClickListenerCoord);
    }
}