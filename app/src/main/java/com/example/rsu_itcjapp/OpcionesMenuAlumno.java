package com.example.rsu_itcjapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.rsu_itcjapp.datos.Alumno;
import com.example.rsu_itcjapp.db.DatabaseSGA;
import com.example.rsu_itcjapp.datos.MarcadoresPilas;
import com.example.rsu_itcjapp.datos.Reciclaje;
import com.example.rsu_itcjapp.datos.ResiduosPeligrosos;
import com.example.rsu_itcjapp.datos.SistemaRiego;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.regex.Pattern;

public class OpcionesMenuAlumno extends AppCompatActivity {

    private Alumno alumno;
    private String idUsuario = "";
    private Integer codigosPeligrosidad = 0;

    private HashMap<String, String> fechaIngreso;
    private HashMap<String, String> fechaSalida;
    private HashMap<String, Boolean> peligrosidad;

    private DatabaseSGA databaseSGA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        int layoutRegistro = (Integer) bundle.get(Constantes.LAYOUT);
        alumno = (Alumno) bundle.getSerializable(Constantes.USUARIO_ALUMNO);

        setContentView(layoutRegistro);

        databaseSGA = new DatabaseSGA(OpcionesMenuAlumno.this);
        idUsuario = databaseSGA.getUser().getUid();

        fechaIngreso = new HashMap<>();
        fechaSalida = new HashMap<>();
        restablecerFechas();

        peligrosidad = new HashMap<>();
        establecerPeligrosidad();

        seleccionarComponentes(layoutRegistro);
    }

    private void seleccionarComponentes(int opcionSeleccionada) {
        switch(opcionSeleccionada) {
            case Constantes.RECICLAJE:
                registrarDatosReciclaje();
                break;
            case Constantes.MARCADORESPILAS:
                registrarDatosMarcadoresPilas();
                break;
            case Constantes.RESIDUOSPELIGROSOS:
                registrarDatosResiduosPeligrosos();
                break;
            case Constantes.SISTEMADERIEGO:
                registrarDatosSistemaRiego();
                break;
            case Constantes.ENVIARCORREO:
                enviarCorreo();
                break;
            case Constantes.VERPERFIL:
                mostrarDatosCuenta();
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
        final int ckbR = R.id.ckb_R;
        final int ckbE = R.id.ckb_E;
        final int ckbT = R.id.ckb_T;
        final int ckbI = R.id.ckb_I;
        final int ckbB = R.id.ckb_B;

        switch(view.getId()){
            case ckbC:
                modificarPeligrosidad(seleccionado, "corrosivo");
                break;
            case ckbR:
                modificarPeligrosidad(seleccionado, "reactivo");
                break;
            case ckbE:
                modificarPeligrosidad(seleccionado, "explosivo");
                break;
            case ckbT:
                modificarPeligrosidad(seleccionado, "toxico");
                break;
            case ckbI:
                modificarPeligrosidad(seleccionado, "inflamable");
                break;
            case ckbB:
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

    private void registrarDatosReciclaje() {
        final String nodo = "reciclajeBitacora/";
        final String path = "contadores/reciclajeBitacoraContador";

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

        databaseSGA.verificarNodo(path);

        btnReciclaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cantidadTapas = txtTapasRec.getText().toString().trim();
                String cantidadBotellas = txtBotellas.getText().toString().trim();
                String botesAluminio = txtBotesAlum.getText().toString().trim();
                String fecha = txtFecha.getText().toString();

                ocultarTeclado(view);

                if (cantidadTapas.isEmpty() || cantidadBotellas.isEmpty() || botesAluminio.isEmpty()) {
                    Toast.makeText(OpcionesMenuAlumno.this,
                            "Campo vacío.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(fecha.isEmpty()) {
                    txtFecha.setError("Campo vacío");
                    return;
                }

                int tapas = Integer.parseInt(cantidadTapas);
                int botellas = Integer.parseInt(cantidadBotellas);
                int botes = Integer.parseInt(botesAluminio);

                Reciclaje reciclaje =
                        new Reciclaje (alumno.getMatricula(), fechaIngreso, tapas, botellas, botes, idUsuario);
                databaseSGA.registrarDatosBitacora(path, nodo, reciclaje);

                txtTapasRec.setText("");
                txtBotellas.setText("");
                txtBotesAlum.setText("");
                txtFecha.setText("");
            }
        });

    }

    private void registrarDatosMarcadoresPilas() {

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

        databaseSGA.verificarNodo(Constantes.PATH_MARCADORES);
        databaseSGA.verificarNodo(Constantes.PATH_PILAS);
        databaseSGA.verificarNodo(Constantes.PATH_TONERS);

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

               String path = "contadores/"+categoriaMpt+"BitacoraContador";
               String nodo = categoriaMpt+"Bitacora/";

               ocultarTeclado(view);

               if (categoriaMpt.isEmpty() || departamento.isEmpty()) {
                   Toast.makeText(OpcionesMenuAlumno.this, "Campo vacío",
                           Toast.LENGTH_SHORT).show();
                   return;
               }

               if (cantidad.isEmpty()) {
                   txtCantidad.setError("Cantidad no válida");
                   return;
               }

               if (fecha.isEmpty()) {
                   txtFecha.setError("Campo vacío.");
                   return;
               }

               int cantidadMpt = Integer.parseInt(cantidad);

               MarcadoresPilas marcadoresPilas =
                       new MarcadoresPilas(alumno.getMatricula(), fechaIngreso, cantidadMpt, departamento, idUsuario);
               databaseSGA.registrarDatosBitacora(path, nodo, marcadoresPilas);

               txtCantidad.setText("");
               actDepartamento.setText("");
               actCategoria.setText("");
               txtFecha.setText("");
           }
        });

    }

    private void registrarDatosResiduosPeligrosos() {
        final String nodo = "rspBitacora/";
        final String path = "rspContador/rspBitacoraContador";

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

        databaseSGA.verificarNodo(path);

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
                if (tilFaseManejo.getVisibility() == View.GONE) {

                    tilFaseManejo.setVisibility(View.VISIBLE);
                    tilPrestadorServicio.setVisibility(View.VISIBLE);
                    tilNumAuto.setVisibility(View.VISIBLE);

                } else if (tilFaseManejo.getVisibility() == View.VISIBLE) {

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

                ocultarTeclado(view);

                int numManifiesto = 0;
                int numAutorizacion = 0;

                if(cantidadResiduoKg.isEmpty() || nombreResiduo.isEmpty()) {
                    Toast.makeText(OpcionesMenuAlumno.this,
                            "Campo vacío", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!TextUtils.isEmpty(noManifiesto)) {
                    numManifiesto = Integer.parseInt(noManifiesto);
                }

                if(!TextUtils.isEmpty(numeroAutorizacion)) {
                    numAutorizacion = Integer.parseInt(numeroAutorizacion);
                }

                if(codigosPeligrosidad < 1) {
                    Toast.makeText(OpcionesMenuAlumno.this,
                            "Falta seleccionar código(s) de peligrosidad.", Toast.LENGTH_SHORT).show();
                    return;
                }

                float residuoKg = Float.parseFloat(cantidadResiduoKg);

                ResiduosPeligrosos residuosPeligrosos =
                        new ResiduosPeligrosos(fechaIngreso, idUsuario, prestadorServicio, nombreResiduo,
                                faseManejo, alumno.getMatricula(), residuoKg, fechaSalida, peligrosidad,
                                numManifiesto, numAutorizacion);

                databaseSGA.registrarDatosBitacora(path, nodo, residuosPeligrosos);

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

    private void registrarDatosSistemaRiego() {
        final String nodo = "sistemaRiegoBitacora/";
        final String path = "contadores/sistemaRiegoBitacoraContador";

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

        databaseSGA.verificarNodo(path);

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
                Interfaz.setTimePicker(txtHoraInicio, OpcionesMenuAlumno.this);
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
                String observaciones = txtObservaciones.getText().toString();

                ocultarTeclado(view);

                if (TextUtils.isEmpty(area) || TextUtils.isEmpty(tipoRiego) || TextUtils.isEmpty(turno)
                        || TextUtils.isEmpty(horaRiego) || TextUtils.isEmpty(duracion)) {
                            Toast.makeText(OpcionesMenuAlumno.this, "Campo vacío.",
                                Toast.LENGTH_SHORT).show();
                    return;
                }

                if (fechaRiego.isEmpty()) {
                    txtFechaRiego.setError("Campo vacío");
                    return;
                }

                int duracionRiego = Integer.parseInt(duracion);

                SistemaRiego sistemaRiego = new SistemaRiego(alumno.getMatricula(), fechaIngreso, area, tipoRiego,
                                            turno, horaRiego, duracionRiego, observaciones, idUsuario);

                databaseSGA.registrarDatosBitacora(path, nodo, sistemaRiego);

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

        txtDestinatario.setText(Constantes.CORREO_DIR);

        btnEnviarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String destinatario = txtDestinatario.getText().toString();
                String asunto = txtAsunto.getText().toString();
                String mensaje = txtMensaje.getText().toString();

                String expresion = "^[\\w+_.-]+@[\\w+.-]+$";
                Pattern pattern = Pattern.compile(expresion, Pattern.CASE_INSENSITIVE);

                if (destinatario.isEmpty() || !pattern.matcher(destinatario).matches()) {
                    txtDestinatario.setError("Campo no válido");
                    return;
                }

                if (asunto.isEmpty()) {
                    txtAsunto.setError("Falta introducir asunto");
                    return;
                }

                if (mensaje.isEmpty()) {
                    txtMensaje.setError("Mensaje vacío");
                    return;
                }

                ocultarTeclado(view);

                try {
                    Intent correo = new Intent(Intent.ACTION_SEND);

                    correo.setData(Uri.parse("mailto:"));
                    correo.setType("text/plain");

                    correo.putExtra(Intent.EXTRA_EMAIL, new String[]{ destinatario });
                    correo.putExtra(Intent.EXTRA_SUBJECT, asunto);
                    correo.putExtra(Intent.EXTRA_TEXT, mensaje);

                    startActivity(Intent.createChooser(correo, "Enviar correo"));

                } catch (ActivityNotFoundException e) {
                    Toast.makeText(OpcionesMenuAlumno.this, e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }

                txtDestinatario.setText("");
                txtAsunto.setText("");
                txtMensaje.setText("");
            }
        });
    }

    private void mostrarDatosCuenta() {

        String fechaInicio = alumno.getResidenciasInicio().get("dia")+"/"
                +alumno.getResidenciasInicio().get("mes")+"/"+alumno.getResidenciasInicio().get("anho");

        String fechaFin = alumno.getResidenciasFin().get("dia")+"/"
                +alumno.getResidenciasFin().get("mes")+"/"+alumno.getResidenciasFin().get("anho");

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

        txtFechaInicio.setText(fechaInicio);
        txtFechaFinal.setText(fechaFin);

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseSGA.getAuth().signOut();

                Toast.makeText(OpcionesMenuAlumno.this,
                            "Sesión finalizada.", Toast.LENGTH_SHORT).show();
                Intent pantallaInicio = new Intent(OpcionesMenuAlumno.this,
                            MainActivity.class);
                startActivity(pantallaInicio);
                finish();
            }
        });

    }
}