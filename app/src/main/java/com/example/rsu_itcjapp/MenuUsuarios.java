package com.example.rsu_itcjapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.rsu_itcjapp.datos.Alumno;
import com.example.rsu_itcjapp.datos.Usuario;
import com.example.rsu_itcjapp.listView.DatosListaMenu;
import com.example.rsu_itcjapp.listView.ListaMenuAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuUsuarios extends AppCompatActivity {

    private Usuario usuario;
    private Alumno alumno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_usuario);
        ListView listView = (ListView) findViewById(R.id.list_view);

        Bundle bundle = getIntent().getExtras();
        String usuarioSeleccionado = (String) bundle.get(Constantes.USUARIO);

        if (usuarioSeleccionado.equals(Constantes.USUARIO_ALUMNO)) {
            alumno = (Alumno) bundle.getSerializable(Constantes.DATOS_USUARIO);
            crearOpcionesAlumno(listView);
        } else if (usuarioSeleccionado.equals(Constantes.USUARIO_DOCENTE)) {
            usuario = (Usuario) bundle.getSerializable(Constantes.DATOS_USUARIO);
            crearOpcionesCoordinador(listView);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        if(alumno == null && usuario == null) {
            Intent pantallaPrincipal = new Intent(MenuUsuarios.this, MainActivity.class);
            startActivity(pantallaPrincipal);
            finish();
        }
    }


    public void crearOpcionesAlumno(ListView listAlumno) {
        ArrayList<DatosListaMenu> alumnoAdapter = new ArrayList<>();
        HashMap<String, Integer> layouts = new HashMap<>();

        layouts.put(Constantes.REC, Constantes.RECICLAJE);
        layouts.put(Constantes.MPT, Constantes.MARCADORESPILAS);
        layouts.put(Constantes.RSP, Constantes.RESIDUOSPELIGROSOS);
        layouts.put(Constantes.STAR, Constantes.SISTEMADERIEGO);

        alumnoAdapter.add(new DatosListaMenu(alumno.getArea(), R.drawable.ic_baseline_assignment_24));
        alumnoAdapter.add(new DatosListaMenu(Constantes.EMAIL, R.drawable.ic_baseline_email_24));
        alumnoAdapter.add(new DatosListaMenu(Constantes.PERFIL, R.drawable.ic_baseline_account_circle_24));

        ListaMenuAdapter listaMenuAdapterAlumno = new ListaMenuAdapter(this, alumnoAdapter);

        AdapterView.OnItemClickListener itemClickListenerAlumno = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long id) {
                Integer layout = null;
                switch((int) id){
                    case 0:
                        layout = layouts.get(alumno.getArea());
                        break;
                    case 1:
                        layout = Constantes.ENVIARCORREO;
                        break;
                    case 2:
                        layout = Constantes.VERPERFIL;
                        break;
                }
                if (layout == null) {
                    layout = Constantes.RESIDUOSPELIGROSOS;
                }
                Intent opcionesMenu = new Intent(MenuUsuarios.this, OpcionesMenuAlumno.class);
                opcionesMenu.putExtra(Constantes.LAYOUT, layout);
                opcionesMenu.putExtra(Constantes.USUARIO_ALUMNO, alumno);
                startActivity(opcionesMenu);
            }
        };

        listAlumno.setAdapter(listaMenuAdapterAlumno);
        listAlumno.setOnItemClickListener(itemClickListenerAlumno);
    }

    public void crearOpcionesCoordinador(ListView listCoordinador) {
        ArrayList<DatosListaMenu> coordinadorAdapter = new ArrayList<>();

        coordinadorAdapter.add(new DatosListaMenu(Constantes.AVISO, R.drawable.ic_baseline_assignment_24));
        coordinadorAdapter.add(new DatosListaMenu(Constantes.REPORTE_BIMESTRAL, R.drawable.ic_baseline_assignment_24));
        coordinadorAdapter.add(new DatosListaMenu(Constantes.PERFIL, R.drawable.ic_baseline_account_circle_24));

        ListaMenuAdapter listaMenuAdapterCoordinador = new ListaMenuAdapter(this, coordinadorAdapter);

        AdapterView.OnItemClickListener itemClickListenerCoord = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long id) {
                Integer layout = null;

                switch((int) id) {
                    case 0:
                        layout = Constantes.GENERARAVISO;
                        break;
                    case 1:
                        layout = Constantes.ALUMNOSSERVICIO;
                        break;
                    case 2:
                        layout = Constantes.VERPERFIL;
                        break;
                }

                Intent opcionesMenuCoord = new Intent(MenuUsuarios.this, OpcionesMenuCoord.class);
                opcionesMenuCoord.putExtra(Constantes.LAYOUT, layout);
                opcionesMenuCoord.putExtra(Constantes.USUARIO_DOCENTE, usuario);
                startActivity(opcionesMenuCoord);
            }
        };

        listCoordinador.setAdapter(listaMenuAdapterCoordinador);
        listCoordinador.setOnItemClickListener(itemClickListenerCoord);
    }
}