package org.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter  {

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extrae encabezado de autorizacion
        String headerAuthorization = request.getHeader("Authorization");

        String token = null;
        String email = null;

        // Valida y comprueba el formato del Bearer
        if (headerAuthorization != null && headerAuthorization.startsWith("Bearer ")) {
            token = headerAuthorization.substring(7); // Recorta la palabra "Bearer " para quedarse solo con el token
            if (jwtProvider.validarToken(token)) {
                email = jwtProvider.obtenerEmailDelToken(token); // Descifra el email guardado
            }
        }

        // Si encontramos un email válido y el usuario no está autenticado en la sesión actual
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Creamos un objeto de usuario plano de Spring Security
            UserDetails userDetails = new User(email, "", new ArrayList<>());

            // Fabricamos la credencial de acceso oficial de Spring
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 🔥 LE DAMOS EL ACCESO INMEDIATO: Inyectamos la autenticación en el contexto seguro de Spring Boot
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // Continuar el viaje hacia el Controller correspondiente
        filterChain.doFilter(request, response);
    }
}
