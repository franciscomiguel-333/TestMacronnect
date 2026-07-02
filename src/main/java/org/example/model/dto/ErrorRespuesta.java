package org.example.model.dto;

import java.util.Map;

public class ErrorRespuesta {
    private int codigo;
    private  String mensaje;
    private Map<String, String> detalles; //Fallo campo y detalles

    public ErrorRespuesta(int codigo, String mensaje){
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    public ErrorRespuesta(int codigo, String mensaje, Map<String, String> detalles){
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.detalles = detalles;
    }

    // Getters y Setters
    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public Map<String, String> getDetalles() { return detalles; }
    public void setDetalles(Map<String, String> detalles) { this.detalles = detalles; }
}
