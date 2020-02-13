package com.example.firebasevocala.model;

public class JugadoresVocalia {

    public String idJV;
    public String idJugador;
    public String idEquipo;
    public String goles;
    public String tamarilla;
    public String troja;
    public String idVocalia;

    public JugadoresVocalia() {
    }

    public String getIdJV() {
        return idJV;
    }

    public void setIdJV(String idJV) {
        this.idJV = idJV;
    }

    public String getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(String idJugador) {
        this.idJugador = idJugador;
    }

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getGoles() {
        return goles;
    }

    public void setGoles(String goles) {
        this.goles = goles;
    }

    public String getTamarilla() {
        return tamarilla;
    }

    public void setTamarilla(String tamarilla) {
        this.tamarilla = tamarilla;
    }

    public String getTroja() {
        return troja;
    }

    public void setTroja(String troja) {
        this.troja = troja;
    }

    public String getIdVocalia() {
        return idVocalia;
    }

    public void setIdVocalia(String idVocalia) {
        this.idVocalia = idVocalia;
    }

    @Override
    public String toString() {
        return idJugador;
    }
}
