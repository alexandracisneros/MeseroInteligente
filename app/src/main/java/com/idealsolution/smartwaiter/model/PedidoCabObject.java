package com.idealsolution.smartwaiter.model;

import java.util.ArrayList;

/**
 * Created by Usuario on 07/04/2015.
 */
public class PedidoCabObject {
    private int id;
    private String fecha;
    private int nro_mesa;
    private int ambiente;
    private String cod_usuario;
    private int cod_cliente;
    private String tipo_venta;
    private String tipo_pago;
    private String moneda;
    private float monto_total;
    private float monto_recibido;
    private int estado;
    private ArrayList<PedidoDetObject> detalle;

    public PedidoCabObject() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getNro_mesa() {
        return nro_mesa;
    }

    public void setNro_mesa(int nro_mesa) {
        this.nro_mesa = nro_mesa;
    }

    public int getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(int ambiente) {
        this.ambiente = ambiente;
    }

    public String getCod_usuario() {
        return cod_usuario;
    }

    public void setCod_usuario(String cod_usuario) {
        this.cod_usuario = cod_usuario;
    }

    public int getCod_cliente() {
        return cod_cliente;
    }

    public void setCod_cliente(int cod_cliente) {
        this.cod_cliente = cod_cliente;
    }

    public String getTipo_venta() {
        return tipo_venta;
    }

    public void setTipo_venta(String tipo_venta) {
        this.tipo_venta = tipo_venta;
    }

    public String getTipo_pago() {
        return tipo_pago;
    }

    public void setTipo_pago(String tipo_pago) {
        this.tipo_pago = tipo_pago;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public float getMonto_total() {
        return monto_total;
    }

    public void setMonto_total(float monto_total) {
        this.monto_total = monto_total;
    }

    public float getMonto_recibido() {
        return monto_recibido;
    }

    public void setMonto_recibido(float monto_recibido) {
        this.monto_recibido = monto_recibido;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public ArrayList<PedidoDetObject> getDetalle() {
        return detalle;
    }

    public void setDetalle(ArrayList<PedidoDetObject> detalle) {
        this.detalle = detalle;
    }
}
