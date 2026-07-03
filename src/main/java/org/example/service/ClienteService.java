package org.example.service;

import org.example.model.dto.ClienteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ClienteService {

    //Insert
    ClienteDTO registrar(ClienteDTO clienteDTO);
    //GetAll
    Page<ClienteDTO> obtenerClientes(Pageable pageable);
    //Update
    ClienteDTO actualizar(String correo, ClienteDTO clienteDTO);
    //Baja
    void darDeBaja(String correo);
    //Reactivar
    void reactivar(String correo);

    ClienteDTO obtenerByEmail(String email);
}
