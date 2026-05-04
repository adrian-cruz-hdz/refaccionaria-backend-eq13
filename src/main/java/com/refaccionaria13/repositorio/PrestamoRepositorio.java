package com.refaccionaria13.repositorio;

import com.refaccionaria13.modelo.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, Integer> {

    // Buscar las solicitudes que te han hecho a ti (como prestamista) filtradas por estado
    List<Prestamo> findBySucursalPrestamistaAndEstado(String sucursalPrestamista, String estado);

    // Buscar el historial de todas las solicitudes que tú has enviado a otros (como solicitante)
    List<Prestamo> findBySucursalSolicitante(String sucursalSolicitante);
}