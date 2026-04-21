package com.refaccionaria13.controlador;

import com.refaccionaria13.modelo.Empleado;
import com.refaccionaria13.repositorio.EmpleadoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "${FRONTEND_URL}", methods = {RequestMethod.POST})
public class LoginControlador {

    @Autowired
    private EmpleadoRepositorio empleadoRepository;

    @PostMapping
    public ResponseEntity<?> iniciarSesion(@RequestBody Map<String, String> credenciales) {
        // Extraemos los datos que nos enviará el HTML
        String usuario = credenciales.get("usuario");
        String password = credenciales.get("password");

        // Vamos a la base de datos a preguntar si existe
        Optional<Empleado> empleado = empleadoRepository.findByUsuarioAndContrasenaAndEstatusTrue(usuario, password);

        if (empleado.isPresent()) {
            // ¡Éxito! Las credenciales son correctas.
            // Devolvemos todos los datos del empleado (Nombre, Rol, etc.) para usarlos en el sistema
            return ResponseEntity.ok(empleado.get());
        } else {
            // Falla: Usuario incorrecto, contraseña incorrecta o dado de baja
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"mensaje\": \"Usuario o contraseña incorrectos\"}");
        }
    }
}