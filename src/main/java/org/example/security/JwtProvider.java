package org.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    // Clave secreta en texto plano para firmar digitalmente tus tokens
    private final String CLAVE_SECRETA = "EsafdsrEWrwRwerwersdfsdf";

    private final long TIEMPO_EXPIRACION = 86400000; // 24 horas

    public String generarToken(String username) {
        Date ahora = new Date();
        Date fechaExpiracion = new Date(ahora.getTime() + TIEMPO_EXPIRACION);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(ahora)
                .setExpiration(fechaExpiracion)
                .signWith(SignatureAlgorithm.HS256, CLAVE_SECRETA) // Sintaxis nativa y portable en memoria
                .compact();
    }

    public String obtenerEmailDelToken(String token) {
        Claims reclamos = Jwts.parser()
                .setSigningKey(CLAVE_SECRETA)
                .parseClaimsJws(token)
                .getBody();

        return reclamos.getSubject();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parser().setSigningKey(CLAVE_SECRETA).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Atrapa tokens manipulados, falsos o expirados de forma segura
            return false;
        }
    }
}
