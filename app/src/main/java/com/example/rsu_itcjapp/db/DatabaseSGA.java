package com.example.rsu_itcjapp.db;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.rsu_itcjapp.Constantes;
import com.example.rsu_itcjapp.MenuUsuarios;
import com.example.rsu_itcjapp.datos.Alumno;
import com.example.rsu_itcjapp.datos.Bitacora;
import com.example.rsu_itcjapp.datos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class DatabaseSGA {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private Context context;

    public DatabaseSGA(Context context) {
        this.auth = FirebaseAuth.getInstance();
        this.user = auth.getCurrentUser();
        this.db = FirebaseDatabase.getInstance();
        this.dbRef = db.getReference();
        this.context = context;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseUser getUser(){
        return user;
    }

    public DatabaseReference getDbRef() {
        return dbRef;
    }

    public void obtenerUsuario(String usuario, String datosUsuario) {
        final String userId = auth.getUid();
        if (userId == null) return;

        dbRef.child(Constantes.USUARIOS)
             .child(userId)
             .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot snapshot) {
                        Usuario user = snapshot.getValue(Usuario.class);
                        if (user == null) return;

                        String tipo = user.getTipo();

                        Intent menu = new Intent(context, MenuUsuarios.class);
                        menu.putExtra(usuario, tipo);

                        if (tipo.equals(Constantes.USUARIO_ALUMNO)) {
                            menu.putExtra(datosUsuario, snapshot.getValue(Alumno.class));
                        } else {
                            menu.putExtra(datosUsuario, user);
                        }
                        context.startActivity(menu);
                        Toast.makeText(context, "Bienvenido " + user.getNombre()
                                + " " + user.getApellidoPaterno(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NotNull DatabaseError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void registrarCuenta(Usuario usuario, String userId) {
        dbRef.child(Constantes.USUARIOS)
             .child(userId)
             .setValue(usuario)
             .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NotNull Task<Void> task) {
                    Toast.makeText(context, "Cuenta creada satisfactoriamente.",
                            Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NotNull Exception error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        });
    }

    public void verificarNodo (String path) {
        dbRef.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange (@NotNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    dbRef.child(path).setValue(0);
                }
            }
            @Override
            public void onCancelled (@NotNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registrarDatosBitacora(String path, String nodo, Bitacora datos){

        dbRef.child(path)
             .get()
             .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NotNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        Integer contador = task.getResult().getValue(Integer.class);
                        HashMap <String, Object> updates = new HashMap<>();
                        updates.put(nodo+contador, datos);
                        updates.put(path, contador + 1);

                        dbRef.updateChildren(updates)
                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NotNull Task<Void> task) {
                                    Toast.makeText(context, "Registro guardado correctamente.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                             .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NotNull Exception e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                    }
                }
           }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NotNull Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
           });
    }
}
