package com.refaccionaria13.controlador;

import com.refaccionaria13.modelo.Prestamo;
import com.refaccionaria13.modelo.Producto;
import com.refaccionaria13.repositorio.PrestamoRepositorio;
import com.refaccionaria13.repositorio.ProductoRepositorio;
import com.refaccionaria13.dto.PrestamoRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    // DICCIONARIO DE API KEYS 
    // ==========================================================
    private static final Map<String, String> SUCURSALES_AUTORIZADAS = Map.ofEntries(
        Map.entry("KEY-EQ01-LOSREPROBADOS-8A3B", "Equipo 01: LOS REPROBADOS"),
        Map.entry("KEY-EQ02-UNPARDE2-4C9D", "Equipo 02: UN PAR DE 2"),
        Map.entry("KEY-EQ03-PADILLORK-7F2E", "Equipo 03: PADILLORK"),
        Map.entry("KEY-EQ04-LOSCUADRADO-1B5A", "Equipo 04: lOS E AL CUADRADO"),
        Map.entry("KEY-EQ05-SUCURSAL05-9D6C", "Equipo 05"),
        Map.entry("KEY-EQ06-ECONOMICOS-3E8F", "Equipo 06: LOS ECONOMIcOS"),
        Map.entry("KEY-EQ07-LOSPAPUS-5A2B", "Equipo 07: LOS PAPUS"),
        Map.entry("KEY-EQ08-LOSPLEBES-6C4D", "Equipo 08: LOS PLEBES"),
        Map.entry("KEY-EQ09-ARSENICOS-2F1E", "Equipo 09: ARSENICOS"),
        Map.entry("KEY-EQ10-MOJARRAS-8B3A", "Equipo 10: LAS MOJARRAS"),
        Map.entry("KEY-EQ11-NIFUNIFA-4D7C", "Equipo 11: NI FU NI FA"),
        Map.entry("KEY-EQ12-INGEMENSOS-9E5F", "Equipo 12: LOS INGE MENSOS"),
        Map.entry("KEY-EQ13-GPPERUANOS-1A6B", "Equipo 13: GP PERUANOS REMASTER"),
        Map.entry("KEY-EQ14-GP-7C2D", "Equipo 14: GP"),
        Map.entry("KEY-EQ15-FORANEOS-3F8E", "Equipo 15: FORANEOS"),
        Map.entry("KEY-MASTER-ADMIN-0000", "Administrador / Profesor")
    );

    // ==========================================================
    // 1. RECIBIR UNA SOLICITUD DE PRÉSTAMO (De otra sucursal)
    // ==========================================================
    @PostMapping("/solicitar")
    public ResponseEntity<?> solicitarPrestamo(
            @RequestHeader(value = "X-API-KEY", required = false) String apiKey,
            @RequestBody PrestamoRequestDTO request) {
        
        // VALIDACIÓN DE SEGURIDAD
        if (apiKey == null || apiKey.trim().isEmpty() || !SUCURSALES_AUTORIZADAS.containsKey(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Acceso denegado: API KEY inválida o ausente.");
        }

        String nombreSucursal = SUCURSALES_AUTORIZADAS.get(apiKey);

        Prestamo nuevoPrestamo = new Prestamo();
        // Guardamos el nombre real validado por la API Key, no el que ellos escriban en el JSON
        nuevoPrestamo.setSucursalSolicitante(nombreSucursal); 
        nuevoPrestamo.setSucursalPrestamista("Equipo 13: GP PERUANOS REMASTER"); 
        nuevoPrestamo.setIdProducto(request.getIdProducto());
        nuevoPrestamo.setCantidad(request.getCantidad());
        nuevoPrestamo.setEstado("PENDIENTE"); 

        Prestamo prestamoGuardado = prestamoRepositorio.save(nuevoPrestamo);
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamoGuardado);
    }

    // ==========================================================
    // 2. VER BANDEJA DE ENTRADA (Para tu Front-end)
    // ==========================================================
    @GetMapping("/pendientes")
    public List<Prestamo> obtenerPrestamosPendientes() {
        return prestamoRepositorio.findBySucursalPrestamistaAndEstado("Equipo 13: GP PERUANOS REMASTER", "PENDIENTE");
    }

    // =========================================================
    // ENDPOINT BLINDADO PARA ACTUALIZAR ESTADO
    // =========================================================
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstadoPrestamo(
            @PathVariable Integer id, 
            @RequestBody Map<String, String> request) {
        
        // Preparamos un JSON seguro para responder
        Map<String, String> response = new HashMap<>();
        
        try {
            Optional<Prestamo> prestamoOpt = prestamoRepositorio.findById(id);

            if (!prestamoOpt.isPresent()) {
                response.put("error", "Préstamo no encontrado en la base de datos.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Prestamo prestamo = prestamoOpt.get();
            String nuevoEstado = request.get("estado");
            
            // FILTRO DE SEGURIDAD: Evita que el programa choque si el Front-end manda un dato vacío
            if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
                response.put("error", "El Front-End no envió la variable 'estado' en el JSON.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // 1. Actualizamos el texto
            prestamo.setEstado(nuevoEstado);
            String mensajeExito = "Estado actualizado a " + nuevoEstado;

            // 2. LA LÓGICA MATEMÁTICA
            if ("COMPLETADO".equalsIgnoreCase(nuevoEstado)) {
                // Revisa que los métodos get coincidan con tu código real
                Optional<Producto> productoOpt = productoRepositorio.findById(prestamo.getIdProducto());
                
                if (productoOpt.isPresent()) {
                    Producto producto = productoOpt.get();
                    producto.setStock(producto.getStock() + prestamo.getCantidad());
                    productoRepositorio.save(producto);
                    mensajeExito += ". ¡Inventario sumado correctamente!";
                } else {
                    mensajeExito += ". ALERTA: No se sumó inventario porque no existe el SKU.";
                }
            }

            // 3. Guardamos el cambio
            prestamoRepositorio.save(prestamo);
            response.put("mensaje", mensajeExito);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            // SI ALGO EXPLOTA, ESTO NOS DIRÁ EXACTAMENTE QUÉ FUE
            e.printStackTrace(); 
            response.put("error", "Choque interno en Java: " + e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ==========================================================
    // 4. CONSULTAR ESTADO DEL PRÉSTAMO (Para la otra sucursal)
    // ==========================================================
    @GetMapping("/{id}")
    public ResponseEntity<?> consultarEstadoPrestamo(
            @RequestHeader(value = "X-API-KEY", required = false) String apiKey,
            @PathVariable Integer id) {
            
        // Validación de seguridad opcional pero recomendada
        if (apiKey == null || apiKey.trim().isEmpty() || !SUCURSALES_AUTORIZADAS.containsKey(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Acceso denegado: API KEY inválida o ausente.");
        }

        Optional<Prestamo> prestamoOpt = prestamoRepositorio.findById(id);

        if (!prestamoOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Préstamo no encontrado.");
        }

        return ResponseEntity.ok(prestamoOpt.get());
    }

    // =========================================================
    // RESTAR STOCK (Para cuando apruebas un préstamo)
    // =========================================================
    @PutMapping("/{id}/restar-stock")
    public ResponseEntity<?> restarStock(
            @PathVariable String id, // Asumiendo que tu ID de producto es String (ej. SKU-01)
            @RequestBody Map<String, Integer> request) {
        
        Optional<Producto> productoOpt = productoRepositorio.findById(id);
        
        if (!productoOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Producto no encontrado.\"}");
        }

        Producto producto = productoOpt.get();
        Integer cantidadARestar = request.get("cantidad");

        if (cantidadARestar == null || cantidadARestar <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Cantidad inválida.\"}");
        }

        // Validación de seguridad: Evitar stock negativo
        if (producto.getStock() < cantidadARestar) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Stock insuficiente para prestar.\"}");
        }

        // La matemática: Restamos el stock
        producto.setStock(producto.getStock() - cantidadARestar);
        
        // Guardamos en la base de datos
        productoRepositorio.save(producto);
        
        return ResponseEntity.ok("{\"mensaje\": \"Stock restado correctamente. Nuevo stock: " + producto.getStock() + "\"}");
    }
}