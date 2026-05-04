package com.refaccionaria13.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro_prestamos") // Vincula exactamente con el nombre de tu tabla en Supabase
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestamo")
    private Integer idPrestamo;

    @Column(name = "id_producto", nullable = false)
    private String idProducto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "sucursal_solicitante", nullable = false)
    private String sucursalSolicitante;

    @Column(name = "sucursal_prestamista", nullable = false)
    private String sucursalPrestamista;

    @Column(nullable = false)
    private String estado;

    @Column(name = "fecha_solicitud")
    private LocalDateTime fechaSolicitud;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // ==========================================
    // MÉTODOS AUTOMÁTICOS PARA FECHAS Y ESTADO
    // ==========================================
    @PrePersist
    protected void alCrear() {
        fechaSolicitud = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (estado == null) {
            estado = "PENDIENTE"; // Estado por defecto
        }
    }

    @PreUpdate
    protected void alActualizar() {
        fechaActualizacion = LocalDateTime.now();
    }

    // ==========================================
    // CONSTRUCTORES, GETTERS Y SETTERS
    // ==========================================
    public Prestamo() {}

    public Integer getIdPrestamo() { return idPrestamo; }
    public void setIdPrestamo(Integer idPrestamo) { this.idPrestamo = idPrestamo; }

    public String getIdProducto() { return idProducto; }
    public void setIdProducto(String idProducto) { this.idProducto = idProducto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getSucursalSolicitante() { return sucursalSolicitante; }
    public void setSucursalSolicitante(String sucursalSolicitante) { this.sucursalSolicitante = sucursalSolicitante; }

    public String getSucursalPrestamista() { return sucursalPrestamista; }
    public void setSucursalPrestamista(String sucursalPrestamista) { this.sucursalPrestamista = sucursalPrestamista; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}