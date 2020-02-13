package com.example.firebasevocala.model;

public class ValoresVocalia {
    public String idValores;
    public String idVocalia;
    public String idEquipo;
    public String valorArbitraje;
    public String valorAporte;
    public String valorSeguro;
    public String valorTA;
    public String valorTR;
    public String otrosValores;

    public ValoresVocalia() {
    }

    public String getIdValores() {
        return idValores;
    }

    public void setIdValores(String idValores) {
        this.idValores = idValores;
    }

    public String getIdVocalia() {
        return idVocalia;
    }

    public void setIdVocalia(String idVocalia) {
        this.idVocalia = idVocalia;
    }

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getValorArbitraje() {
        return valorArbitraje;
    }

    public void setValorArbitraje(String valorArbitraje) {
        this.valorArbitraje = valorArbitraje;
    }

    public String getValorAporte() {
        return valorAporte;
    }

    public void setValorAporte(String valorAporte) {
        this.valorAporte = valorAporte;
    }

    public String getValorSeguro() {
        return valorSeguro;
    }

    public void setValorSeguro(String valorSeguro) {
        this.valorSeguro = valorSeguro;
    }

    public String getValorTA() {
        return valorTA;
    }

    public void setValorTA(String valorTA) {
        this.valorTA = valorTA;
    }

    public String getValorTR() {
        return valorTR;
    }

    public void setValorTR(String valorTR) {
        this.valorTR = valorTR;
    }

    public String getOtrosValores() {
        return otrosValores;
    }

    public void setOtrosValores(String otrosValores) {
        this.otrosValores = otrosValores;
    }

    @Override
    public String toString() {
        return idEquipo ;
    }

}
