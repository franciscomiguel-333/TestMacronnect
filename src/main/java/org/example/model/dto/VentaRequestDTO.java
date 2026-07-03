package org.example.model.dto;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class VentaRequestDTO {

    @NotBlank
    @Email
    @NotNull
    private String clienteEmail;

    @NotEmpty
    @Valid
    private List<VentaDetalleRequestDTO> detalles;

    // Getters y Setters
    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }

    public List<VentaDetalleRequestDTO> getDetalles() { return detalles; }
    public void setDetalles(List<VentaDetalleRequestDTO> detalles) { this.detalles = detalles; }
}