package com.DigitalMoneyHouse.msvc_gateway.filters;

import io.jsonwebtoken.Jwts;
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
import io.jsonwebtoken.Claims;


/*
* El filtro JwtForwardingFilter intercepta las solicitudes en el gateway,
* extrae el token JWT del encabezado Authorization, y lo envía a un servicio de validación.
* Si el token es válido, la solicitud continúa; si no, se devuelve un error 401 Unauthorized.
* Maneja errores con una respuesta 500 Internal Server Error.
* */


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



/*
//Con validación de userId o accountId
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

        // Verificamos si el token es válido y llamamos al método modularizado para obtener el email
        if (isValidAuthorizationHeader(authorizationHeader)) {
            String token = extractTokenFromHeader(authorizationHeader);

            // Validar el token y continuar con la lógica de autorización
            return processTokenValidation(token, request, response, chain, exchange);
        } else {
            return handleUnauthorized(response);
        }
    }

    // Método para validar el encabezado Authorization
    private boolean isValidAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    // Método para extraer el token del encabezado Authorization
    private String extractTokenFromHeader(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }

    // Método para manejar la validación del token y la autorización
    private Mono<Void> processTokenValidation(String token, ServerHttpRequest request, ServerHttpResponse response, GatewayFilterChain chain, ServerWebExchange exchange) {
        return extractEmailFromToken(token).flatMap(emailFromToken -> {
            String accountIdFromRequest = extractAccountIdFromPath(request.getURI().getPath());

            if (emailFromToken != null && accountIdFromRequest != null) {
                return authorizeRequest(emailFromToken, accountIdFromRequest, chain, exchange, response);
            } else {
                return handleForbidden(response);
            }
        });
    }

    // Método para extraer accountId de la ruta
    private String extractAccountIdFromPath(String path) {
        String[] pathSegments = path.split("/");
        return pathSegments.length > 2 ? pathSegments[2] : null;
    }

    // Método para autorizar la solicitud
    private Mono<Void> authorizeRequest(String emailFromToken, String accountIdFromRequest, GatewayFilterChain chain, ServerWebExchange exchange, ServerHttpResponse response) {
        return getUserIdFromEmail(emailFromToken)
                .flatMap(userId -> isAuthorizedForAccount(userId, Long.parseLong(accountIdFromRequest)))
                .flatMap(isAuthorized -> {
                    if (isAuthorized) {
                        return chain.filter(exchange);
                    } else {
                        return handleForbidden(response);
                    }
                });
    }

    // Manejo de errores 401 (Unauthorized)
    private Mono<Void> handleUnauthorized(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    // Manejo de errores 403 (Forbidden)
    private Mono<Void> handleForbidden(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    // Otros métodos
    private Mono<String> extractEmailFromToken(String token) {
        String authServiceUrl = "http://localhost:8081/auth/validateToken";
        return webClient.get()
                .uri(authServiceUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.empty());
    }

    private Mono<Long> getUserIdFromEmail(String email) {
        String userServiceUrl = "http://localhost:8082/users/userIdFromEmail?email=" + email;
        return webClient.get()
                .uri(userServiceUrl)
                .retrieve()
                .bodyToMono(Long.class)
                .onErrorResume(e -> Mono.empty());
    }

    private Mono<Boolean> isAuthorizedForAccount(Long userId, Long accountId) {
        String managerServiceUrl = "http://localhost:8082/manager/validate/" + accountId + "/" + userId;
        return webClient.get()
                .uri(managerServiceUrl)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(e -> Mono.just(false));
    }
}
*/


