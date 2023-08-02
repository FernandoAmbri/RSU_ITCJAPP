package com.example.rsu_itcjapp.datos;

public class ResiduosPeligrosos extends Bitacora {

    private String nombreResiduo;
    private float cantidadResiduoKg;
    private String codigoPeligrosidad;
    private int noManifiesto;

    public ResiduosPeligrosos() {

    }

    public ResiduosPeligrosos(Usuario usuario, String fecha, String nombreResiduo, float cantidadResiduoKg,
                                String codigoPeligrosidad, int noManifiesto) {
        super(usuario, fecha);
        this.nombreResiduo = nombreResiduo;
        this.cantidadResiduoKg = cantidadResiduoKg;
        this.codigoPeligrosidad = codigoPeligrosidad;
        this.noManifiesto = noManifiesto;
    }

    public String getNombreResiduo() {
        return nombreResiduo;
    }

    public void setNombreResiduo(String nombreResiduo) {
        this.nombreResiduo = nombreResiduo;
    }

    public float getCantidadResiduoKg() {
        return cantidadResiduoKg;
    }

    public void setCantidadResiduoKg(float cantidadResiduoKg) {
        this.cantidadResiduoKg = cantidadResiduoKg;
    }

    public String getCodigoPeligrosidad() {
        return codigoPeligrosidad;
    }

    public void setCodigoPeligrosidad(String codigoPeligrosidad) {
        this.codigoPeligrosidad = codigoPeligrosidad;
    }

    public int getNoManifiesto() {
        return noManifiesto;
    }

    public void setNoManifiesto(int noManifiesto) {
        this.noManifiesto = noManifiesto;
    }
}
