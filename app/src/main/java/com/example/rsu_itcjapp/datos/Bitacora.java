package com.example.rsu_itcjapp.datos;

import java.util.HashMap;

public class Bitacora {

    private String idUsuario;
    private Usuario usuario;
    private HashMap<String, String> fechaIngreso;


    public Bitacora(){

    }

    public Bitacora(String idUsuario, HashMap<String, String> fechaIngreso) {
        this.idUsuario = idUsuario;
        this.fechaIngreso = fechaIngreso;
    }

    public Bitacora(Usuario usuario, HashMap<String, String> fechaIngreso, String idUsuario){
        this.usuario = usuario;
        this.fechaIngreso = fechaIngreso;
        this.idUsuario = idUsuario;
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

    public String getIdUsuario() {
        return idUsuario;
    }
}
