package com.refaccionaria13.modelo;

import java.util.List;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ventas") // Todo a minúsculas
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta") // Minúsculas
    private Integer folioVenta;

    @Column(name = "fecha_venta") // Estandarizamos a minúsculas con guion bajo
    private LocalDateTime fechaVenta;

    @Column(name = "id_empleado")
    private Integer idEmpleado; // Cambiamos de Integer a Long

    @Column(name = "subtotal")
    private Double subtotal; // Cambiamos de Double a BigDecimal

    @Column(name = "impuestos")
    private Double impuestos; 

    @Column(name = "total")
    private Double total; 

    @OneToMany
    // Actualizamos los nombres de las columnas para que coincidan con Postgres
    @JoinColumn(name = "id_venta", referencedColumnName = "id_venta", insertable = false, updatable = false)
    private List<VentaIncluye> detalles;

    // -- GET & SET

    public Integer getFolioVenta() { return folioVenta; }
    public void setFolioVenta(Integer folioVenta) { this.folioVenta = folioVenta; }

    public LocalDateTime getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDateTime fechaVenta) { this.fechaVenta = fechaVenta; }

    public Integer getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Integer idEmpleado) { this.idEmpleado = idEmpleado; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getImpuestos() { return impuestos; }
    public void setImpuestos(Double impuestos) { this.impuestos = impuestos; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public List<VentaIncluye> getDetalles() { return detalles; }
    public void setDetalles(List<VentaIncluye> detalles) { this.detalles = detalles; }
}
