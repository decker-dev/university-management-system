package modelo;

import enums.TipoAula;

public class Aula {
    private String sede;
    private int numero;
    private int piso;
    private TipoAula tipoAula;
    private int capacidad;

    public Aula() {
    }

    public Aula(String sede, int numero, int piso, TipoAula tipoAula, int capacidad) {
        this.sede = sede;
        this.numero = numero;
        this.piso = piso;
        this.tipoAula = tipoAula;
        this.capacidad = capacidad;
    }

    // Getters y Setters
    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }

    public TipoAula getTipoAula() {
        return tipoAula;
    }

    public void setTipoAula(TipoAula tipoAula) {
        this.tipoAula = tipoAula;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    @Override
    public String toString() {
        return "Aula " + numero + " - Piso " + piso + " (" + tipoAula + ")";
    }
}

