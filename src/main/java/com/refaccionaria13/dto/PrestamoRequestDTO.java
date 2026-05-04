package com.refaccionaria13.dto;

public class PrestamoRequestDTO {
    private String sucursalSolicitante;
    private String idProducto;
    private int cantidad;

    // Constructores, getters y setters
    public PrestamoRequestDTO() {}

    public String getSucursalSolicitante() { return sucursalSolicitante; }
    public void setSucursalSolicitante(String sucursalSolicitante) { this.sucursalSolicitante = sucursalSolicitante; }

    public String getIdProducto() { return idProducto; }
    public void setIdProducto(String idProducto) { this.idProducto = idProducto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}