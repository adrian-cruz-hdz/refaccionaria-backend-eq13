package com.refaccionaria13.repositorio;

import com.refaccionaria13.modelo.VentaIncluye;
import com.refaccionaria13.modelo.VentaIncluyeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaIncluyeRepositorio extends JpaRepository<VentaIncluye, VentaIncluyeId> {
}