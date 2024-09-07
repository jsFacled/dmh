package com.DigitalMoneyHouse.msvc_gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class JwtForwardingFilterFactory extends AbstractGatewayFilterFactory<JwtForwardingFilterFactory.Config> {

    private final WebClient.Builder webClient;

    public JwtForwardingFilterFactory(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClient = webClientBuilder;
    }

    public static class Config {
        // Configuración adicional si es necesaria
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new JwtForwardingFilter(webClient);
    }
}
