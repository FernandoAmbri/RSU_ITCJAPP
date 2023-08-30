package com.example.rsu_itcjapp.listView;

public class DatosReporte {

    private String nombreCompleto = "";
    private String matricula = "";
    private String areaTrabajo = "";
    private String reporte = "";

    public DatosReporte(String nombreCompleto, String matricula, String areaTrabajo, String reporte) {
        this.nombreCompleto = nombreCompleto;
        this.matricula = matricula;
        this.areaTrabajo = areaTrabajo;
        this.reporte = reporte;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getAreaTrabajo() {
        return areaTrabajo;
    }

    public void setAreaTrabajo(String areaTrabajo) {
        this.areaTrabajo = areaTrabajo;
    }

    public String getReporte() {
        return reporte;
    }

    public void setReporte(String reporte) {
        this.reporte = reporte;
    }
}
