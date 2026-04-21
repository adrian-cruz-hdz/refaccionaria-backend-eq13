package com.refaccionaria13.repositorio;

import com.refaccionaria13.modelo.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, String> {
    
    List<Producto> findAllByEstatusTrueOrderByStockAsc();

    @Query("SELECT p FROM Producto p WHERE p.estatus = true AND " +
           "(LOWER(p.idProducto) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))) " +
           "ORDER BY p.stock ASC")
    List<Producto> buscarPorIdODescripcion(@Param("termino") String termino);
}