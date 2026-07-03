package org.example.service;

import org.example.model.dto.VentaDTO;
import org.example.model.dto.VentaRequestDTO;
import org.example.model.dto.VentaDetalleRequestDTO;
import org.example.model.entity.Articulo;
import org.example.model.entity.Cliente;
import org.example.model.entity.Venta;
import org.example.model.entity.Folio;
import org.example.repository.ArticuloRepository;
import org.example.repository.ClienteRepository;
import org.example.repository.FolioRepository;
import org.example.repository.VentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VentaServiceTest {

    @InjectMocks
    private VentaServiceImpl ventaService;

    @Mock private VentaRepository ventaRepository;
    @Mock private ClienteRepository clienteRepository;
    @Mock private ArticuloRepository articuloRepository;
    @Mock private FolioRepository folioRepository;

    private Cliente clientePrueba;
    private Articulo articuloPrueba;
    private Folio folioPrueba;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        clientePrueba = new Cliente();
        clientePrueba.setEmail("francisco@protonmail.com");

        articuloPrueba = new Articulo();
        articuloPrueba.setCodigo("01");
        articuloPrueba.setNombre("Laptop");
        articuloPrueba.setPrecio(new BigDecimal("100.00"));
        articuloPrueba.setStock(5);
        articuloPrueba.setActivo(true);

        folioPrueba = new Folio();
        folioPrueba.setNombre("Venta");
        folioPrueba.setFolio(1L);
    }

    @Test
    void cuandoRegistrarVenta_entoncesCalcularTotalCorrecto() {
        VentaRequestDTO request = new VentaRequestDTO();
        request.setClienteEmail("francisco@protonmail.com");
        request.setDetalles(new ArrayList<>());

        VentaDetalleRequestDTO detalleRequest = new VentaDetalleRequestDTO();
        detalleRequest.setArticuloCodigo("01");
        detalleRequest.setCantidad(2);
        request.getDetalles().add(detalleRequest);

        when(clienteRepository.findByEmail(anyString())).thenReturn(Optional.of(clientePrueba));
        when(articuloRepository.findByCodigoAndActivoTrue("01")).thenReturn(Optional.of(articuloPrueba));
        when(folioRepository.findByNombre("Venta")).thenReturn(Optional.of(folioPrueba));
        when(ventaRepository.save(any(Venta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VentaDTO resultado = ventaService.registrar(request);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("200.00"), resultado.getTotal());
        assertEquals(3, articuloPrueba.getStock());
    }

    @Test
    void cuandoRegistrarVentaConStockInsuficiente_entoncesLanzarExcepcion() {
        VentaRequestDTO request = new VentaRequestDTO();
        request.setClienteEmail("francisco@protonmail.com");
        request.setDetalles(new ArrayList<>());

        VentaDetalleRequestDTO detalleRequest = new VentaDetalleRequestDTO();
        detalleRequest.setArticuloCodigo("01");
        detalleRequest.setCantidad(10);
        request.getDetalles().add(detalleRequest);

        when(clienteRepository.findByEmail(anyString())).thenReturn(Optional.of(clientePrueba));
        when(articuloRepository.findByCodigoAndActivoTrue("01")).thenReturn(Optional.of(articuloPrueba));
        when(folioRepository.findByNombre("Venta")).thenReturn(Optional.of(folioPrueba));

        Exception excepcion = assertThrows(IllegalArgumentException.class, () -> {
            ventaService.registrar(request);
        });

        assertTrue(excepcion.getMessage().contains("Stock insuficiente"));
    }
}