package com.refaccionaria13.controlador;

import com.refaccionaria13.modelo.Prestamo;
import com.refaccionaria13.modelo.Producto;
import com.refaccionaria13.repositorio.PrestamoRepositorio;
import com.refaccionaria13.repositorio.ProductoRepositorio;
import com.refaccionaria13.dto.PrestamoRequestDTO;
import com.refaccionaria13.dto.ActualizarEstadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prestamos")
@CrossOrigin(origins = "*")
public class PrestamoControlador {

    @Autowired
    private PrestamoRepositorio prestamoRepositorio;

    @Autowired
    private ProductoRepositorio productoRepositorio;

    // ==========================================================
    // 1. RECIBIR UNA SOLICITUD DE PRÉSTAMO (De otra sucursal)
    // ==========================================================
    @PostMapping("/solicitar")
    public ResponseEntity<?> solicitarPrestamo(@RequestBody PrestamoRequestDTO request) {
        
        Prestamo nuevoPrestamo = new Prestamo();
        nuevoPrestamo.setSucursalSolicitante(request.getSucursalSolicitante());
        nuevoPrestamo.setSucursalPrestamista("Equipo 13: GP PERUANOS REMASTER"); // Nosotros
        nuevoPrestamo.setIdProducto(request.getIdProducto());
        nuevoPrestamo.setCantidad(request.getCantidad());
        nuevoPrestamo.setEstado("PENDIENTE"); // Arranca como pendiente

        prestamoRepositorio.save(nuevoPrestamo);

        return ResponseEntity.status(HttpStatus.CREATED).body("Solicitud de préstamo registrada con éxito y en espera de aprobación.");
    }

    // ==========================================================
    // 2. VER BANDEJA DE ENTRADA (Para tu Front-end)
    // ==========================================================
    @GetMapping("/pendientes")
    public List<Prestamo> obtenerPrestamosPendientes() {
        // Busca solo los que nos pidieron a nosotros y que no hemos respondido
        return prestamoRepositorio.findBySucursalPrestamistaAndEstado("Equipo 13: GP PERUANOS REMASTER", "PENDIENTE");
    }

    // ==========================================================
    // 3. APROBAR O RECHAZAR EL PRÉSTAMO (El núcleo del sistema)
    // ==========================================================
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstadoPrestamo(
            @PathVariable Integer id, 
            @RequestBody ActualizarEstadoDTO dto) {

        Optional<Prestamo> prestamoOpt = prestamoRepositorio.findById(id);

        if (!prestamoOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Préstamo no encontrado.");
        }

        Prestamo prestamo = prestamoOpt.get();
        String estadoActual = prestamo.getEstado();
        String nuevoEstado = dto.getNuevoEstado().toUpperCase();

        // Validamos que solo se puedan procesar los que están pendientes
        if (!estadoActual.equals("PENDIENTE")) {
            return ResponseEntity.badRequest().body("Este préstamo ya fue procesado (" + estadoActual + ").");
        }

        // Si decides APROBARLO ("ACTIVO")
        if (nuevoEstado.equals("ACTIVO")) {
            Optional<Producto> productoOpt = productoRepositorio.findById(prestamo.getIdProducto());
            
            if (!productoOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Error: El producto ya no existe en la base de datos.");
            }

            Producto producto = productoOpt.get();

            // Verificamos si aún hay stock suficiente para prestar
            if (producto.getStock() < prestamo.getCantidad()) {
                return ResponseEntity.badRequest().body("Stock insuficiente para aprobar este préstamo.");
            }

            // Descontamos el inventario
            producto.setStock(producto.getStock() - prestamo.getCantidad());
            productoRepositorio.save(producto); // Guardamos el nuevo stock
        }

        // Finalmente, actualizamos el estado del préstamo
        prestamo.setEstado(nuevoEstado);
        prestamoRepositorio.save(prestamo);

        return ResponseEntity.ok("Préstamo actualizado exitosamente a: " + nuevoEstado);
    }
}
