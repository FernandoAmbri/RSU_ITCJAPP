package com.example.rsu_itcjapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.rsu_itcjapp.datos.DatabaseSGA;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;

public class LoginUsuarios extends AppCompatActivity {

    private DatabaseSGA databaseSGA;

    private String usuario = "";

    private Button btnEntrar, btnRegistrarAlumno;
    private TextInputEditText txtCorreo, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_usuario);

        // Tipo de usuario
        usuario = (String) getIntent().getExtras().get(Constantes.USUARIO);

        // Instancia de firebase

        databaseSGA = new DatabaseSGA();

        //Componentes de interfaz

        btnRegistrarAlumno = (Button) findViewById(R.id.btn_registrar_cuenta);
        btnEntrar = (Button) findViewById(R.id.btn_login_alumno);
        txtCorreo = (TextInputEditText) findViewById(R.id.txt_correo_login);
        txtPassword = (TextInputEditText) findViewById(R.id.txt_password_login);

        // Ocultar botón de registro para usuario tipo coordinador y trabajador

        if (usuario.equals(Constantes.USUARIO_DOCENTE)
                || usuario.equals(Constantes.USUARIO_TRABAJADOR)) {
            btnRegistrarAlumno.setVisibility(View.INVISIBLE);
        }

        btnRegistrarAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegistro = new Intent(LoginUsuarios.this, RegistroUsuario.class);
                intentRegistro.putExtra(Constantes.USUARIO, usuario);
                startActivity(intentRegistro);
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ocultarTeclado(view);
                iniciarSesion();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        // Si el usuario ya inició sesión, mostrar menú principal
        if(databaseSGA.getUser() != null){
            databaseSGA.mostrarMenu(LoginUsuarios.this,
                    Constantes.USUARIO, Constantes.USER_DATA);
        }
    }

    private void ocultarTeclado(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void iniciarSesion(){

        String correo = txtCorreo.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        if(correo.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginUsuarios.this, "Campos vacios", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseSGA.getAuth().signInWithEmailAndPassword(correo, password)
             .addOnCompleteListener(LoginUsuarios.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            databaseSGA.mostrarMenu(LoginUsuarios.this, Constantes.USUARIO,
                                                        Constantes.USER_DATA);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error al iniciar sesión.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
            })
            .addOnFailureListener(LoginUsuarios.this, new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
        });
    }
}