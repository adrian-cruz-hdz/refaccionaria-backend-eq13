package com.refaccionaria13.dto;

public class ItemConsultaDTO {
    
    private String idProducto;
    private int cantidadSolicitada;

    public ItemConsultaDTO() {}

    public String getIdProducto() { return idProducto; }
    public void setIdProducto(String idProducto) { this.idProducto = idProducto; }

    public int getCantidadSolicitada() { return cantidadSolicitada; }
    public void setCantidadSolicitada(int cantidadSolicitada) { this.cantidadSolicitada = cantidadSolicitada; }
}