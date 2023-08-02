package com.example.rsu_itcjapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.rsu_itcjapp.datos.Alumno;
import com.example.rsu_itcjapp.datos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

public class RegistroUsuario extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    private Button btnRegistroUsuario;
    private TextInputEditText txtNombre, txtApellidos, txtArea, txtMatricula,
                                    txtCorreo, txtPassword, txtFechaInicio, txtFechaFinal;

    private String usuario = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        usuario = (String) getIntent().getExtras().get(LoginUsuarios.OPCION);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        db.setPersistenceEnabled(true);

        dbRef = db.getReference("DB_RSUITCJ/"+usuario);


        btnRegistroUsuario = (Button) findViewById(R.id.btn_registrar_usuario);
        txtNombre = (TextInputEditText) findViewById(R.id.txt_nombre_alumno_reg);
        txtApellidos = (TextInputEditText) findViewById(R.id.txt_apellidos_usuario);
        txtArea = (TextInputEditText) findViewById(R.id.txt_area_alumno_reg);
        txtMatricula = (TextInputEditText) findViewById(R.id.txt_matricula);
        txtCorreo = (TextInputEditText) findViewById(R.id.txt_correo_usuario);
        txtPassword = (TextInputEditText) findViewById(R.id.txt_password_usuario);
        txtFechaInicio = (TextInputEditText) findViewById(R.id.txt_fecha_inicio_reg);
        txtFechaFinal = (TextInputEditText) findViewById(R.id.txt_fecha_final_reg);


        txtFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDatePicker(txtFechaInicio);
            }
        });

        txtFechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDatePicker(txtFechaFinal);
            }
        });

        btnRegistroUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    registrarUsuario();
                } catch (Exception e) {
                    Toast.makeText(RegistroUsuario.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(e.getMessage());
                }

            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser usuarioActual = mAuth.getCurrentUser();
        if(usuarioActual != null){
            mostrarMenu();
        }
    }

    public void mostrarMenu() {
        Intent menu = new Intent(RegistroUsuario.this, MenuUsuarios.class);
        menu.putExtra(LoginUsuarios.OPCION, usuario);
        startActivity(menu);
    }

    public void registrarUsuario() throws Exception {

        String nombre = txtNombre.getText().toString().trim();
        String apellidos = txtApellidos.getText().toString().trim();
        String area = txtArea.getText().toString().trim();
        String matricula = txtMatricula.getText().toString().trim();
        String correo = txtCorreo.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String fechaInicio = txtFechaInicio.getText().toString();
        String fechaFinal = txtFechaFinal.getText().toString();

        if(nombre.isEmpty() || apellidos.isEmpty() || area.isEmpty() || matricula.isEmpty()
                || correo.isEmpty() || password.isEmpty() || fechaInicio.isEmpty() || fechaFinal.isEmpty()){
            Toast.makeText(RegistroUsuario.this, "Campos vacios", Toast.LENGTH_SHORT).show();
            return;
        }


        mAuth.createUserWithEmailAndPassword(correo, password).
                addOnCompleteListener(RegistroUsuario.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            String userId = task.getResult().getUser().getUid();

                            Alumno alumno = new Alumno(nombre, apellidos, area, Integer.parseInt(matricula) ,
                                    correo, password, fechaInicio, fechaFinal);

                            dbRef.child(userId).setValue(alumno);

                            Toast.makeText(getApplicationContext(), "Cuenta registrada",
                                    Toast.LENGTH_SHORT).show();
                            mostrarMenu();

                        } else {
                            Toast.makeText(RegistroUsuario.this, correo, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Error al registrar la cuenta",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setDatePicker(TextInputEditText textView){
        final Calendar date = Calendar.getInstance();

        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(RegistroUsuario.this,
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