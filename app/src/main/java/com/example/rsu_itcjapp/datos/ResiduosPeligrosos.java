package com.example.rsu_itcjapp.datos;

import java.util.HashMap;

public class ResiduosPeligrosos extends Bitacora {

    private String idUsuario;
    private String prestadorServicio;
    private String residuo;
    private String faseManejo;
    private int matriculaUsuario;
    private float cantidadGenerada;
    private HashMap<String, String> fechaIngreso;
    private HashMap<String, String> fechaSalida;
    private HashMap<String, Boolean> peligrosidad;
    private int numeroManifiesto;
    private int numeroAutorizacion;

    public ResiduosPeligrosos() {

    }

    public ResiduosPeligrosos(HashMap<String, String> fechaIngreso, String idUsuario,
              String prestadorServicio, String residuo, String faseManejo, int matriculaUsuario,
              float cantidadGenerada, HashMap<String, String> fechaSalida,
              HashMap<String, Boolean> peligrosidad, int numeroManifiesto, int numeroAutorizacion) {
        this.idUsuario = idUsuario;
        this.prestadorServicio = prestadorServicio;
        this.residuo = residuo;
        this.faseManejo = faseManejo;
        this.matriculaUsuario = matriculaUsuario;
        this.cantidadGenerada = cantidadGenerada;
        this.fechaIngreso = fechaIngreso;
        this.fechaSalida = fechaSalida;
        this.peligrosidad = peligrosidad;
        this.numeroManifiesto = numeroManifiesto;
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public String getIdUsuario(){
        return idUsuario;
    }

    public String getPrestadorServicio() {
        return prestadorServicio;
    }

    public void setPrestadorServicio(String prestadorServicio) {
        this.prestadorServicio = prestadorServicio;
    }

    public String getResiduo() {
        return residuo;
    }

    public void setResiduo(String residuo) {
        this.residuo = residuo;
    }

    public String getFaseManejo() {
        return faseManejo;
    }

    public void setFaseManejo(String faseManejo) {
        this.faseManejo = faseManejo;
    }

    public int getMatriculaUsuario() {
        return matriculaUsuario;
    }

    public void setMatriculaUsuario(int matriculaUsuario) {
        this.matriculaUsuario = matriculaUsuario;
    }

    public float getCantidadGenerada() {
        return cantidadGenerada;
    }

    public void setCantidadGenerada(float cantidadGenerada) {
        this.cantidadGenerada = cantidadGenerada;
    }

    public HashMap<String, String> getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(HashMap<String, String> fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public HashMap<String, String> getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(HashMap<String, String> fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public HashMap<String, Boolean> getPeligrosidad() {
        return peligrosidad;
    }

    public void setPeligrosidad(HashMap<String, Boolean> peligrosidad) {
        this.peligrosidad = peligrosidad;
    }

    public int getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(int numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public int getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(int numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }
}
