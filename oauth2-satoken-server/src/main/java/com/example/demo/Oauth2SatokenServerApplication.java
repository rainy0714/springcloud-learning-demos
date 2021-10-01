package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Oauth2SatokenServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(Oauth2SatokenServerApplication.class, args);
		System.out.println("\nSa-Token-OAuth Server端启动成功");
	}

}
