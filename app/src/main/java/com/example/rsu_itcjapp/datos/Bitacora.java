package com.example.rsu_itcjapp.datos;

import java.util.HashMap;

public class Bitacora {

    private Usuario usuario;
    private HashMap<String, String> fechaIngreso;

    public Bitacora(){

    }

    public Bitacora(HashMap<String, String> fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Bitacora(Usuario usuario, HashMap<String, String> fechaIngreso){
        this.usuario = usuario;
        this.fechaIngreso = fechaIngreso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public HashMap<String, String> getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(HashMap<String, String> fecha) {
        this.fechaIngreso = fecha;
    }
}
