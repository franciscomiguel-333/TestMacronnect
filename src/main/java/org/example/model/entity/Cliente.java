package org.example.model.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;



@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 500, message = "El nombre no puede superar los 500 caracteres")
    @Column( nullable = false, length = 500)
    private String nombre;


    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo no es válido")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
            message = "El correo debe incluir una extensión válida (ejemplo: .com, .net)")
    @Size(max = 200, message = "El correo no puede superar los 200 caracteres")
    @Column(unique = true, nullable = false, length = 200)
    private String email;



    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\d{10}$", message = "El teléfono debe tener exactamente 10 dígitos")
    @Column(unique = true, nullable = false, length = 10)
    private String telefono;


    @NotBlank(message = "La dirección es obligatorio")
    @Size(max = 1000, message = "La dirección no puede superar los 1000 caracteres")
    @Column(nullable = false, length = 1000)
    private String direccion;


    @Column(nullable = false)
    private boolean activo = true; // En el Entity (por defecto true)



    public Cliente(){}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }


}
