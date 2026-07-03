package org.example.model.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


public class VentaDetalleDTO {

    private Long id;

    @NotBlank(message = "El código del artículo es obligatorio")
    private String articuloCodigo; // Recibe el código único de negocio

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima a comprar debe ser 1")
    private Integer cantidad;

    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public VentaDetalleDTO() {}

    // Getters y Setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getArticuloCodigo() { return articuloCodigo; }
    public void setArticuloCodigo(String articuloCodigo) { this.articuloCodigo = articuloCodigo; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
