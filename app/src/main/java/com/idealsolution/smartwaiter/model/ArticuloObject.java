package com.idealsolution.smartwaiter.model;

/**
 * Created by Usuario on 01/04/2015.
 */
public class ArticuloObject {
    private int id;
    private String descripcionNorm;
    private String um;
    private String umDescripcion;
    private float precio;
    private String url;

    public ArticuloObject() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcionNorm() {
        return descripcionNorm;
    }

    public void setDescripcionNorm(String descripcionNorm) {
        this.descripcionNorm = descripcionNorm;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public String getUmDescripcion() {
        return umDescripcion;
    }

    public void setUmDescripcion(String umDescripcion) {
        this.umDescripcion = umDescripcion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
