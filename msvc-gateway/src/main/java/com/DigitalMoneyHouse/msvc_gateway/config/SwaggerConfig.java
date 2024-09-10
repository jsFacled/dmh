package com.DigitalMoneyHouse.msvc_gateway.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("msvc-gateway")
                        .description("Este microservicio actuará como el punto de entrada para todas las solicitudes del frontend. Realizará la autorización y autenticación al recibir JWT")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Facundo Ledezma")
                                .email("facled@yahoo.com.ar")
                                .url("https://www.digitalhouse.com"))
                        .license(new License()
                                .name("Licencia API-Fac")
                                .url("https://www.digitalhouse.com")));
    }
}
