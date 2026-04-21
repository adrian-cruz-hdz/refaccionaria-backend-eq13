package com.refaccionaria13.repositorio;

import com.refaccionaria13.modelo.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmpleadoRepositorio extends JpaRepository<Empleado, Integer> {
    
    Optional<Empleado> findByUsuarioAndContrasenaAndEstatusTrue(String usuario, String contrasena);
}