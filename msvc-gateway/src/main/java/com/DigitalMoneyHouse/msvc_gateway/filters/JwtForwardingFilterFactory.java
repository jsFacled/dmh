package com.DigitalMoneyHouse.msvc_gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


/*
* La clase extiende AbstractGatewayFilterFactory de Spring Cloud Gateway
* y se utiliza para crear un filtro personalizado que reenvía tokens JWT (JSON Web Token)
* a otros microservicios en una arquitectura de gateway.
*
* */

@Component
public class JwtForwardingFilterFactory extends AbstractGatewayFilterFactory<JwtForwardingFilterFactory.Config> {

    //Construye un cliente web reactivo
    private final WebClient.Builder webClient;

    public JwtForwardingFilterFactory(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClient = webClientBuilder;
    }

    public static class Config {
        // Configuración adicional del filtro para cuando sea necesario
    }

    /*
        El objetivo es devolver un objeto de tipo GatewayFilter, que contiene la lógica personalizada
        que se aplicará cuando una solicitud pase a través del gateway.

    * El método apply() simplemente crea y devuelve un filtro personalizado (JwtForwardingFilter)
      que utiliza WebClient para manejar las solicitudes HTTP.
      Este filtro se ejecutará cada vez que una solicitud pase por el gateway y
      tenga que realizar alguna operación con el JWT, como reenviarlo o validarlo.
    * */
    @Override
    public GatewayFilter apply(Config config) {
        return new JwtForwardingFilter(webClient); //devuelve instancia del filtro que implementa la lógica de validación del jwt
    }
}
