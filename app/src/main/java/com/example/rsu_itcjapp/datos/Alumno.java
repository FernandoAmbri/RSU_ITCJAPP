package com.example.rsu_itcjapp.datos;

import java.util.Date;

public class Alumno extends Usuario {

    private String areaTrabajo;
    private int matricula;
    private String fechaInicio;
    private String fechaFinal;

    public Alumno(){

    }

    public Alumno (String nombre, String apellidos, int matricula){
        super(nombre, apellidos);
        this.matricula = matricula;
    }

    public Alumno (String nombre, String apellidos, String areaTrabajo, int matricula, String correo,
                        String password, String fechaInicio, String fechaFinal) {
        super(nombre, apellidos, correo, password);
        this.areaTrabajo = areaTrabajo;
        this.matricula = matricula;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
    }

    public String getAreaTrabajo() {
        return areaTrabajo;
    }

    public void setAreaTrabajo(String areaTrabajo) {
        this.areaTrabajo = areaTrabajo;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
}
