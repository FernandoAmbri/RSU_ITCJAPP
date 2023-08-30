package com.example.rsu_itcjapp.datos;

public class Aviso {

    private String correo = "";
    private String info = "";
    private String titulo = "";

    public Aviso() {

    }

    public Aviso(String correo, String info, String titulo) {
        this.correo = correo;
        this.info = info;
        this.titulo = titulo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
