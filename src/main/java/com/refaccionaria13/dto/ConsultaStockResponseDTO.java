package com.refaccionaria13.dto;

public class ConsultaStockResponseDTO {
    private String idProducto;
    private String descripcion;
    private int cantidadSolicitada;
    private int stockDisponible;
    private String estatus; // Puede ser: "SUFICIENTE", "PARCIAL" o "SIN_STOCK"

    // Constructores, Getters y Setters
    public ConsultaStockResponseDTO() {}

    public String getIdProducto() { return idProducto; }
    public void setIdProducto(String idProducto) { this.idProducto = idProducto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getCantidadSolicitada() { return cantidadSolicitada; }
    public void setCantidadSolicitada(int cantidadSolicitada) { this.cantidadSolicitada = cantidadSolicitada; }

    public int getStockDisponible() { return stockDisponible; }
    public void setStockDisponible(int stockDisponible) { this.stockDisponible = stockDisponible; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }
}