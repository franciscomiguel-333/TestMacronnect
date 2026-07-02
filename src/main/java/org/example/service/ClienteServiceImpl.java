package org.example.service;

import org.example.model.dto.ClienteDTO;
import org.example.model.entity.Cliente;
import org.example.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override // Abre una transaccion en la BD para validar que se guardaron los cambios
    @Transactional
    public ClienteDTO registrar(ClienteDTO dto){

        //Validaciones
        if(clienteRepository.existsByEmail(dto.getEmail())){
            throw new IllegalArgumentException("El correo electrónico ya esta registrado por otro usuario");
        }
        if(clienteRepository.existsByTelefono(dto.getTelefono())){
            throw new IllegalArgumentException("El teléfono ya esta registrado por otro usuario");
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setDireccion(dto.getDireccion());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());

        return mapearADto(clienteRepository.save(cliente));
    }


    @Override
    @Transactional(readOnly = true)
    public Page<ClienteDTO> obtenerClientes(Pageable pageable) {
        Page<Cliente> paginaClientes = clienteRepository.findAll(pageable);
        return paginaClientes.map(this::mapearADto);
    }


    // Mapear Entidad a DTO
    private ClienteDTO mapearADto(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setEmail(cliente.getEmail());
        dto.setTelefono(cliente.getTelefono());
        dto.setDireccion(cliente.getDireccion());
        return dto;
    }

}
