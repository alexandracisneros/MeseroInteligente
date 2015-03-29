package com.idealsolution.smartwaiter.model;

/**
 * Created by Usuario on 28/03/2015.
 */
public class SpinnerObject {
    private int codigo;
    private String descripcion;

    public SpinnerObject() {
        super();
    }

    public SpinnerObject(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    @Override
    public String toString() {
        return getDescripcion();
    }
}
