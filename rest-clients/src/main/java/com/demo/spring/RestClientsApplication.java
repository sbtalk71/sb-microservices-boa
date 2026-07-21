package com.demo.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.Builder;

@SpringBootApplication
public class RestClientsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestClientsApplication.class, args);
	}

	
}
