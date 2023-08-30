package com.example.rsu_itcjapp.datos;

import java.io.Serializable;
import java.util.HashMap;

public class Alumno extends Usuario implements Serializable {

    private String carrera;
    private int matricula;
    private HashMap<String, String> residenciasInicio;
    private HashMap<String, String> residenciasFin;

    public Alumno(){

    }

    public Alumno (String nombre, String apellidoPaterno, String apellidoMaterno, int matricula){
        super(nombre, apellidoPaterno, apellidoMaterno);
        this.matricula = matricula;
    }

    public Alumno(String nombre, String apellidoPaterno, String apellidoMaterno, String area, String tipo,
                  String carrera, int matricula, HashMap<String, String> residenciasInicio,
                  HashMap<String, String> residenciasFin) {
        super(nombre, apellidoPaterno, apellidoMaterno, area, tipo);
        this.carrera = carrera;
        this.matricula = matricula;
        this.residenciasInicio = residenciasInicio;
        this.residenciasFin = residenciasFin;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public HashMap<String, String> getResidenciasInicio() {
        return residenciasInicio;
    }

    public void setResidenciasInicio(HashMap<String, String> residenciasInicio) {
        this.residenciasInicio = residenciasInicio;
    }

    public HashMap<String, String> getResidenciasFin() {
        return residenciasFin;
    }

    public void setResidenciasFin(HashMap<String, String> residenciasFin) {
        this.residenciasFin = residenciasFin;
    }
}
