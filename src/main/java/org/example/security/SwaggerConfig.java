package org.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30) // Formato OpenAPI 3 estándar
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.example.controller")) // Escanea tus controladores
                .paths(PathSelectors.any())
                .build();
    }
}
