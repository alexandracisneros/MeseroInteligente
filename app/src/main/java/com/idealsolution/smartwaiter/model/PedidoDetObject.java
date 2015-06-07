package com.idealsolution.smartwaiter.model;

/**
 * Created by Usuario on 07/04/2015.
 */
public class PedidoDetObject {
    private int id;
    private int pedido_id;
    private int cod_articulo;
    private float cantidad;
    private float precio;
    private int tipo_articulo;
    private int cod_art_principal;
    private String comentario;
    private int estado_articulo;
    private String desc_articulo; //FALTA AGREGARLO A LA BD PORQUE SINO AL MOSTRAR HABRIA QUE VOLVER A CONSULTAR LA BD SOLO X LOS NOMBRES

    public PedidoDetObject() {
    }

    public PedidoDetObject(ArticuloObject articulo) {
        this.setCod_articulo(articulo.getId());
        this.setDescripcionArticulo(articulo.getDescripcionNorm());
        this.setCantidad(1); //1 item por defecto.
        this.setPrecio(articulo.getPrecio());
        this.setEstado_articulo(0); //No enviado a cocina
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPedido_id() {
        return pedido_id;
    }

    public void setPedido_id(int pedido_id) {
        this.pedido_id = pedido_id;
    }

    public int getCod_articulo() {
        return cod_articulo;
    }

    public void setCod_articulo(int cod_articulo) {
        this.cod_articulo = cod_articulo;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getTipo_articulo() {
        return tipo_articulo;
    }

    public void setTipo_articulo(int tipo_articulo) {
        this.tipo_articulo = tipo_articulo;
    }

    public int getCod_art_principal() {
        return cod_art_principal;
    }

    public void setCod_art_principal(int cod_art_principal) {
        this.cod_art_principal = cod_art_principal;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getEstado_articulo() {
        return estado_articulo;
    }

    public void setEstado_articulo(int estado_articulo) {
        this.estado_articulo = estado_articulo;
    }

    public String getDescripcionArticulo() {
        return desc_articulo;
    }

    public void setDescripcionArticulo(String desc_articulo) {
        this.desc_articulo = desc_articulo;
    }

//    public PedidoDetObject PedidoDetObject(ArticuloObject articulo){
//        PedidoDetObject detalleItem=new PedidoDetObject();
//        detalleItem.setCod_articulo(articulo.getId());
//        detalleItem.setDescripcionArticulo(articulo.getDescripcionNorm());
//        detalleItem.setCantidad(1); //1 item por defecto.
//        detalleItem.setPrecio(articulo.getPrecio());
//        detalleItem.setEstado_articulo(0); //No enviado a cocina
//        return detalleItem;
//    }

    //This functions has been overriden so that I can use CONTAINS in the ArrayList of Details. See PedidoSharedPreference.addItem
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof PedidoDetObject) ) {
            return false;
        }
        PedidoDetObject item = (PedidoDetObject)o;
        return this.getCod_articulo()==item.getCod_articulo();
    }
}
