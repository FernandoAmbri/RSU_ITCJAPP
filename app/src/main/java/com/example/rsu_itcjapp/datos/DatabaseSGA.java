package com.example.rsu_itcjapp.datos;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.rsu_itcjapp.Constantes;
import com.example.rsu_itcjapp.MenuUsuarios;
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
import java.util.HashMap;

public class DatabaseSGA {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    public DatabaseSGA() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseUser getUser(){
        return user;
    }

    public FirebaseDatabase getDb() {
        return db;
    }

    public DatabaseReference getDbRef() {
        return dbRef;
    }

    public void mostrarMenu(Context activityInicio, String usuario, String userData) {

        final String userId = auth.getUid();
        if (userId == null) return;

        dbRef.child(Constantes.USUARIOS)
             .child(userId)
             .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Usuario user = snapshot.getValue(Usuario.class);
                        String tipo = user.getTipo();
                        Intent menu = new Intent(activityInicio, MenuUsuarios.class);
                        menu.putExtra(usuario, tipo);

                        if (tipo.equals(Constantes.USUARIO_ALUMNO)) {
                            menu.putExtra(userData, snapshot.getValue(Alumno.class));
                        } else {
                            menu.putExtra(userData, user);
                        }
                        activityInicio.startActivity(menu);
                        Toast.makeText(activityInicio, "Hola, " + user.getNombre()
                                + " " + user.getApellidoPaterno(), Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(activityInicio, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void registrarCuenta(Usuario usuario, String userId, Context context) {
        dbRef.child(Constantes.USUARIOS)
             .child(userId)
             .setValue(usuario)
             .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    Toast.makeText(context, "Cuenta creada satisfactoriamente.",
                            Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        });
    }

    public void verificarNodo(String nodo, Context context) {

        String path = "contadores"+"/"+nodo+"Contador";

        dbRef.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(!snapshot.exists()) {
                    dbRef.child(path).setValue(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registrarDatosBitacora(String path, String nodo, Bitacora datos, Context context){

        dbRef.child(path)
             .get()
             .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(Task<DataSnapshot> task) {
                    if(task.isSuccessful()) {
                        Integer contador = task.getResult().getValue(Integer.class);
                        HashMap<String, Object> updates = new HashMap<>();
                        updates.put(nodo+"Bitacora"+"/"+contador, datos);
                        updates.put(path, contador + 1);

                        dbRef.updateChildren(updates)
                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    Toast.makeText(context, "Registro guardado correctamente.",
                                            Toast.LENGTH_SHORT).show();
                                }
                        });
                    }
                }
           }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
           });

    }

}
