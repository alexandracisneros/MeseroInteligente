package com.idealsolution.smartwaiter.model;

/**
 * Created by Usuario on 31/03/2015.
 */
public class CategoriaObject {
    private int id;
    private String codigo;
    private String descripcion;
    private String url;

    public CategoriaObject() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
