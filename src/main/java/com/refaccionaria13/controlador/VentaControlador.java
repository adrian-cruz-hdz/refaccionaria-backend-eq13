package com.refaccionaria13.controlador;

import com.refaccionaria13.dto.VentaRequestDTO;
import com.refaccionaria13.modelo.Producto;
import com.refaccionaria13.modelo.Venta;
import com.refaccionaria13.modelo.VentaIncluye;
import com.refaccionaria13.modelo.VentaIncluyeId;
import com.refaccionaria13.repositorio.ProductoRepositorio;
import com.refaccionaria13.repositorio.VentaIncluyeRepositorio;
import com.refaccionaria13.repositorio.VentaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "${FRONTEND_URL}", methods = {RequestMethod.GET, RequestMethod.POST})
public class VentaControlador {

    @Autowired private VentaRepositorio ventaRepository;
    @Autowired private VentaIncluyeRepositorio detalleRepository;
    @Autowired private ProductoRepositorio productoRepository;

    @PostMapping
    @Transactional // Si algo falla (ej. falta de stock), se cancela TODA la operación en Azure
    public ResponseEntity<?> registrarVenta(@RequestBody VentaRequestDTO request) {
        try {
            // 1. Guardar la Venta (El Ticket general)
            Venta nuevaVenta = new Venta();
            nuevaVenta.setFechaVenta(LocalDateTime.now());
            nuevaVenta.setIdEmpleado(request.getIdEmpleado());
            nuevaVenta.setSubtotal(request.getSubtotal());
            nuevaVenta.setImpuestos(request.getImpuestos());
            nuevaVenta.setTotal(request.getTotal());
            
            // Al guardar, Azure nos autogenera el FOLIO y nos lo devuelve
            Venta ventaGuardada = ventaRepository.save(nuevaVenta);

            // 2. Procesar cada producto del carrito
            for (VentaRequestDTO.ItemVentaDTO item : request.getDetalles()) {
                
                // A) Verificar y descontar inventario
                Producto productoDB = productoRepository.findById(item.getIdProducto())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getIdProducto()));
                
                if (productoDB.getStock() < item.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente para: " + productoDB.getDescripcion());
                }
                
                // Restamos el stock y guardamos
                productoDB.setStock(productoDB.getStock() - item.getCantidad());
                productoRepository.save(productoDB);

                // B) Guardar el detalle de la venta usando la llave compuesta
                VentaIncluyeId idCompuesta = new VentaIncluyeId(ventaGuardada.getFolioVenta(), item.getIdProducto());
                
                VentaIncluye detalle = new VentaIncluye();
                detalle.setId(idCompuesta);
                detalle.setCantidad(item.getCantidad());
                detalle.setPrecioUnitario(item.getPrecioUnitario());
                detalle.setSubtotalLinea(item.getCantidad() * item.getPrecioUnitario());
                
                detalleRepository.save(detalle);
            }

            // Si todo salió bien, devolvemos el ticket con su nuevo folio
            return ResponseEntity.ok(ventaGuardada);

        } catch (Exception e) {
            // Si hay error, devolvemos un Bad Request con el motivo
            return ResponseEntity.badRequest().body("Error al procesar la venta: " + e.getMessage());
        }
    }

    // Método para obtener el historial de ventas (Para el Reporte)
    @GetMapping
    public ResponseEntity<java.util.List<Venta>> obtenerHistorialVentas() {
        // Busca todas las ventas en la base de datos y las devuelve ordenadas
        return ResponseEntity.ok(ventaRepository.findAll());
    }
}