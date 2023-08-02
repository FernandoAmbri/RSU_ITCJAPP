package com.example.rsu_itcjapp.datos;

public class MarcadoresPilas extends Bitacora {

    private int cantidadMarcadores;
    private String departamentoMarcadores;
    private int cantidadPilas;
    private String departamentoPilas;
    private int cantidadToners;
    private String departamentoToners;

    public MarcadoresPilas(Usuario usuario, String fecha, int cantidadMarcadores,
                           String departamentoMarcadores, int cantidadPilas, String departamentoPilas,
                                int toners, String departamentoToners) {
        super(usuario, fecha);
        this.cantidadMarcadores = cantidadMarcadores;
        this.departamentoMarcadores = departamentoMarcadores;
        this.cantidadPilas = cantidadPilas;
        this.departamentoPilas = departamentoPilas;
        this.cantidadToners = toners;
        this.departamentoToners = departamentoToners;
    }

    public int getCantidadMarcadores() {
        return cantidadMarcadores;
    }

    public void setCantidadMarcadores(int cantidadMarcadores) {
        this.cantidadMarcadores = cantidadMarcadores;
    }

    public String getDepartamentoMarcadores() {
        return departamentoMarcadores;
    }

    public void setDepartamentoMarcadores(String departamentoMarcadores) {
        this.departamentoMarcadores = departamentoMarcadores;
    }

    public int getCantidadPilas() {
        return cantidadPilas;
    }

    public void setCantidadPilas(int cantidadPilas) {
        this.cantidadPilas = cantidadPilas;
    }

    public String getDepartamentoPilas() {
        return departamentoPilas;
    }

    public void setDepartamentoPilas(String departamentoPilas) {
        this.departamentoPilas = departamentoPilas;
    }

    public int getCantidadToners() {
        return cantidadToners;
    }

    public void setCantidadToners(int cantidadToners) {
        this.cantidadToners = cantidadToners;
    }

    public String getDepartamentoToners() {
        return departamentoToners;
    }

    public void setDepartamentoToners(String departamentoToners) {
        this.departamentoToners = departamentoToners;
    }
}
