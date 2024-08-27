package com.DigitalMoneyHouse.msvc_usersAccountsManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsvcUsersAccountsManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcUsersAccountsManagerApplication.class, args);

		System.out.println(" *// * // * // * // * // * // *  - - - - - - - - - -  Iniciando ms-Manager  - - - - - - - - - - - - - - - - - - - --*// * // * // * // * // * // *");
	}

}
