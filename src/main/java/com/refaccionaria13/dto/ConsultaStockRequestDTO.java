package com.refaccionaria13.dto;
import java.util.List;

public class ConsultaStockRequestDTO {
    private String sucursalDestino; // Quien recibe (en este caso, tú)
    private List<ItemConsultaDTO> productos;

    // Getters y Setters
    public String getSucursalDestino() { return sucursalDestino; }
    public void setSucursalDestino(String sucursalDestino) { this.sucursalDestino = sucursalDestino; }

    public List<ItemConsultaDTO> getProductos() { return productos; }
    public void setProductos(List<ItemConsultaDTO> productos) { this.productos = productos; }
}
