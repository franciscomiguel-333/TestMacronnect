package org.example.service;

import org.example.model.dto.ClienteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ClienteService {

    // Registrar
    ClienteDTO registrar(ClienteDTO clienteDTO);

    // Listado)
    Page<ClienteDTO> obtenerClientes(Pageable pageable);

}
