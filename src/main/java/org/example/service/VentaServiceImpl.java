package org.example.service;

import org.example.model.dto.VentaDTO;
import org.example.model.dto.VentaDetalleDTO;
import org.example.model.dto.VentaDetalleRequestDTO;
import org.example.model.dto.VentaRequestDTO;
import org.example.model.entity.*;
import org.example.repository.ArticuloRepository;
import org.example.repository.ClienteRepository;
import org.example.repository.FolioRepository;
import org.example.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class VentaServiceImpl implements VentaService  {

    @Autowired // Ahora el IDE te lo aceptará perfectamente y se pintará de su color normal
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private FolioRepository folioRepository;

    @Override
    @Transactional
    public VentaDTO registrar(VentaRequestDTO dto) {

        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("No se puede registrar una venta sin líneas de detalle");
        }

        //Para validar si se repite un articulo en los detalles, los agrupamos primero
        Map<String, Integer> cantidadesAgrupadas = new HashMap<>();
        for (VentaDetalleRequestDTO dDto : dto.getDetalles()) {
            String codigo = dDto.getArticuloCodigo();
            cantidadesAgrupadas.put(codigo, cantidadesAgrupadas.getOrDefault(codigo, 0) + dDto.getCantidad());
        }


        Cliente cliente = clienteRepository.findByEmail(dto.getClienteEmail())
                .orElseThrow(() -> new IllegalArgumentException("El cliente con correo " + dto.getClienteEmail() + " no existe"));

        Venta venta = new Venta();
        venta.setCliente(cliente);

        BigDecimal totalVenta = BigDecimal.ZERO;

        for (Map.Entry<String, Integer> entrada : cantidadesAgrupadas.entrySet()) {
            String codigoArticulo = entrada.getKey();
            Integer cantidadTotal = entrada.getValue();

            // Buscar el artículo por su código único de negocio
            Articulo articulo = articuloRepository.findByCodigoAndActivoTrue(codigoArticulo)
                    .orElseThrow(() -> new IllegalArgumentException("El artículo con código " + codigoArticulo + " no existe o está inactivo"));

            // Validar Stock contra el total fusionado
            if (articulo.getStock() < cantidadTotal) {
                throw new IllegalArgumentException("Stock insuficiente para: " + articulo.getNombre()
                        + ". Disponibles: " + articulo.getStock() + ", Solicitados en total: " + cantidadTotal);
            }

            // C. Descontar el stock real de una sola vez en la base de datos
            articulo.setStock(articulo.getStock() - cantidadTotal);
            articuloRepository.save(articulo);

            // D. Calcular subtotales con precisión BigDecimal
            BigDecimal cantidadConvertida = new BigDecimal(cantidadTotal);
            BigDecimal subtotalLinea = articulo.getPrecio().multiply(cantidadConvertida);

            // E. Acumular al gran total de la factura
            totalVenta = totalVenta.add(subtotalLinea);

            // F. Crear una ÚNICA línea de detalle fusionada para MySQL
            VentaDetalle detalleEntidad = new VentaDetalle();
            detalleEntidad.setVenta(venta);
            detalleEntidad.setArticulo(articulo);
            detalleEntidad.setCantidad(cantidadTotal); // Guarda la suma total real
            detalleEntidad.setPrecioUnitario(articulo.getPrecio()); // Congela el precio del momento
            detalleEntidad.setSubtotal(subtotalLinea);

            // Agregamos la línea limpia al maestro
            venta.getDetalles().add(detalleEntidad);
        }

        venta.setTotal(totalVenta);
        Folio folio = folioRepository.findByNombre("Venta")
                .orElseThrow(() -> new IllegalArgumentException("Error interno al obtener el folio Venta"));
        venta.setFolio(folio.getFolio());
        folio.setFolio(folio.getFolio() + 1);
        folioRepository.save(folio);
        Venta ventaGuardada = ventaRepository.save(venta);
        return mapearADto(ventaGuardada);
    }



    @Override
    @Transactional(readOnly = true)
    public Page<VentaDTO> obtenerTodas(Pageable pageable) {
        Page<Venta> paginaVentas = ventaRepository.findAll(pageable);
        return paginaVentas.map(this::mapearADto);
    }

    @Override
    @Transactional(readOnly = true)
    public VentaDTO obtenerPorFolio(Long Folio) {
        Venta venta = ventaRepository.findByFolio(Folio)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la venta"));
        return mapearADto(venta);
    }

    @Override
    @Transactional
    public void cancelar(Long Folio) {
        Venta venta = ventaRepository.findByFolio(Folio)
                .orElseThrow(() -> new IllegalArgumentException("La venta con el folio "+Folio+" no existe"));

        if(venta.getEstado().equals("CANCELADO")){
            throw new IllegalArgumentException("La venta con el folio "+Folio+" ya estaba cancelada");
        }
        for(VentaDetalle detalle: venta.getDetalles()){
            Articulo articulo = detalle.getArticulo();
            //Regresa el Stock al articulo
            articulo.setStock(articulo.getStock() + detalle.getCantidad());
           //Guardar cambios de Stock
            articuloRepository.save(articulo);
        }

        venta.setEstado("CANCELADO");
        ventaRepository.save(venta);
    }

    private VentaDTO mapearADto(Venta venta) {
        VentaDTO dto = new VentaDTO();
        dto.setId(venta.getId());
        dto.setFolio(venta.getFolio());
        dto.setFecha(venta.getFecha());
        dto.setEstado(venta.getEstado());
        dto.setTotal(venta.getTotal());
        dto.setClienteEmail(venta.getCliente().getEmail());

        // Mapeamos también la lista interna de detalles usando Streams funcionales
        java.util.List<VentaDetalleDTO> listaDetallesDto = new java.util.ArrayList<>();
        for (VentaDetalle detalle : venta.getDetalles()) {
            VentaDetalleDTO dDto = new VentaDetalleDTO();
            dDto.setId(detalle.getId());
            dDto.setArticuloCodigo(detalle.getArticulo().getCodigo());
            dDto.setCantidad(detalle.getCantidad());
            dDto.setPrecioUnitario(detalle.getPrecioUnitario());
            dDto.setSubtotal(detalle.getSubtotal());
            listaDetallesDto.add(dDto);
        }
        dto.setDetalles(listaDetallesDto);
        return dto;
    }

}
