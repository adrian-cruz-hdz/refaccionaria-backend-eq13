package com.refaccionaria13.dto;

public class ActualizarEstadoDTO {
    private String nuevoEstado; // Ej. "ACTIVO", "RECHAZADO"

    public ActualizarEstadoDTO() {}

    public String getNuevoEstado() { return nuevoEstado; }
    public void setNuevoEstado(String nuevoEstado) { this.nuevoEstado = nuevoEstado; }
}
