package com.example.rsu_itcjapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.rsu_itcjapp.datos.Alumno;
import com.example.rsu_itcjapp.datos.DatabaseSGA;
import com.example.rsu_itcjapp.datos.MarcadoresPilas;
import com.example.rsu_itcjapp.datos.Reciclaje;
import com.example.rsu_itcjapp.datos.ResiduosPeligrosos;
import com.example.rsu_itcjapp.datos.SistemaRiego;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class OpcionesMenuAlumno extends AppCompatActivity {

    private Alumno alumno;
    private String idUsuario = "";
    private Integer codigosPeligrosidad = 0;

    private final ArrayList<Integer> opcionesLayout = new ArrayList<>();

    private HashMap<String, String> fechaIngreso;
    private HashMap<String, String> fechaSalida;
    private HashMap<String, Boolean> peligrosidad;

    private DatabaseSGA databaseSGA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();

        int opcionSeleccionada = (Integer) bundle.get(Constantes.OPCION_MENU_ID);
        int primerLayout = (Integer) bundle.get(Constantes.LAYOUT);
        alumno = (Alumno) bundle.getSerializable(Constantes.USUARIO_ALUMNO);

        opcionesLayout.add(primerLayout);
        opcionesLayout.add(R.layout.layout_enviar_correo);
        opcionesLayout.add(R.layout.layout_informacion_usuario);

        setContentView(opcionesLayout.get(opcionSeleccionada));

        databaseSGA = new DatabaseSGA();
        idUsuario = databaseSGA.getUser().getUid();

        fechaIngreso = new HashMap<>();
        fechaSalida = new HashMap<>();

        restablecerFechas();

        peligrosidad = new HashMap<>();

        establecerPeligrosidad();

        seleccionarLayout(opcionSeleccionada, alumno);
    }

    @Override
    public void onStart(){
        super.onStart();
        if(databaseSGA.getUser() == null){
            Intent pantallaInicio = new Intent(OpcionesMenuAlumno.this, MainActivity.class);
            startActivity(pantallaInicio);
        }
    }

    private void seleccionarLayoutRegistro(Alumno alumno) {
        switch(alumno.getArea()) {
            case Constantes.REC:
                registrarDatosReciclaje(alumno);
                break;
            case Constantes.MPT:
                registrarDatosMarcadoresPilas(alumno);
                break;
            case Constantes.STAR:
                registrarDatosSistemaRiego(alumno);
                break;
            default:
                registrarDatosResiduosPeligrosos(alumno);
                break;
        }
    }

    private void seleccionarLayout(int opcionSeleccionada, Alumno alumno) {
        switch(opcionSeleccionada) {
            case 0:
                seleccionarLayoutRegistro(alumno);
                break;
            case 1:
                enviarCorreo();
                break;
            case 2:
                mostrarDatosCuenta(alumno);
                break;
        }
    }

    private void ocultarTeclado(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void establecerPeligrosidad() {
        peligrosidad.put("biologicoInfeccioso", false);
        peligrosidad.put("corrosivo", false);
        peligrosidad.put("explosivo", false);
        peligrosidad.put("inflamable", false);
        peligrosidad.put("reactivo", false);
        peligrosidad.put("toxico", false);
    }

    public void restablecerFechas() {
        fechaIngreso.put("anho", "0000");
        fechaIngreso.put("mes", "00");
        fechaIngreso.put("dia", "00");

        fechaSalida.put("anho", "0000");
        fechaSalida.put("mes", "00");
        fechaSalida.put("dia", "00");
    }

    private void registrarDatosReciclaje(Alumno alumno) {
        String nodo = "reciclaje";
        String path = "contadores/reciclajeContador";

        TextInputEditText txtTapasRec = (TextInputEditText) findViewById(R.id.tie_tapas_rec);
        TextInputEditText txtBotellas = (TextInputEditText) findViewById(R.id.tie_botellas_rec);
        TextInputEditText txtBotesAlum = (TextInputEditText) findViewById(R.id.tie_botes_alum);
        TextInputEditText txtFecha = (TextInputEditText) findViewById(R.id.tie_fecha_rec);

        Button btnReciclaje = (Button) findViewById(R.id.btn_reciclaje);

        txtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fechaIngreso = Interfaz.setDatePicker(txtFecha, OpcionesMenuAlumno.this);
            }
        });

        databaseSGA.verificarNodo(nodo, getApplicationContext());

        btnReciclaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cantidadTapas = txtTapasRec.getText().toString().trim();
                String cantidadBotellas = txtBotellas.getText().toString().trim();
                String botesAluminio = txtBotesAlum.getText().toString().trim();
                String fecha = txtFecha.getText().toString();

                int tapas = 0, botellas = 0, botes = 0;

                if(!TextUtils.isEmpty(cantidadTapas)) {
                    tapas = Integer.parseInt(cantidadTapas);
                }

                if(!TextUtils.isEmpty(cantidadBotellas)) {
                    botellas = Integer.parseInt(cantidadBotellas);
                }

                if(!TextUtils.isEmpty(botesAluminio)) {
                    botes = Integer.parseInt(botesAluminio);
                }

                if(tapas < 1 && botellas < 1 && botes < 1) {
                    Toast.makeText(OpcionesMenuAlumno.this,
                            "Es necesario introducir una cantidad.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(fecha.isEmpty()) {
                    Toast.makeText(OpcionesMenuAlumno.this,
                            "Falta introducir la fecha.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ocultarTeclado(view);

                Alumno alum = new Alumno(alumno.getNombre(), alumno.getApellidoPaterno(),
                                                alumno.getApellidoMaterno(), alumno.getMatricula());
                Reciclaje reciclaje = new Reciclaje(alum, fechaIngreso, tapas, botellas, botes);
                databaseSGA.registrarDatosBitacora(path, nodo, reciclaje, getApplicationContext());

                txtTapasRec.setText("");
                txtBotellas.setText("");
                txtBotesAlum.setText("");
                txtFecha.setText("");
            }
        });

    }

    private void registrarDatosMarcadoresPilas(Alumno alumno) {

        AutoCompleteTextView actCategoria = (AutoCompleteTextView) findViewById(R.id.act_categoria_mpt);
        TextInputEditText txtCantidad = (TextInputEditText) findViewById(R.id.tie_cantidad_mpt);
        AutoCompleteTextView actDepartamento = (AutoCompleteTextView) findViewById(R.id.act_departamento_mpt);
        TextInputEditText txtFecha = (TextInputEditText) findViewById(R.id.tie_fecha_mpt);

        Button btnMarcadores = (Button) findViewById(R.id.btn_marcadores_pilas);

        String [] departamentosInstituto = getResources().getStringArray(R.array.departamentos);
        String [] categorias = getResources().getStringArray(R.array.opcionesMarcadores);

        ArrayAdapter<String> adapterDepartamento =
                new ArrayAdapter<>(this, R.layout.dropdown_menu, departamentosInstituto);

        ArrayAdapter<String> adapterCategorias =
                new ArrayAdapter<>(this, R.layout.dropdown_menu, categorias);

        actDepartamento.setAdapter(adapterDepartamento);
        actCategoria.setAdapter(adapterCategorias);

        databaseSGA.verificarNodo("marcadores", getApplicationContext());
        databaseSGA.verificarNodo("pilas", getApplicationContext());
        databaseSGA.verificarNodo("toners", getApplicationContext());

        txtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fechaIngreso =
                        Interfaz.setDatePicker(txtFecha, OpcionesMenuAlumno.this);
            }
        });

        btnMarcadores.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               String categoriaMpt = actCategoria.getText().toString().toLowerCase();
               String cantidad = txtCantidad.getText().toString().trim();
               String departamento = actDepartamento.getText().toString();
               String fecha = txtFecha.getText().toString();
               String path = "contadores"+"/"+categoriaMpt+"Contador";

               if(categoriaMpt.isEmpty() || cantidad.isEmpty() || departamento.isEmpty()) {
                   Toast.makeText(OpcionesMenuAlumno.this, "Campo vacío.",
                           Toast.LENGTH_SHORT).show();
                   return;
               }

               int cantidadMpt = Integer.parseInt(cantidad);

               if(cantidadMpt < 1) {
                   Toast.makeText(OpcionesMenuAlumno.this, "Cantidad no valida.",
                           Toast.LENGTH_SHORT).show();
                   return;
               }

               if(fecha.isEmpty()) {
                   Toast.makeText(OpcionesMenuAlumno.this, "Falta introducir la fecha.",
                           Toast.LENGTH_SHORT).show();
                   return;
               }

               ocultarTeclado(view);

               Alumno alum = new Alumno(alumno.getNombre(), alumno.getApellidoPaterno(),
                                                alumno.getApellidoMaterno(), alumno.getMatricula());
               MarcadoresPilas marcadoresPilas = new MarcadoresPilas(alum, fechaIngreso, cantidadMpt, departamento);
               databaseSGA.registrarDatosBitacora(path, categoriaMpt, marcadoresPilas, getApplicationContext());

               txtCantidad.setText("");
               actDepartamento.setText("");
               actCategoria.setText("");
               txtFecha.setText("");
           }
        });

    }

    public void modificarPeligrosidad(boolean seleccionado, String codigo) {
        if(seleccionado) {
            peligrosidad.replace(codigo, true);
            codigosPeligrosidad += 1;
            return;
        }
        peligrosidad.replace(codigo, false);
        codigosPeligrosidad -= 1;
    }

    public void seleccionarCodigoPeligrosidad(View view) {
        boolean seleccionado = ((CheckBox) view).isChecked();
        final int ckbC = R.id.ckb_C;

        switch(view.getId()){
            case ckbC:
                modificarPeligrosidad(seleccionado, "corrosivo");
                break;
            case R.id.ckb_R:
                modificarPeligrosidad(seleccionado, "reactivo");
                break;
            case R.id.ckb_E:
                modificarPeligrosidad(seleccionado, "explosivo");
                break;
            case R.id.ckb_T:
                modificarPeligrosidad(seleccionado, "toxico");
                break;
            case R.id.ckb_I:
                modificarPeligrosidad(seleccionado, "inflamable");
                break;
            case R.id.ckb_B:
                modificarPeligrosidad(seleccionado, "biologicoInfeccioso");
                break;
        }
    }

    private void limpiarcheckBoxes() {
        ((CheckBox) findViewById(R.id.ckb_C)).setChecked(false);
        ((CheckBox) findViewById(R.id.ckb_R)).setChecked(false);
        ((CheckBox) findViewById(R.id.ckb_E)).setChecked(false);
        ((CheckBox) findViewById(R.id.ckb_T)).setChecked(false);
        ((CheckBox) findViewById(R.id.ckb_I)).setChecked(false);
        ((CheckBox) findViewById(R.id.ckb_B)).setChecked(false);
    }

    private void registrarDatosResiduosPeligrosos(Alumno alumno) {
        String nodo = "rsp";
        String path = "rspContador/rspBitacoraContador";

        TextInputLayout tilFaseManejo = (TextInputLayout) findViewById(R.id.til_fase_manejo);
        TextInputLayout tilPrestadorServicio = (TextInputLayout) findViewById(R.id.til_prestador_servicio);
        TextInputLayout tilNumAuto = (TextInputLayout) findViewById(R.id.til_num_autorizacion);

        AutoCompleteTextView actResiduo = (AutoCompleteTextView) findViewById(R.id.act_categoria_rsp);
        TextInputEditText txtCantidadResiduo = (TextInputEditText) findViewById(R.id.tie_cantidad_residuo_p);
        TextInputEditText txtFechaIngreso = (TextInputEditText) findViewById(R.id.tie_fecha_ingreso_rsp);
        TextInputEditText txtFechaSalida = (TextInputEditText) findViewById(R.id.tie_fecha_salida_rsp);
        TextInputEditText txtNoManifiesto = (TextInputEditText) findViewById(R.id.tie_manifiesto);
        TextInputEditText txtFaseManejo = (TextInputEditText) findViewById(R.id.tie_fase_manejo);
        TextInputEditText txtPrestadorServicio = (TextInputEditText) findViewById(R.id.tie_prestador_servicio);
        TextInputEditText txtNumeroAut = (TextInputEditText) findViewById(R.id.tie_numero_autorizacion);

        Button btnResiduosPeligrosos = (Button) findViewById(R.id.btn_residuos_peligrosos);
        Button btnSeccionManejo = (Button) findViewById(R.id.btn_seccion_manejo);

        String [] categorias = getResources().getStringArray(R.array.rspCategorias);
        ArrayAdapter<String> categoriasAdapter =
                new ArrayAdapter<>(this, R.layout.dropdown_menu, categorias);

        actResiduo.setAdapter(categoriasAdapter);

        tilFaseManejo.setVisibility(View.GONE);
        tilPrestadorServicio.setVisibility(View.GONE);
        tilNumAuto.setVisibility(View.GONE);

        actResiduo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restablecerFechas();
            }
        });

        txtFechaIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fechaIngreso =
                        Interfaz.setDatePicker(txtFechaIngreso, OpcionesMenuAlumno.this);
            }
        });

        txtFechaSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fechaSalida =
                        Interfaz.setDatePicker(txtFechaSalida, OpcionesMenuAlumno.this);
            }
        });

        btnSeccionManejo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tilFaseManejo.getVisibility() == View.GONE) {

                    tilFaseManejo.setVisibility(View.VISIBLE);
                    tilPrestadorServicio.setVisibility(View.VISIBLE);
                    tilNumAuto.setVisibility(View.VISIBLE);

                } else if(tilFaseManejo.getVisibility() == View.VISIBLE) {

                    tilFaseManejo.setVisibility(View.GONE);
                    tilPrestadorServicio.setVisibility(View.GONE);
                    tilNumAuto.setVisibility(View.GONE);
                }
            }
        });

        btnResiduosPeligrosos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreResiduo = actResiduo.getText().toString();
                String cantidadResiduoKg = txtCantidadResiduo.getText().toString().trim();
                String noManifiesto = txtNoManifiesto.getText().toString().trim();
                String faseManejo = txtFaseManejo.getText().toString().trim();
                String prestadorServicio = txtPrestadorServicio.getText().toString().trim();
                String numeroAutorizacion = txtNumeroAut.getText().toString().trim();

                float residuoKg = 0;
                int numManifiesto = 0;
                int numAutorizacion = 0;

                if(!TextUtils.isEmpty(cantidadResiduoKg)) {
                    residuoKg = Float.parseFloat(cantidadResiduoKg);
                }

                if(!TextUtils.isEmpty(noManifiesto)) {
                    numManifiesto = Integer.parseInt(noManifiesto);
                }

                if(!TextUtils.isEmpty(numeroAutorizacion)) {
                    numAutorizacion = Integer.parseInt(numeroAutorizacion);
                }

                if(TextUtils.isEmpty(nombreResiduo) ) {
                    Toast.makeText(OpcionesMenuAlumno.this, "Falta seleccionar residuo.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(residuoKg < 1) {
                    Toast.makeText(OpcionesMenuAlumno.this, "Falta cantidad residuo.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(codigosPeligrosidad <= 0) {
                    Toast.makeText(OpcionesMenuAlumno.this,
                            "Falta seleccionar código(s) de peligrosidad.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ocultarTeclado(view);

                ResiduosPeligrosos residuosPeligrosos =
                        new ResiduosPeligrosos(fechaIngreso, idUsuario, prestadorServicio, nombreResiduo,
                                faseManejo, alumno.getMatricula(), residuoKg, fechaSalida, peligrosidad,
                                numManifiesto, numAutorizacion);

                databaseSGA.registrarDatosBitacora(path, nodo, residuosPeligrosos, getApplicationContext());

                actResiduo.setText("");
                txtCantidadResiduo.setText("");
                txtNoManifiesto.setText("");
                txtFechaIngreso.setText("");
                txtFechaSalida.setText("");
                txtPrestadorServicio.setText("");
                txtNumeroAut.setText("");
                codigosPeligrosidad = 0;
                limpiarcheckBoxes();
            }
        });
    }

    private void registrarDatosSistemaRiego(Alumno alumno) {
        String nodo = "sistemaRiego";
        String path = "contadores/sistemaRiegoContador";

        TextInputEditText txtArea = (TextInputEditText) findViewById(R.id.tie_area_riego);
        AutoCompleteTextView actTipo = (AutoCompleteTextView) findViewById(R.id.act_tipo_riego);
        AutoCompleteTextView actTurno = (AutoCompleteTextView) findViewById(R.id.act_turno_riego);
        TextInputEditText txtFechaRiego = (TextInputEditText) findViewById(R.id.tie_fecha_riego);
        TextInputEditText txtHoraInicio = (TextInputEditText) findViewById(R.id.tie_hora_riego);
        TextInputEditText txtDuracion = (TextInputEditText) findViewById(R.id.tie_duracion_riego);
        TextInputEditText txtObservaciones = (TextInputEditText) findViewById(R.id.tie_observaciones_riego);

        Button btnSistemaRiego = (Button) findViewById(R.id.btn_sistema_riego);

        String [] tiposRiego = getResources().getStringArray(R.array.tipoRiego);
        ArrayAdapter<String> tiposRiegoAdapter =
                new ArrayAdapter<>(this, R.layout.dropdown_menu, tiposRiego);
        actTipo.setAdapter(tiposRiegoAdapter);

        String [] turnos = getResources().getStringArray(R.array.turnosRiego);
        ArrayAdapter<String> turnosRiegoAdapter =
                new ArrayAdapter<>(this, R.layout.dropdown_menu, turnos);
        actTurno.setAdapter(turnosRiegoAdapter);

        databaseSGA.verificarNodo(nodo, getApplicationContext());

        txtFechaRiego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fechaIngreso =
                        Interfaz.setDatePicker(txtFechaRiego, OpcionesMenuAlumno.this);
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

                                if(m < 10) time = h+":0"+m;

                                txtHoraInicio.setText(time);
                            }
                        }, hour, minute, DateFormat.is24HourFormat(OpcionesMenuAlumno.this));
                timePickerDialog.show();
            }
        });

        btnSistemaRiego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String area = txtArea.getText().toString().trim();
                String tipoRiego = actTipo.getText().toString();
                String turno = actTurno.getText().toString();
                String fechaRiego = txtFechaRiego.getText().toString();
                String horaRiego = txtHoraInicio.getText().toString();
                String duracion = txtDuracion.getText().toString().trim();
                String observaciones = txtObservaciones.getText().toString().trim();


                if(TextUtils.isEmpty(area) || TextUtils.isEmpty(tipoRiego) || TextUtils.isEmpty(turno)
                        || TextUtils.isEmpty(horaRiego) || TextUtils.isEmpty(duracion)) {
                            Toast.makeText(OpcionesMenuAlumno.this, "Campo vacio.",
                                Toast.LENGTH_SHORT).show();
                    return;
                }

                if(fechaRiego.isEmpty()) {
                    Toast.makeText(OpcionesMenuAlumno.this,
                            "Falta introducir la fecha.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int duracionRiego = Integer.parseInt(duracion);

                if(duracionRiego < 1) {
                    Toast.makeText(OpcionesMenuAlumno.this,
                            "El valor de duración no es correcto.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ocultarTeclado(view);
                Alumno alum = new Alumno(alumno.getNombre(), alumno.getApellidoPaterno(),
                                                alumno.getApellidoMaterno(), alumno.getMatricula());
                SistemaRiego sistemaRiego = new SistemaRiego(alum, fechaIngreso, area, tipoRiego, turno,
                                                        horaRiego, duracionRiego, observaciones);
                databaseSGA.registrarDatosBitacora(path, nodo, sistemaRiego, getApplicationContext());

                txtArea.setText("");
                actTipo.setText("");
                actTurno.setText("");
                txtFechaRiego.setText("");
                txtHoraInicio.setText("");
                txtDuracion.setText("");
                txtObservaciones.setText("");
            }
        });

    }

    private void enviarCorreo() {

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

                if(destinatario.isEmpty() || asunto.isEmpty() || mensaje.isEmpty()) {
                    Toast.makeText(OpcionesMenuAlumno.this,
                            "Campos vacíos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ocultarTeclado(view);

                Intent correo = new Intent(Intent.ACTION_SEND);

                correo.putExtra(Intent.EXTRA_EMAIL, new String[]{destinatario});
                correo.putExtra(Intent.EXTRA_SUBJECT, asunto);
                correo.putExtra(Intent.EXTRA_TEXT, mensaje);

                correo.setData(Uri.parse("mailto:"));
                correo.setType("text/plain");

                startActivity(Intent.createChooser(correo, "Enviar correo"));

                txtDestinatario.setText("");
                txtAsunto.setText("");
                txtMensaje.setText("");
            }
        });
    }

    private void mostrarDatosCuenta(Alumno alumno) {

        TextInputEditText txtNombreUsuario = (TextInputEditText) findViewById(R.id.txt_nombre_inf);
        TextInputEditText txtApellidoPaterno = (TextInputEditText) findViewById(R.id.txt_apellidoPaterno_inf);
        TextInputEditText txtApellidoMaterno = (TextInputEditText) findViewById(R.id.txt_apellidoMaterno_inf);
        TextInputEditText txtArea = (TextInputEditText) findViewById(R.id.txt_area_inf);
        TextInputEditText txtCarrera = (TextInputEditText) findViewById(R.id.txt_carrera_inf);
        TextInputEditText txtMatricula = (TextInputEditText) findViewById(R.id.txt_matricula_inf);
        TextInputEditText txtCorreo = (TextInputEditText) findViewById(R.id.txt_correo_inf);
        TextInputEditText txtFechaInicio = (TextInputEditText) findViewById(R.id.txt_fecha_inicio_inf);
        TextInputEditText txtFechaFinal = (TextInputEditText) findViewById(R.id.txt_fecha_final_inf);

        Button btnCerrarSesion = (Button) findViewById(R.id.btn_cerrar_sesion);

        txtNombreUsuario.setText(alumno.getNombre());
        txtApellidoPaterno.setText(alumno.getApellidoPaterno());
        txtApellidoMaterno.setText(alumno.getApellidoMaterno());
        txtArea.setText(alumno.getArea());
        txtCarrera.setText(alumno.getCarrera());
        txtMatricula.setText(String.valueOf(alumno.getMatricula()));
        txtCorreo.setText(databaseSGA.getUser().getEmail());

        String fechaInicio = alumno.getResidenciasInicio().get("dia")+"/"
                +alumno.getResidenciasInicio().get("mes")+"/"+alumno.getResidenciasInicio().get("anho");

        String fechaFin = alumno.getResidenciasFin().get("dia")+"/"
                +alumno.getResidenciasFin().get("mes")+"/"+alumno.getResidenciasFin().get("anho");

        txtFechaInicio.setText(fechaInicio);
        txtFechaFinal.setText(fechaFin);

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseSGA.getAuth().signOut();
                if(databaseSGA.getAuth() == null) {
                    Toast.makeText(OpcionesMenuAlumno.this,
                            "Sesión finalizada.", Toast.LENGTH_SHORT).show();
                    Intent pantallaInicio = new Intent(OpcionesMenuAlumno.this,
                            MainActivity.class);
                    startActivity(pantallaInicio);
                }
            }
        });

    }
}