package org.example.controller;

import org.example.model.dto.ClienteDTO;
import org.example.model.entity.Cliente;
import org.example.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    //Endpoint Registrar Cliente
    @PostMapping
    public ResponseEntity<ClienteDTO> registrarCliente(@Valid @RequestBody ClienteDTO clienteDTO){
        ClienteDTO nuevoCliente = clienteService.registrar(clienteDTO);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED); //HTTP 21 CREATED
    }

    //Obtener Paginado
    @GetMapping
    public ResponseEntity<Page<ClienteDTO>> obtenerClientes(Pageable pageable){
        Page<ClienteDTO> pagina = clienteService.obtenerClientes(pageable);
        return ResponseEntity.ok(pagina); //HTTP 200 OK
    }
}
