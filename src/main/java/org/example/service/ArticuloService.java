package org.example.service;

import org.example.model.dto.ArticuloDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticuloService {

    ArticuloDTO registrar(ArticuloDTO articuloDTO);
    Page<ArticuloDTO> obtenerArticulos(Pageable pageable);
    ArticuloDTO actualizar(String codigo, ArticuloDTO articuloDTO); // <-- Cambiado
    void darDeBaja(String codigo); // <-- Cambiado
    void reactivar(String codigo); // <-- Cambiado
    ArticuloDTO obtenerPorCodigo(String codigo);
}
