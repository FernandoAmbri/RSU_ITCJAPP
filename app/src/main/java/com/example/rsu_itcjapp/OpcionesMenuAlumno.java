package com.example.rsu_itcjapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.number.IntegerWidth;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.rsu_itcjapp.datos.Alumno;
import com.example.rsu_itcjapp.datos.Bitacora;
import com.example.rsu_itcjapp.datos.MarcadoresPilas;
import com.example.rsu_itcjapp.datos.Reciclaje;
import com.example.rsu_itcjapp.datos.ResiduosPeligrosos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class OpcionesMenuAlumno extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private Alumno alumno;
    private String userId = "", nombre = "";
    private ArrayList<String> codigosPeligrosidad = new ArrayList<>();

    public static final String OPCION_MENU = "opcionId";

    int [] opcionesLayout = {R.layout.layout_reciclaje, R.layout.layout_marcadores_pilas,
                                R.layout.layout_residuos_peligrosos, R.layout.layout_sistema_riego,
                                R.layout.layout_enviar_correo, R.layout.layout_informacion_alumno};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int opcionSeleccionada = (Integer) getIntent().getExtras().get(OPCION_MENU);
        setContentView(opcionesLayout[opcionSeleccionada]);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();
        db = FirebaseDatabase.getInstance();

        dbRef = db.getReference("DB_RSUITCJ");

        obtenerDatosUsuario(opcionSeleccionada);

    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            Intent pantallaInicio = new Intent(OpcionesMenuAlumno.this, MainActivity.class);
            startActivity(pantallaInicio);
        }
    }

    public void obtenerDatosUsuario(int opcionSeleccionada) {

        dbRef.child("Alumnos").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Alumno alumno = snapshot.getValue(Alumno.class);
                seleccionarLayout(opcionSeleccionada, alumno);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(OpcionesMenuAlumno.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void seleccionarLayout(int opcionSeleccionada, Alumno alumno) {
        switch(opcionSeleccionada){
            case 0:
                registrarDatosReciclaje(alumno);
                break;
            case 1:
                registrarDatosMarcadoresPilas(alumno);
                break;
            case 2:
                registrarDatosResiduosPeligrosos(alumno);
                break;
            case 3:
                registrarDatosSistemaRiego(alumno);
                break;
            case 4:
                EnviarCorreo();
                break;
            case 5:
                mostrarDatosCuenta(alumno);
                break;
        }
    }

    private String establecerFecha() {
        final Calendar date = Calendar.getInstance();

        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1;
        int day = date.get(Calendar.DAY_OF_MONTH);

        String fecha = day + "-" + month + "-" + year;
        if(month < 10) fecha = day+"-"+"0"+(month)+"-"+year;

        return fecha;
    }

    private void registrarDatosReciclaje(Alumno alumno) {

        TextInputEditText txtTapasRec = (TextInputEditText) findViewById(R.id.tie_tapas_rec);
        TextInputEditText txtBotellas = (TextInputEditText) findViewById(R.id.tie_botellas_rec);
        TextInputEditText txtBotesAlum = (TextInputEditText) findViewById(R.id.tie_botes_alum);
        TextInputEditText txtFechaRec = (TextInputEditText) findViewById(R.id.tie_fecha_rec);
        Button btnReciclaje = (Button) findViewById(R.id.btn_reciclaje);

        txtFechaRec.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setDatePicker(txtFechaRec);
            }
        });

        btnReciclaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int cantidadTapas = Integer.parseInt(txtTapasRec.getText().toString().trim());
                int cantidadBotellas = Integer.parseInt(txtBotellas.getText().toString().trim());
                int botesAluminio = Integer.parseInt(txtBotesAlum.getText().toString().trim());
                String fecha = txtFechaRec.getText().toString();

                if(cantidadTapas == 0 && cantidadBotellas == 0 && botesAluminio == 0) {
                    Toast.makeText(OpcionesMenuAlumno.this,
                                    "Es necesario introducir una cantidad.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(fecha)) {
                    Toast.makeText(OpcionesMenuAlumno.this,
                            "Es necesario introducir la fecha.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Reciclaje reciclaje = new Reciclaje(new Alumno(alumno.getNombre(),
                            alumno.getApellidos(), alumno.getMatricula()), fecha, cantidadTapas,
                                cantidadBotellas, botesAluminio);

                String key = dbRef.child("Reciclaje").push().getKey();
                String finalKey = key + "-" + establecerFecha();
                dbRef.child("Reciclaje").child(key).removeValue();

                dbRef.child("Reciclaje").child(finalKey).setValue(reciclaje).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        Toast.makeText(OpcionesMenuAlumno.this, "Registro guardado.",
                                        Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(OpcionesMenuAlumno.this, e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

                txtTapasRec.setText("0");
                txtBotellas.setText("0");
                txtBotesAlum.setText("0");
                txtFechaRec.setText("");

            }
        });

    }

    private void registrarDatosMarcadoresPilas(Alumno alumno) {

        TextInputEditText txtMarcadores = (TextInputEditText) findViewById(R.id.tie_marcadores);
        AutoCompleteTextView actMarcadores = (AutoCompleteTextView) findViewById(R.id.act_marcadores);
        TextInputEditText txtPilas = (TextInputEditText) findViewById(R.id.tie_pilas);
        AutoCompleteTextView actPilas = (AutoCompleteTextView) findViewById(R.id.act_pilas);
        TextInputEditText txtToners = (TextInputEditText) findViewById(R.id.tie_toners);
        AutoCompleteTextView actToners = (AutoCompleteTextView) findViewById(R.id.act_toners);
        TextInputEditText txtFechaMarcadores = (TextInputEditText) findViewById(R.id.tie_fecha_marcadores);

        Button btnMarcadores = (Button) findViewById(R.id.btn_marcadores_pilas);

        String [] departamentosInstituto = getResources().getStringArray(R.array.departamentos);

        ArrayAdapter<String> adapterDepartamento =
                new ArrayAdapter<>(this, R.layout.dropdown_menu, departamentosInstituto);

        actMarcadores.setAdapter(adapterDepartamento);
        actPilas.setAdapter(adapterDepartamento);
        actToners.setAdapter(adapterDepartamento);

        txtFechaMarcadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDatePicker(txtFechaMarcadores);
            }
        });

        btnMarcadores.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               int cantidadMarcadores = Integer.parseInt(txtMarcadores.getText().toString());
               String depMarcadores = actMarcadores.getText().toString();
               int cantidadPilas = Integer.parseInt(txtPilas.getText().toString());
               String depPilas = actPilas.getText().toString();
               int cantidadToners = Integer.parseInt(txtToners.getText().toString());
               String depToners = actToners.getText().toString();
               String fecha = txtFechaMarcadores.getText().toString();

               if(cantidadMarcadores == 0 && cantidadPilas == 0 && cantidadToners == 0) {
                   Toast.makeText(OpcionesMenuAlumno.this,
                            "Es necesario introducir una cantidad.", Toast.LENGTH_SHORT).show();
                    return;
               }

               if((cantidadMarcadores > 0 && TextUtils.isEmpty(depMarcadores))
                    || (cantidadPilas > 0 && TextUtils.isEmpty(depPilas))
                        || (cantidadToners > 0 && TextUtils.isEmpty(depToners))) {
                   Toast.makeText(OpcionesMenuAlumno.this,
                           "Es necesario introducir un departamento.", Toast.LENGTH_SHORT).show();
                   return;
               }

               if(TextUtils.isEmpty(fecha)) {
                   Toast.makeText(OpcionesMenuAlumno.this,
                           "Es necesario introducir la fecha.", Toast.LENGTH_SHORT).show();
                   return;
               }

               Alumno alum = new Alumno(alumno.getNombre(), alumno.getApellidos(), alumno.getMatricula());
               MarcadoresPilas marcadoresPilas = new MarcadoresPilas(alum, fecha, cantidadMarcadores,
                                    depMarcadores, cantidadPilas, depPilas, cantidadToners, depToners);

               String key = dbRef.child("MarcadoresPilasToners").push().getKey();
               String finalKey = key + "-" + establecerFecha();
               dbRef.child("MarcadoresPilasToners").child(key).removeValue();

               dbRef.child("MarcadoresPilasToners")
                    .child(finalKey)
                    .setValue(marcadoresPilas)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            Toast.makeText(OpcionesMenuAlumno.this, "Registro guardado.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(Exception e) {
                           Toast.makeText(OpcionesMenuAlumno.this, e.getMessage(),
                                   Toast.LENGTH_SHORT).show();
                       }
                   });
           }
        });

    }

    public void modificarCodigosPeligrosidad(boolean seleccionado, String codigo) {
        if(seleccionado) {
            codigosPeligrosidad.add(codigo);
            return;
        }
        int indice = codigosPeligrosidad.indexOf(codigo);
        codigosPeligrosidad.remove(indice);
    }

    public void seleccionarCodigoPeligrosidad(View view) {
        boolean seleccionado = ((CheckBox) view).isChecked();

        switch(view.getId()){
            case R.id.ckb_C:
                modificarCodigosPeligrosidad(seleccionado, "C");
                break;
            case R.id.ckb_R:
                modificarCodigosPeligrosidad(seleccionado, "R");
                break;
            case R.id.ckb_E:
                modificarCodigosPeligrosidad(seleccionado, "E");
                break;
            case R.id.ckb_T:
                modificarCodigosPeligrosidad(seleccionado, "T");
                break;
            case R.id.ckb_I:
                modificarCodigosPeligrosidad(seleccionado, "I");
                break;
            case R.id.ckb_B:
                modificarCodigosPeligrosidad(seleccionado, "B");
                break;
        }
    }

    private void registrarDatosResiduosPeligrosos(Alumno alumno) {

        TextInputEditText txtNombreResiduo = (TextInputEditText) findViewById(R.id.tie_nombre_residuo_p);
        TextInputEditText txtCantidadResiduo = (TextInputEditText) findViewById(R.id.tie_cantidad_residuo_p);
        TextInputEditText txtNoManifiesto = (TextInputEditText) findViewById(R.id.tie_manifiesto);
        TextInputEditText txtFechaResiduoP = (TextInputEditText) findViewById(R.id.tie_fecha_residuos_p);

        Button btnResiduosPeligrosos = (Button) findViewById(R.id.btn_residuos_peligrosos);

        txtFechaResiduoP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDatePicker(txtFechaResiduoP);
            }
        });

        btnResiduosPeligrosos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombreResiduo = txtNombreResiduo.getText().toString().trim();
                float cantidadResiduoKg = Float.parseFloat(txtCantidadResiduo.getText().toString());
                int noManifiesto = Integer.parseInt(txtNoManifiesto.getText().toString());
                String fecha = txtFechaResiduoP.getText().toString();

                if(TextUtils.isEmpty(nombreResiduo)) {
                    Toast.makeText(OpcionesMenuAlumno.this, "Campo vacio.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(cantidadResiduoKg == 0 || Float.toString(cantidadResiduoKg) == "") {
                    Toast.makeText(OpcionesMenuAlumno.this, "Cantidad de residuo no valida.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(codigosPeligrosidad.size() == 0) {
                    Toast.makeText(OpcionesMenuAlumno.this, "Falta seleccionar CPR.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(noManifiesto == 0 || Integer.toString(noManifiesto) == "") {
                    Toast.makeText(OpcionesMenuAlumno.this, "No. De manifiesto no valido.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(fecha)) {
                    Toast.makeText(OpcionesMenuAlumno.this, "Falta seleccionar la fecha.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Alumno alum = new Alumno(alumno.getNombre(), alumno.getApellidos(), alumno.getMatricula());
                ResiduosPeligrosos residuosPeligrosos = new ResiduosPeligrosos(alum, fecha, nombreResiduo,
                                         cantidadResiduoKg, codigosPeligrosidad.toString(), noManifiesto);


                String key = dbRef.child("ResiduosPeligrosos").push().getKey();
                String finalKey = key + "-" + establecerFecha();
                dbRef.child("ResiduosPeligrosos").child(key).removeValue();

                dbRef.child("ResiduosPeligrosos")
                        .child(finalKey)
                        .setValue(residuosPeligrosos)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                Toast.makeText(OpcionesMenuAlumno.this, "Registro guardado.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(OpcionesMenuAlumno.this, e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }

    private void registrarDatosSistemaRiego(Alumno alumno) {

        TextInputEditText txtArea = (TextInputEditText) findViewById(R.id.tie_area_riego);
        TextInputEditText txtTipo = (TextInputEditText) findViewById(R.id.tie_tipo_riego);
        TextInputEditText txtTurno = (TextInputEditText) findViewById(R.id.tie_turno_riego);
        TextInputEditText txtFechaRiego = (TextInputEditText) findViewById(R.id.tie_fecha_riego);
        TextInputEditText txtHoraInicio = (TextInputEditText) findViewById(R.id.tie_hora_riego);
        TextInputEditText txtDuracion = (TextInputEditText) findViewById(R.id.tie_duracion_riego);
        TextInputEditText txtObservaciones = (TextInputEditText) findViewById(R.id.tie_observaciones_riego);

        Button btnSistemaRiego = (Button) findViewById(R.id.btn_sistema_riego);

        Toast.makeText(OpcionesMenuAlumno.this, alumno.getAreaTrabajo() + alumno.getApellidos(), Toast.LENGTH_SHORT).show();


        txtFechaRiego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDatePicker(txtFechaRiego);
            }
        });

        txtHoraInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar time = Calendar.getInstance();
                int hour = time.get(Calendar.HOUR_OF_DAY);
                int minute = time.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(OpcionesMenuAlumno.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int h, int m) {
                                String time = h+":"+m;
                                if(m < 10) {
                                    time = h+":0"+m;
                                }
                                txtHoraInicio.setText(time);
                            }
                        }, hour, minute, DateFormat.is24HourFormat(OpcionesMenuAlumno.this));
                timePickerDialog.show();
            }
        });


    }

    private void EnviarCorreo() {

        TextInputEditText txtDestinatario = (TextInputEditText) findViewById(R.id.tie_correo_destinatario);
        TextInputEditText txtAsunto = (TextInputEditText) findViewById(R.id.tie_asunto_correo);
        TextInputEditText txtMensaje = (TextInputEditText) findViewById(R.id.tie_mensaje_correo);

        Button btnEnviarCorreo = (Button) findViewById(R.id.btn_enviar_correo);

        btnEnviarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String destinatario = txtDestinatario.getText().toString();
                String asunto = txtAsunto.getText().toString();
                String mensaje = txtMensaje.getText().toString();

                Intent correo = new Intent(Intent.ACTION_SEND);

                correo.putExtra(correo.EXTRA_EMAIL, new String[]{destinatario});
                correo.putExtra(correo.EXTRA_SUBJECT, asunto);
                correo.putExtra(correo.EXTRA_TEXT, mensaje);

                correo.setData(Uri.parse("mailto:"));
                correo.setType("text/plain");

                startActivity(Intent.createChooser(correo, "Enviar correo"));
            }
        });
    }

    private void mostrarDatosCuenta(Alumno alumno) {

        TextInputEditText txtNombreUsuario = (TextInputEditText) findViewById(R.id.txt_nombre_inf);
        TextInputEditText txtApellidosUsuario = (TextInputEditText) findViewById(R.id.txt_apellidos_inf);
        TextInputEditText txtArea = (TextInputEditText) findViewById(R.id.txt_area_inf);
        TextInputEditText txtMatricula = (TextInputEditText) findViewById(R.id.txt_matricula_inf);
        TextInputEditText txtCorreo = (TextInputEditText) findViewById(R.id.txt_correo_inf);
        TextInputEditText txtFechaInicio = (TextInputEditText) findViewById(R.id.txt_fecha_inicio_inf);
        TextInputEditText txtFechaFinal = (TextInputEditText) findViewById(R.id.txt_fecha_final_inf);

        Button btnEliminarCuenta = (Button) findViewById(R.id.btn_eliminar_cuenta);

        txtNombreUsuario.setText(alumno.getNombre());
        txtApellidosUsuario.setText(alumno.getApellidos());
        txtArea.setText(alumno.getAreaTrabajo());
        txtMatricula.setText(String.valueOf(alumno.getMatricula()));
        txtCorreo.setText(alumno.getCorreo());
        txtFechaInicio.setText(alumno.getFechaInicio());
        txtFechaFinal.setText(alumno.getFechaFinal());

        btnEliminarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void setDatePicker(TextInputEditText textView){
        final Calendar date = Calendar.getInstance();

        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(OpcionesMenuAlumno.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        int monthFinal = month + 1;
                        String date = day+"/"+(monthFinal)+"/"+year;
                        if(monthFinal < 10) {
                            date = day+"/"+"0"+(monthFinal)+"/"+year;
                        }
                        textView.setText(date);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }
}