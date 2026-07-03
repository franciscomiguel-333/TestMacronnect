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

    @Override
    @Transactional
    public ClienteDTO registrar(ClienteDTO dto){

        //Validaciones
        if(clienteRepository.existsByEmail(dto.getEmail())){
            throw new IllegalArgumentException("El correo electrónico ya esta registrado por otro usuario");
        }
        if(clienteRepository.existsByTelefono(dto.getTelefono())){
            throw new IllegalArgumentException("El teléfono ya esta registrado por otro usuario");
        }
        Cliente cliente = mapearAEntidad(dto);
        return mapearADto(clienteRepository.save(cliente));
    }


    @Override
    @Transactional(readOnly = true)
    public Page<ClienteDTO> obtenerClientes(Pageable pageable) {
        Page<Cliente> paginaClientes = clienteRepository.findByActivoTrue(pageable);
        return paginaClientes.map(this::mapearADto);
    }

    @Override
    @Transactional
    public ClienteDTO actualizar(String correo, ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.findByEmail(correo)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el correo en la base de datos"));

        cliente.setTelefono(clienteDTO.getTelefono());
        cliente.setActivo(clienteDTO.isActivo());
        cliente.setTelefono(clienteDTO.getTelefono());
        cliente.setDireccion(clienteDTO.getDireccion());
        return mapearADto(clienteRepository.save(cliente));
    }

    @Override
    @Transactional
    public void darDeBaja(String correo) {
        Cliente cliente = clienteRepository.findByEmail(correo)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el correo en la base de datos"));
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public void reactivar(String correo) {
        Cliente cliente = clienteRepository.findByEmail(correo)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el correo en la base de datos"));
        cliente.setActivo(true);
        clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteDTO obtenerByEmail(String correo) {
        Cliente cliente = clienteRepository.findByEmail(correo)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el correo en la base de datos"));
        return mapearADto(cliente);
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

    //Mapear a Entidad
    private Cliente mapearAEntidad(ClienteDTO dto){
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setDireccion(dto.getDireccion());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        return cliente;
    }
}
