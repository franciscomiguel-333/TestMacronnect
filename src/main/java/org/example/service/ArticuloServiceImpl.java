package org.example.service;

import org.example.model.dto.ArticuloDTO;
import org.example.model.entity.Articulo;
import org.example.repository.ArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ArticuloServiceImpl implements ArticuloService {

    @Autowired
    private ArticuloRepository articuloRepository;

    // Valor referencia para precio minimo
    BigDecimal precioMinimo = new BigDecimal("0.01");

    @Override
    @Transactional
    public ArticuloDTO registrar(ArticuloDTO articuloDTO) {
        if(articuloRepository.existsByCodigo(articuloDTO.getCodigo())){
            throw new IllegalArgumentException("El código de producto ya existe");
        }
        if(articuloDTO.getPrecio().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("El precio debe ser un monto mayor a cero");
        }
        if(articuloDTO.getStock() < 0){
            throw new IllegalArgumentException("El stock debe ser mayor a 0");
        }
        Articulo articulo = mapearAEntidad(articuloDTO);
        return mapearADto(articuloRepository.save(articulo));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArticuloDTO> obtenerArticulos(Pageable pageable) {
        Page<Articulo> paginaArticulos = articuloRepository.findByActivoTrue(pageable);
        return paginaArticulos.map(this::mapearADto);
    }


    @Override
    @Transactional
    public ArticuloDTO actualizar(String codigo, ArticuloDTO dto) {
        Articulo articulo = articuloRepository.findByCodigoAndActivoTrue(codigo)
                .orElseThrow(() -> new IllegalArgumentException("El artículo con código " + codigo + " no existe o está inactivo"));

        if (dto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser un monto mayor a cero");
        }
        if (dto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser un número negativo");
        }

        articulo.setNombre(dto.getNombre());
        articulo.setDescripcion(dto.getDescripcion());
        articulo.setPrecio(dto.getPrecio());
        articulo.setStock(dto.getStock());
        articulo.setActivo(dto.isActivo());
        return mapearADto(articuloRepository.save(articulo));
    }


    @Override
    @Transactional
    public void darDeBaja(String codigo) {
        Articulo articulo = articuloRepository.findByCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("No se puede realizar la operación: El artículo con código " + codigo + " no existe"));
        articulo.setActivo(false);
        articuloRepository.save(articulo);
    }

    @Override
    @Transactional
    public void reactivar(String codigo) {
        Articulo articulo = articuloRepository.findByCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("No se puede realizar la operación: El artículo con código " + codigo + " no existe"));
        articulo.setActivo(true);
        articuloRepository.save(articulo);
    }

    @Override
    public ArticuloDTO obtenerPorCodigo(String codigo) {
        Articulo articulo = articuloRepository.findByCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("No se puede realizar la operación: El artículo con código " + codigo + " no existe"));
        return mapearADto(articulo);
    }

    // Mapear Entidad a DTO
    private ArticuloDTO mapearADto(Articulo articulo) {
        ArticuloDTO dto = new ArticuloDTO();
        dto.setId(articulo.getId());
        dto.setStock(articulo.getStock());
        dto.setPrecio(articulo.getPrecio());
        dto.setActivo(articulo.isActivo());
        dto.setCodigo(articulo.getCodigo());
        dto.setNombre(articulo.getNombre());
        dto.setDescripcion(articulo.getDescripcion());
        return dto;
    }

    // Mapear DTO a Entidad
    private Articulo mapearAEntidad(ArticuloDTO dto) {
        Articulo articulo = new Articulo();
        articulo.setId(dto.getId());
        articulo.setStock(dto.getStock());
        articulo.setPrecio(dto.getPrecio());
        articulo.setActivo(dto.isActivo());
        articulo.setCodigo(dto.getCodigo());
        articulo.setNombre(dto.getNombre());
        articulo.setDescripcion(dto.getDescripcion());
        return articulo;
    }


}
