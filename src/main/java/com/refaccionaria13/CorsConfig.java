package com.refaccionaria13; // OJO: Cambia esto por el nombre de tu paquete (ej. com.refaccionaria13.config)

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aplica a todas las rutas de tu API
                        .allowedOrigins("*") // Permite cualquier página web
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // Permite todos los métodos
                        .allowedHeaders("*");
            }
        };
    }
}
