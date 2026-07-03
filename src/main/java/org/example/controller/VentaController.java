package org.example.controller;

import org.example.model.dto.VentaDTO;
import org.example.model.dto.VentaDetalleDTO;
import org.example.model.dto.VentaDetalleRequestDTO;
import org.example.model.dto.VentaRequestDTO;
import org.example.model.entity.Venta;
import org.example.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @PostMapping
    public ResponseEntity<VentaDTO> registrar(@Valid @RequestBody VentaRequestDTO dto) {
        VentaDTO respuesta = ventaService.registrar(dto);
        return new ResponseEntity<>(respuesta, HttpStatus.CREATED); // HTTP 201
    }

    @GetMapping
    public ResponseEntity<Page<VentaDTO>> obtenerTodas(Pageable pageable) {
        Page<VentaDTO> pagina = ventaService.obtenerTodas(pageable);
        return ResponseEntity.ok(pagina); // HTTP 200
    }

    @GetMapping("/{folio}")
    public ResponseEntity<VentaDTO> obtenerPorFolio(@PathVariable Long folio) {
        VentaDTO venta = ventaService.obtenerPorFolio(folio);
        return ResponseEntity.ok(venta); // HTTP 200
    }

    @DeleteMapping("/{folio}")
    public ResponseEntity<Void> cancelar(@PathVariable Long folio) {
        ventaService.cancelar(folio);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

}
