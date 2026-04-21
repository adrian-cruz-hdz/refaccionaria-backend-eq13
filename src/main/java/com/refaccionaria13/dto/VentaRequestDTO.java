package com.refaccionaria13.dto;

import java.util.List;

public class VentaRequestDTO {
    private Integer idEmpleado;
    private Double subtotal;
    private Double impuestos;
    private Double total;
    private List<ItemVentaDTO> detalles;

    // Getters y Setters
    public Integer getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Integer idEmpleado) { this.idEmpleado = idEmpleado; }
    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    public Double getImpuestos() { return impuestos; }
    public void setImpuestos(Double impuestos) { this.impuestos = impuestos; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public List<ItemVentaDTO> getDetalles() { return detalles; }
    public void setDetalles(List<ItemVentaDTO> detalles) { this.detalles = detalles; }

    // Clase interna para representar cada fila de producto en el carrito
    public static class ItemVentaDTO {
        private String idProducto;
        private Integer cantidad;
        private Double precioUnitario;

        // Getters y Setters
        public String getIdProducto() { return idProducto; }
        public void setIdProducto(String idProducto) { this.idProducto = idProducto; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public Double getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }
    }
}
