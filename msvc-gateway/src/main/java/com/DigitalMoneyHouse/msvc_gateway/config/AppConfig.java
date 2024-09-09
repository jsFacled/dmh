package com.DigitalMoneyHouse.msvc_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.http.HttpClient;

@Configuration
public class AppConfig {


    /*
    * WebClient.Builder: El propósito de este bean es configurar y construir instancias de WebClient,
    * que es un cliente reactivo utilizado para hacer llamadas HTTP a otros servicios en aplicaciones Spring.
    * */

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}