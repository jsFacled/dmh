package com.DigitalMoneyHouse.msvc_configServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableConfigServer
public class MsvcConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcConfigServerApplication.class, args);
	}


	// RestController para probar el Config Server
	@RestController
	public class ConfigServerController {

		@GetMapping("/")
		public String hello() {
			return "Hola, soy config server";
		}
	}
}
