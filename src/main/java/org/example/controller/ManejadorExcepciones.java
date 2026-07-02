package org.example.controller;

import org.example.model.dto.ErrorRespuesta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // Escucha a todos los controladores globales
public class ManejadorExcepciones {
    // Atrapa los errores deBean

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRespuesta> manejarValidaciones(MethodArgumentNotValidException ex) {
        Map<String, String> erroresCampos = new HashMap<>();

        // Mapeamos campos y mensajes personalizados
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            erroresCampos.put(error.getField(), error.getDefaultMessage());
        }

        ErrorRespuesta respuesta = new ErrorRespuesta(
                HttpStatus.BAD_REQUEST.value(),
                "Error en la validación de los datos de entrada",
                erroresCampos
        );

        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
    }

    // Atrapa los errores de lógica de negocio (como el correo/teléfono duplicado del Service)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorRespuesta> manejarErroresNegocio(IllegalArgumentException ex) {
        ErrorRespuesta respuesta = new ErrorRespuesta(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST); // HTTP 400
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuesta> manejarErroresImprevistos(Exception ex) {

        ErrorRespuesta respuesta = new ErrorRespuesta(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ha ocurrido un error interno en el servidor. Por favor, contacte al administrador del sistema."
        );

        return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR); // HTTP 500 Internal Server Error
    }
}