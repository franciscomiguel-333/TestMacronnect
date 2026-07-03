package org.example.service;

import org.example.model.dto.VentaDTO;
import org.example.model.dto.VentaRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface VentaService {

    //Registrar una venta
    VentaDTO registrar(VentaRequestDTO ventaRequestDTO);

    //Obtener ventas con paginacion
    Page<VentaDTO> obtenerTodas(Pageable pageable);

    //Consultar venta por folio
    VentaDTO obtenerPorFolio(Long Folio);

    //Cancelar Venta
    void cancelar(Long Folio);

}
