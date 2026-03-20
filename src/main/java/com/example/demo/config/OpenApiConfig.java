package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI().info(new Info()
				.title("Users REST API")
				.version("1.0")
				.description("User management with sorting, filtering, AES-256 password storage, and RFC / AndresFormat validation."));
	}

}
