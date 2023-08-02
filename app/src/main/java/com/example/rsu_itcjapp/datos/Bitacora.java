package com.example.rsu_itcjapp.datos;

public class Bitacora {

    Usuario usuario;
    String fecha;

    public Bitacora(){

    }

    public Bitacora(Usuario usuario, String fecha){
        this.usuario = usuario;
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
