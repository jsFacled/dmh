package com.DigitalMoneyHouse.msvc_gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;


@Component
@Order(1)
public class JwtForwardingFilter implements GatewayFilter {

    private final WebClient webClient;

    public JwtForwardingFilter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // Extraer el token del encabezado Authorization
        String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            // Construir la URL de ms-uaM
            String msUaMUri = "http://localhost:8081/auth/validate"; // URL de ms-uaM para validar el token

            // Enviar la solicitud a ms-uaM y obtener la respuesta
            return webClient.get()
                    .uri(msUaMUri)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .toBodilessEntity()
                    .flatMap(responseEntity -> {
                        if (responseEntity.getStatusCode().is2xxSuccessful()) {
                            // El token es válido, permitir la solicitud hacia el siguiente microservicio
                            return chain.filter(exchange);
                        } else {
                            // El token no es válido, devolver un error 401 Unauthorized
                            response.setStatusCode(HttpStatus.UNAUTHORIZED);
                            return response.setComplete();
                        }
                    })
                    .onErrorResume(e -> {
                        // Manejo de errores
                        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                        return response.setComplete();
                    });
        } else {
            // No se encontró el token, devolver un error 401 Unauthorized
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
    }
}
