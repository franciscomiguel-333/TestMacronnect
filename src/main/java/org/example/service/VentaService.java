package org.example.service;

import org.example.model.dto.VentaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface VentaService {

    //Registrar una venta
    VentaDTO registrar(VentaDTO ventaDTO);

    //Obtener ventas con paginacion
    Page<VentaDTO> obtenerTodas(Pageable pageable);

    //Consultar venta por folio
    VentaDTO obtenerPorFolio(Long Folio);

    //Cancelar Venta
    void cancelar(Long Folio);

}
