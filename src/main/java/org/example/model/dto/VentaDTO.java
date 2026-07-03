package org.example.model.dto;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class VentaDTO {

    private Long id;
    private Long folio;
    private LocalDateTime fecha;
    private String estado;
    private BigDecimal total;

    @NotBlank(message = "El correo del cliente es obligatorio")
    @Email(message = "El formato del correo del cliente no es válido")
    private String clienteEmail; // Identificador de negocio del cliente

    @NotEmpty(message = "La venta debe incluir al menos un artículo en el detalle")
    @Valid // Activa las validaciones dentro de los detalles
    private List<VentaDetalleDTO> detalles;

    public VentaDTO() {}

    // Getters y Setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getFolio() { return folio; }
    public void setFolio(Long folio) { this.folio = folio; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }
    public List<VentaDetalleDTO> getDetalles() { return detalles; }
    public void setDetalles(List<VentaDetalleDTO> detalles) { this.detalles = detalles; }
}
