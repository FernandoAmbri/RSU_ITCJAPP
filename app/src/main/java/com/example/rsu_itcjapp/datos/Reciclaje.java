package com.example.rsu_itcjapp.datos;

public class Reciclaje extends Bitacora {

    private int tapasRecolectadas;
    private int botellasRecolectadas;
    private int botesAluminio;

    public Reciclaje() {

    }

    public Reciclaje(Usuario usuario, String fecha, int tapasRecolectadas,
                                        int botellasRecolectadas, int botesAluminio) {
        super(usuario, fecha);
        this.tapasRecolectadas = tapasRecolectadas;
        this.botellasRecolectadas = botellasRecolectadas;
        this.botesAluminio = botesAluminio;
    }

    public int getTapasRecolectadas() {
        return tapasRecolectadas;
    }

    public void setTapasRecolectadas(int tapasRecolectadas) {
        this.tapasRecolectadas = tapasRecolectadas;
    }

    public int getBotellasRecolectadas() {
        return botellasRecolectadas;
    }

    public void setBotellasRecolectadas(int botellasRecolectadas) {
        this.botellasRecolectadas = botellasRecolectadas;
    }

    public int getBotesAluminio() {
        return botesAluminio;
    }

    public void setBotesAluminio(int botesAluminio) {
        this.botesAluminio = botesAluminio;
    }
}
