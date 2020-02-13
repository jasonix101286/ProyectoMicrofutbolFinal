package com.example.firebasevocala.model;

public class Vocalia {

    public String idVocalia;
    public String campeonato;
    public String nombreVocal;
    public String horario;
    public String nombreArbitro;
    public String ubicación;
    public String foto;
    public String nombresEquipos;

    public Vocalia() {
    }

    public String getNombresEquipos() {
        return nombresEquipos;
    }

    public void setNombresEquipos(String nombresEquipos) {
        this.nombresEquipos = nombresEquipos;
    }

    public String getIdVocalia() {
        return idVocalia;
    }

    public void setIdVocalia(String idVocalia) {
        this.idVocalia = idVocalia;
    }

    public String getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(String campeonato) {
        this.campeonato = campeonato;
    }

    public String getNombreVocal() {
        return nombreVocal;
    }

    public void setNombreVocal(String nombreVocal) {
        this.nombreVocal = nombreVocal;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getNombreArbitro() {
        return nombreArbitro;
    }

    public void setNombreArbitro(String nombreArbitro) {
        this.nombreArbitro = nombreArbitro;
    }

    public String getUbicación() {
        return ubicación;
    }

    public void setUbicación(String ubicación) {
        this.ubicación = ubicación;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return nombresEquipos;
    }
}
