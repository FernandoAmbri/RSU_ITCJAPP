package com.example.rsu_itcjapp.datos;

import java.util.HashMap;

public class MarcadoresPilas extends Bitacora {

    private int cantidad;
    private String departamento;

    public MarcadoresPilas() {

    }

    public MarcadoresPilas (int matriculaUsuario, HashMap<String, String> fechaIngreso, int cantidad,
                           String departamento, String idUsuario) {
        super(idUsuario, fechaIngreso, matriculaUsuario);
        this.cantidad = cantidad;
        this.departamento = departamento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
}
