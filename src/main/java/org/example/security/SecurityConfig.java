package org.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 1. Apagamos CSRF porque usaremos JWT más adelante
                .csrf().disable()

                // 2. Autorizamos las rutas en la API
                .authorizeRequests()

                // Permitimos acceso total a todo lo relacionado con Swagger para poder probar
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()

                // Cualquier otra petición (como /api/clientes) requerirá autenticación por ahora
                .anyRequest().authenticated()

                .and()
                // Mantenemos el formulario de login básico temporalmente para las pruebas
                .formLogin().and()
                .httpBasic();
    }
}
