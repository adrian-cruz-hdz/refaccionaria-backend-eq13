package com.refaccionaria13.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @Column(name = "id_producto")
    private String idProducto; 

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "marca")
    private String marca;

    // Supabase usa int8, así que en Java usamos Long
    @Column(name = "categoria")
    private Integer categoria; 

    @Column(name = "stock")
    private Integer stock; 

    @Column(name = "precio_venta")
    private Double precioVenta; 

    @Column(name = "estatus")
    private Boolean estatus;

    // -- GET & SET

    public String getIdProducto() { return idProducto; }
    public void setIdProducto(String idProducto) { this.idProducto = idProducto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public Integer getCategoria() { return categoria; }
    public void setCategoria(Integer categoria) { this.categoria = categoria; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(Double precioVenta) { this.precioVenta = precioVenta; }

    public Boolean getEstatus() { return estatus; }
    public void setEstatus(Boolean estatus) { this.estatus = estatus; }    
}