package com.example.rsu_itcjapp.datos;

import java.util.HashMap;

public class SistemaRiego extends Bitacora {

    private String areaRiego;
    private String tipoRiego;
    private String turno;
    private String hora;
    private int duracionMin;
    private String observaciones;

    public SistemaRiego() {

    }

    public SistemaRiego(int matriculaUsuario, HashMap<String, String> fechaRiego, String area, String tipoRiego,
                String turno, String hora, int duracionMin, String observaciones, String idUsuario) {
        super(idUsuario, fechaRiego, matriculaUsuario);
        this.areaRiego = area;
        this.tipoRiego = tipoRiego;
        this.turno = turno;
        this.hora = hora;
        this.duracionMin = duracionMin;
        this.observaciones = observaciones;
    }

    public String getAreaRiego() {
        return areaRiego;
    }

    public void setAreaRiego(String areaRiego) {
        this.areaRiego = areaRiego;
    }

    public String getTipoRiego() {
        return tipoRiego;
    }

    public void setTipoRiego(String tipoRiego) {
        this.tipoRiego = tipoRiego;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getDuracionMin() {
        return duracionMin;
    }

    public void setDuracionMin(int duracionMin) {
        this.duracionMin = duracionMin;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
