package com.refaccionaria13.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "venta_incluye") // Minúsculas
public class VentaIncluye {

    @EmbeddedId
    private VentaIncluyeId id;

    @Column(name = "cantidad")
    private Integer cantidad; // Integer está perfecto para contar piezas físicas

    @Column(name = "precio_unitario")
    private Double precioUnitario; // Cambiamos a BigDecimal

    // Mantenemos el bloqueo de inserción, pero en minúsculas y BigDecimal
    @Column(name = "subtotal_linea")
    private Double subtotalLinea;

    // -- GET & SET

    public VentaIncluyeId getId() { return id; }
    public void setId(VentaIncluyeId id) { this.id = id; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }

    public Double getSubtotalLinea() { return subtotalLinea; }
    public void setSubtotalLinea(Double subtotalLinea) { this.subtotalLinea = subtotalLinea; }     
}