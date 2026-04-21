package com.refaccionaria13.dto;

import java.util.List;

public class ConsultaStockRequestDTO {
    
    private List<ItemConsultaDTO> productos;

    public ConsultaStockRequestDTO() {}

    public List<ItemConsultaDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<ItemConsultaDTO> productos) {
        this.productos = productos;
    }
}