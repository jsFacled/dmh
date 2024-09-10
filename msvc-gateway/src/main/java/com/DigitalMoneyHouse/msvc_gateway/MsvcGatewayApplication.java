package com.DigitalMoneyHouse.msvc_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/*
import org.springframework.context.annotation.Bean;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
*/

@SpringBootApplication
@EnableDiscoveryClient
public class MsvcGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcGatewayApplication.class, args);
	}
/*
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("accounts_route", r -> r.path("/accounts/**")
						.uri("lb://MSVC-ACCOUNTS"))
				.route("users_route", r -> r.path("/users/**")
						.uri("lb://MSVC-USERS"))
				.route("usersAccountsManager_route", r -> r.path("/manager/**")
						.uri("lb://MSVC-USERSACCOUNTSMANAGER"))
				.build();
	}
*/
}

