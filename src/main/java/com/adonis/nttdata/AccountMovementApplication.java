package com.adonis.nttdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AccountMovementApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountMovementApplication.class, args);
	}

}
