package org.example.repository;


import org.example.model.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface ClienteRepository extends JpaRepository<Cliente,Long> {

    // Validaciones
    boolean existsByEmail(String email);
    boolean existsByTelefono(String telefono);

    // Getall()
    Page<Cliente> findByActivoTrue(Pageable pageable);

    // Búsqueda por email ya que el dato de ID se usa internamente, otra solucion seria añadir campo codigo
    Optional<Cliente> findByEmail(String correo);

}
