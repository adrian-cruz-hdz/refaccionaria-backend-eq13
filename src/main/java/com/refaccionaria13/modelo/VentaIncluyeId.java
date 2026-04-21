package com.refaccionaria13.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class VentaIncluyeId implements Serializable {

    @Column(name = "id_venta") // Minúsculas
    private Integer folioVenta; // Coincide con el Long de la Venta

    @Column(name = "id_producto") // Minúsculas y guion bajo
    private String idProducto;

    // Constructor vacío obligatorio para Spring
    public VentaIncluyeId() {}

    public VentaIncluyeId(Integer folioVenta, String idProducto) {
        this.folioVenta = folioVenta;
        this.idProducto = idProducto;
    }

    // -- GET & SET

    public Integer getFolioVenta() { return folioVenta; }
    public void setFolioVenta(Integer folioVenta) { this.folioVenta = folioVenta; }
    
    public String getIdProducto() { return idProducto; }
    public void setIdProducto(String idProducto) { this.idProducto = idProducto; }   
}