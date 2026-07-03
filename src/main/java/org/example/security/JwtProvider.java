package org.example.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component // Registra esta clase utilitaria en el contenedor de Spring Boot
public class JwtProvider {

    // firma digital secreta ultra segura de 256 bits (HS256)
    private final Key CLAVE_SECRETA = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Tiempo de vida del token: 24 horas
    private final long TIEMPO_EXPIRACION = 86400000;

    //Genera token al inniciar sesion
    public String generarToken(String email) {
        Date ahora = new Date();
        Date fechaExpiracion = new Date(ahora.getTime() + TIEMPO_EXPIRACION);

        return Jwts.builder()
                .setSubject(email)                 // Guardamos el email del usuario adentro del token
                .setIssuedAt(ahora)                // Fecha de emisión
                .setExpiration(fechaExpiracion)    // Fecha de caducidad (24 horas)
                .signWith(CLAVE_SECRETA)           // Firmamos con nuestra llave digital secreta
                .compact();                        // Comprime todo en un String plano largo
    }

    /**
     * 2. MÉTODO PARA EXTRAER EL EMAIL (Se ejecuta cuando el cliente manda un token en sus peticiones)
     */
    public String obtenerEmailDelToken(String token) {
        Claims reclamos = Jwts.parserBuilder()
                .setSigningKey(CLAVE_SECRETA)      // Pasamos la firma para poder descifrarlo
                .build()
                .parseClaimsJws(token)
                .getBody();

        return reclamos.getSubject();              // Retorna el email que estaba guardado
    }

    /**
     * 3. MÉTODO PARA VALIDAR EL ESTADO DEL TOKEN (Verifica que sea auténtico y vigente)
     */
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(CLAVE_SECRETA)
                    .build()
                    .parseClaimsJws(token);            // Si logra parsearlo sin tronar, el token es 100% legítimo
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Atrapa tokens corruptos, falsificados, manipulados o ya expirados
            return false;
        }
    }
}
