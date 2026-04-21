package com.refaccionaria13.controlador;

import com.refaccionaria13.modelo.Producto;
import com.refaccionaria13.repositorio.ProductoRepositorio;
import com.refaccionaria13.dto.ConsultaStockRequestDTO;
import com.refaccionaria13.dto.ItemConsultaDTO;
import com.refaccionaria13.dto.ItemRespuestaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*") // Permite conexiones desde el Front-end
public class ProductoControlador {

    @Autowired
    private ProductoRepositorio productoRepository;

    // ==========================================================
    // DICCIONARIO DE API KEYS PARA TODAS LAS SUCURSALES
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
    // 1. OBTENER INVENTARIO COMPLETO (Para tu propia sucursal)
    // ==========================================================
    @GetMapping
    public List<Producto> obtenerInventario() {
        return productoRepository.findAllByEstatusTrueOrderByStockAsc(); 
    }

    // ==========================================================
    // 2. BUSCAR PRODUCTOS (Buscador en tiempo real)
    // ==========================================================
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarProductos(@RequestParam(value = "q", defaultValue = "") String termino) {
        if (termino.trim().isEmpty()) {
            return ResponseEntity.ok(productoRepository.findAllByEstatusTrueOrderByStockAsc());
        }
        List<Producto> resultados = productoRepository.buscarPorIdODescripcion(termino);
        return ResponseEntity.ok(resultados);
    }

    // ==========================================================
    // 3. CONSULTA DE STOCK EXTERNA (Blindada con API KEY)
    // ==========================================================
    @PostMapping("/consulta-stock")
    public ResponseEntity<?> consultarStockParaSucursal(
            @RequestHeader(value = "X-API-KEY", required = false) String apiKey,
            @RequestBody ConsultaStockRequestDTO request) {
        
        // A) VALIDACIÓN DE SEGURIDAD
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Acceso denegado: Se requiere una API KEY en la cabecera 'X-API-KEY'.");
        }

        if (!SUCURSALES_AUTORIZADAS.containsKey(apiKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado: API KEY inválida o revocada.");
        }

        // B) IDENTIFICACIÓN EN CONSOLA (Para control de préstamos a futuro)
        String nombreSucursal = SUCURSALES_AUTORIZADAS.get(apiKey);
        System.out.println(">>> ALERTA: Petición de inventario recibida desde: " + nombreSucursal);

        // C) LÓGICA DE NEGOCIO Y CÁLCULO DE STOCK
        List<ItemRespuestaDTO> respuesta = new ArrayList<>();

        for (ItemConsultaDTO itemReq : request.getProductos()) {
            ItemRespuestaDTO res = new ItemRespuestaDTO();
            res.setIdProducto(itemReq.getIdProducto());
            res.setCantidadSolicitada(itemReq.getCantidadSolicitada());

            Optional<Producto> prodOpt = productoRepository.findById(itemReq.getIdProducto());

            if (prodOpt.isPresent() && prodOpt.get().getEstatus()) {
                Producto miProducto = prodOpt.get();
                res.setDescripcion(miProducto.getDescripcion());
                res.setStockDisponible(miProducto.getStock());

                if (miProducto.getStock() >= itemReq.getCantidadSolicitada()) {
                    res.setEstatus("SUFICIENTE");
                } else if (miProducto.getStock() > 0) {
                    res.setEstatus("PARCIAL");
                } else {
                    res.setEstatus("SIN_STOCK");
                }
            } else {
                res.setDescripcion("Producto no catalogado");
                res.setStockDisponible(0);
                res.setEstatus("NO_ENCONTRADO");
            }
            respuesta.add(res);
        }

        return ResponseEntity.ok(respuesta);
    }

    // ==========================================================
    // 4. MÉTODOS CRUD ESTÁNDAR DE ADMINISTRACIÓN (Mantenlos si los tienes)
    // ==========================================================
    // Ejemplo:
    // @PostMapping
    // public ResponseEntity<Producto> guardarProducto(@RequestBody Producto producto) { ... }
    
}