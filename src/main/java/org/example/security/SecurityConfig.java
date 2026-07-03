package org.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private JwtFilter jwtFilter; // Inyectamos al guardián que programamos

    // 1. REGISTRO TEMPORAL DE USUARIO EN MEMORIA (Para poder loguearnos antes de amarrar la tabla de usuarios)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin@sistema.com")
                .password(passwordEncoder().encode("admin123")) // Guarda la clave encriptada con BCrypt
                .roles("ADMIN");
    }

    // EXPORTAR EL MOTOR DE AUTENTICACIÓN
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // ENCRIPTADOR DE CONTRASEÑAS (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // EL CAMBIO DE ARQUITECTURA (De Stateful a Stateless)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // Apagamos CSRF porque los tokens JWT son inmunes a este tipo de ataques
                .csrf().disable()

                // AQUÍ SE DESTRUYE EL FORMULARIO: Apagamos el manejo de sesiones en memoria del servidor
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()

                // RUTAS PÚBLICAS
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                .antMatchers("/api/auth/**").permitAll() // <-- Ruta para loguearse y pedir el token

                // RUTAS PROTEGIDAS
                .antMatchers("/api/clientes/**").authenticated()
                .antMatchers("/api/articulos/**").authenticated()
                .antMatchers("/api/ventas/**").authenticated()

                .anyRequest().authenticated();

        // Inyectamos nuestro filtro justo antes del validador por defecto de Spring
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
