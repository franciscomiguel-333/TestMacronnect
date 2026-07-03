package org.example.repository;

import org.example.model.entity.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    Page<Venta> findAll(Pageable pageable);

    Optional<Venta> findByFolio(Long folio);

}
