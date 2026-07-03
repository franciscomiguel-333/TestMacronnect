package org.example.repository;

import org.example.model.entity.Articulo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ArticuloRepository extends JpaRepository <Articulo, Long> {
    // Método personalizado para frenar códigos de artículos duplicados en el Service
    boolean existsByCodigo(String codigo);

    // 2. Muestra solo artículos activos en el listado paginado obligatorio (Requisito 3.2)
    Page<Articulo> findByActivoTrue(Pageable pageable);

    // 3. Busca un artículo por codigo y si esta activo
    Optional<Articulo> findByCodigoAndActivoTrue(String codigo);

    // 3. Busca un artículo por codigo independiente si esta activo o no (para poder reactivar articulos)
    Optional<Articulo> findByCodigo(String codigo);
}
