package com.example.rsu_itcjapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class LoginUsuarios extends AppCompatActivity {

    public static final String OPCION = "USUARIO";
    public static final String USER_DATA = "USER_DATA";

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    public static String usuario = "";

    private Button btnEntrar, btnRegistrarAlumno;
    private TextInputEditText txtCorreo, txtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_usuario);

        // Tipo de usuario
        usuario = (String) getIntent().getExtras().get(OPCION);

        // Instancia de autenticación de firebase
        mAuth = FirebaseAuth.getInstance();

        db = FirebaseDatabase.getInstance();

        //Componentes de interfaz

        btnRegistrarAlumno = (Button) findViewById(R.id.btn_registrar_cuenta);
        btnEntrar = (Button) findViewById(R.id.btn_login_alumno);
        txtCorreo = (TextInputEditText) findViewById(R.id.txt_correo_login);
        txtPassword = (TextInputEditText) findViewById(R.id.txt_password_login);

        // Ocultar botón de registro para usuario tipo coordinador
        if (usuario.equals(MainActivity.USUARIOS[0])) {
            btnRegistrarAlumno.setVisibility(View.INVISIBLE);
        }

        btnRegistrarAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegistro = new Intent(LoginUsuarios.this, RegistroUsuario.class);
                intentRegistro.putExtra(OPCION, usuario);
                startActivity(intentRegistro);
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesion();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();

        // Si el usuario ya inició sesión, mostrar menú principal

        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            mostrarMenu(user.getUid());
        }
    }

    public void mostrarMenu(String userId) {
        Intent menu = new Intent(LoginUsuarios.this, MenuUsuarios.class);
        menu.putExtra(OPCION, usuario);
        menu.putExtra(USER_DATA, userId);
        startActivity(menu);
    }

    public void iniciarSesion(){

        String correo = txtCorreo.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        if(correo.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginUsuarios.this, "Campos vacios", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(correo, password).addOnCompleteListener(LoginUsuarios.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            String userId = task.getResult().getUser().getUid();
                            Toast.makeText(getApplicationContext(), "Bienvenido",
                                    Toast.LENGTH_SHORT).show();
                            mostrarMenu(userId);

                        } else {
                            Toast.makeText(getApplicationContext(), "Error al iniciar sesión",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }
}