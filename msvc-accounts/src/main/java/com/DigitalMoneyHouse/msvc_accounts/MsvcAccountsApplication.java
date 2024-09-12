package com.DigitalMoneyHouse.msvc_accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsvcAccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcAccountsApplication.class, args);
		System.out.println();
		System.out.println(" // * * // * * // * * // * * // * * // * *  -  -  -  -  -  -  -  Iniciando ms-Accounts  -  -  -  -  -  -  -   * * // * * // * * // * * // * * // * * // * * ");
		System.out.println();
	}

}
