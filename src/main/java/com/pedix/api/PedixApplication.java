package com.pedix.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.pedix.api")
public class PedixApplication {

	public static void main(String[] args) {
		SpringApplication.run(PedixApplication.class, args);
		System.out.println(" Pedix API iniciada com sucesso!");
	}
}
