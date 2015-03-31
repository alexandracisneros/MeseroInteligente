package com.idealsolution.smartwaiter.model;


public class MesaPisoObject {
    private int id;
    private int nro_piso;
    private int cod_ambiente;
    private String desc_ambiente;
    private int nro_mesa;
    private int nro_asientos;
    private String cod_estado;
    private String desc_estado;
    private int cod_reserva;

    public MesaPisoObject() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNro_piso() {
        return nro_piso;
    }

    public void setNro_piso(int nro_piso) {
        this.nro_piso = nro_piso;
    }

    public int getCod_ambiente() {
        return cod_ambiente;
    }

    public void setCod_ambiente(int cod_ambiente) {
        this.cod_ambiente = cod_ambiente;
    }

    public String getDesc_ambiente() {
        return desc_ambiente;
    }

    public void setDesc_ambiente(String desc_ambiente) {
        this.desc_ambiente = desc_ambiente;
    }

    public int getNro_mesa() {
        return nro_mesa;
    }

    public void setNro_mesa(int nro_mesa) {
        this.nro_mesa = nro_mesa;
    }

    public int getNro_asientos() {
        return nro_asientos;
    }

    public void setNro_asientos(int nro_asientos) {
        this.nro_asientos = nro_asientos;
    }

    public String getCod_estado() {
        return cod_estado;
    }

    public void setCod_estado(String cod_estado) {
        this.cod_estado = cod_estado;
    }

    public String getDesc_estado() {
        return desc_estado;
    }

    public void setDesc_estado(String desc_estado) {
        this.desc_estado = desc_estado;
    }

    public int getCod_reserva() {
        return cod_reserva;
    }

    public void setCod_reserva(int cod_reserva) {
        this.cod_reserva = cod_reserva;
    }
}
