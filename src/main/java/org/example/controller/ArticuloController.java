package org.example.controller;

import io.swagger.models.Response;
import org.example.model.dto.ArticuloDTO;
import org.example.model.entity.Articulo;
import org.example.service.ArticuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/articulos")
public class ArticuloController {

    @Autowired
    private ArticuloService articuloService;

    @PostMapping
    public ResponseEntity<ArticuloDTO> registrarArticulo(@Valid @RequestBody ArticuloDTO articulo){
        ArticuloDTO nuevo = articuloService.registrar(articulo);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED); //HTTP 21 CREATED
    }

    @GetMapping
    public ResponseEntity<Page<ArticuloDTO>> obtenerArticulos(Pageable pageable){
        Page<ArticuloDTO> pagina = articuloService.obtenerArticulos(pageable);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<ArticuloDTO> obtenerPorCodigo(@PathVariable String codigo) {
        ArticuloDTO articulo = articuloService.obtenerPorCodigo(codigo);
        return ResponseEntity.ok(articulo);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<ArticuloDTO> actualizarArticulo(@PathVariable String codigo, @Valid @RequestBody ArticuloDTO dto){
        ArticuloDTO actualizado = articuloService.actualizar(codigo,dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> darDeBaja(@PathVariable String codigo) {
        articuloService.darDeBaja(codigo);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{codigo}/reactivar")
    public ResponseEntity<Void> reactivar(@PathVariable String codigo) {
        articuloService.reactivar(codigo);
        return ResponseEntity.noContent().build();
    }


}
