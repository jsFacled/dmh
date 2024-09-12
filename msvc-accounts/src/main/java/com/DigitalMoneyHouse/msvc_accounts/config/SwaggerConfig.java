package com.DigitalMoneyHouse.msvc_accounts.config;


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
                        .title("msvc-accounts")
                        .description("Manejará todo lo relacionado con las cuentas de los usuarios, incluyendo el saldo, las transacciones y la actividad de la cuenta.")
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
