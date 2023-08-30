package com.example.rsu_itcjapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rsu_itcjapp.datos.Alumno;
import com.example.rsu_itcjapp.datos.Aviso;
import com.example.rsu_itcjapp.datos.DatabaseSGA;
import com.example.rsu_itcjapp.datos.Usuario;
import com.example.rsu_itcjapp.listView.DatosReporte;
import com.example.rsu_itcjapp.listView.ReportesAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class OpcionesMenuCoord extends AppCompatActivity {

    private final int [] opciones = {
            R.layout.layout_generar_aviso, R.layout.layout_reportes_alumnos_servicio,
            R.layout.layout_informacion_usuario
    };

    private ReportesAdapter adapterReporteBim;
    private ArrayList<DatosReporte> datosReportesBim;

    private DatabaseSGA databaseSGA;
    private Usuario docente;
    private Calendar fechaActual;
    private HashMap<String, String> fechaServicio;
    private SimpleDateFormat smf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        docente = (Usuario) getIntent().getExtras().get(Constantes.USUARIO_DOCENTE);
        int opcionSeleccionada = (Integer) getIntent().getExtras().get(Constantes.OPCION_MENU_ID);
        setContentView(opciones[opcionSeleccionada]);

        databaseSGA = new DatabaseSGA();
        fechaServicio = new HashMap<>();

        smf = new SimpleDateFormat("dd/MM/yyyy");
        fechaActual = Calendar.getInstance();

        seleccionarLayout(opcionSeleccionada);
    }

    @Override
    public void onStart(){
        super.onStart();
        if(databaseSGA.getUser() == null){
            Intent pantallaInicio = new Intent(OpcionesMenuCoord.this, MainActivity.class);
            startActivity(pantallaInicio);
        }
    }

    private void seleccionarLayout(int opcionSeleccionada){
        switch(opcionSeleccionada) {
            case 0:
                generarAviso();
                break;
            case 1:
                mostrarReporteBimestral();
                break;
            case 2:
                mostrarDatosCuenta();
                break;
        }
    }

    private void generarAviso() {
        final String path = "aviso/0";
        TextInputEditText txtTitulo = (TextInputEditText) findViewById(R.id.tie_titulo_aviso);
        TextInputEditText txtCorreo = (TextInputEditText) findViewById(R.id.tie_correo_aviso);
        TextInputEditText txtInformacion = (TextInputEditText) findViewById(R.id.tie_informacion_aviso);
        Button btnGuardarAviso = (Button) findViewById(R.id.btn_guardar_aviso);

        databaseSGA.getDbRef().child(path).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                Aviso aviso = task.getResult().getValue(Aviso.class);
                if(aviso != null) {
                    txtTitulo.setText(aviso.getTitulo());
                    txtCorreo.setText(aviso.getCorreo());
                    txtInformacion.setText(aviso.getInfo());
                }
            }
        });

        btnGuardarAviso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tituloAviso = txtTitulo.getText().toString().trim();
                String correo = txtCorreo.getText().toString().trim();
                String info = txtInformacion.getText().toString().trim();

                if (tituloAviso.isEmpty() || correo.isEmpty() || info.isEmpty()) {
                    Toast.makeText(OpcionesMenuCoord.this, "Campos vacíos",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                Aviso aviso = new Aviso(correo, info, tituloAviso);
                databaseSGA.getDbRef().child(path).setValue(aviso)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(OpcionesMenuCoord.this,
                                            "Aviso guardado correctamente.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(OpcionesMenuCoord.this, e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private int calcularReporte(HashMap<String, String> fechaServicio) {
        final String fechaServicioAlum = fechaServicio.get("dia")+"/"+fechaServicio.get("mes")
                +"/"+fechaServicio.get("anho");
        int rep = 0;

        Calendar fechaServicioInicio = Calendar.getInstance();

        try {
            Date dateServicio = smf.parse(fechaServicioAlum);
            fechaServicioInicio.setTime(dateServicio);

            int anhos = fechaActual.get(Calendar.YEAR) - fechaServicioInicio.get(Calendar.YEAR);
            int dias = fechaActual.get(Calendar.DAY_OF_YEAR) - fechaServicioInicio.get(Calendar.DAY_OF_YEAR);

            if(anhos > 0 || dias > 240) return rep;

            if (dias >= 180) rep = 3;
            else if (dias >= 120) rep = 2;
            else if (dias >= 60) rep = 1;

        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }

        return rep;
    }

    private void mostrarReporteBimestral () {

        final String reporte = "Reporte ";

        ListView listReportesBim = (ListView) findViewById(R.id.list_reportes_bimestral);

        datosReportesBim = new ArrayList<>();

        databaseSGA.getDbRef().child(Constantes.USUARIOS)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable = snapshot.getChildren();
                datosReportesBim.clear();
                for (DataSnapshot dataSnapshot : snapshotIterable) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    if(usuario != null
                            && usuario.getTipo().equals(Constantes.USUARIO_ALUMNO)) {

                        Alumno alumno = dataSnapshot.getValue(Alumno.class);
                        fechaServicio = alumno.getResidenciasInicio();

                        int rep = calcularReporte(fechaServicio);

                        if(rep > 0) {
                            String nombreCompleto = alumno.getNombre() + " " +
                                    alumno.getApellidoPaterno() + " " + alumno.getApellidoMaterno();

                            DatosReporte datosReporte = new DatosReporte(nombreCompleto,
                                    String.valueOf(alumno.getMatricula()), alumno.getArea(),
                                    reporte + rep);

                            datosReportesBim.add(datosReporte);
                        }

                    }
                    adapterReporteBim.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(OpcionesMenuCoord.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        adapterReporteBim = new ReportesAdapter(getApplicationContext(), datosReportesBim);
        listReportesBim.setAdapter(adapterReporteBim);
    }

    private void mostrarDatosCuenta(){

        TextInputLayout tilCarrera = (TextInputLayout) findViewById(R.id.til_carrera_inf);
        TextInputLayout tilMatricula = (TextInputLayout) findViewById(R.id.til_matricula_inf);
        LinearLayout layoutFechas = (LinearLayout) findViewById(R.id.layout_fechas);

        TextInputEditText txtNombreUsuario = (TextInputEditText) findViewById(R.id.txt_nombre_inf);
        TextInputEditText txtApellidoPaterno = (TextInputEditText) findViewById(R.id.txt_apellidoPaterno_inf);
        TextInputEditText txtApellidoMaterno = (TextInputEditText) findViewById(R.id.txt_apellidoMaterno_inf);
        TextInputEditText txtArea = (TextInputEditText) findViewById(R.id.txt_area_inf);
        TextInputEditText txtCorreo = (TextInputEditText) findViewById(R.id.txt_correo_inf);

        Button btnCerrarSesion = (Button) findViewById(R.id.btn_cerrar_sesion);

        tilCarrera.setVisibility(View.GONE);
        tilMatricula.setVisibility(View.GONE);
        layoutFechas.setVisibility(View.GONE);

        txtNombreUsuario.setText(docente.getNombre());
        txtApellidoPaterno.setText(docente.getApellidoPaterno());
        txtApellidoMaterno.setText(docente.getApellidoMaterno());
        txtArea.setText(docente.getArea());
        txtCorreo.setText(databaseSGA.getUser().getEmail());

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               databaseSGA.getAuth().signOut();
               Toast.makeText(OpcionesMenuCoord.this,
                       "Sesión finalizada.", Toast.LENGTH_SHORT).show();
               Intent pantallaInicio = new Intent(OpcionesMenuCoord.this, MainActivity.class);
               startActivity(pantallaInicio);
           }
        });
    }

}