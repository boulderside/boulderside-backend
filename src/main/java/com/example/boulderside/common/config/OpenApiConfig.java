package com.example.boulderside.common.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI openApi() {
		return new OpenAPI()
			.components(new Components()
				.addSecuritySchemes("Authorization", new SecurityScheme()
					.type(SecurityScheme.Type.HTTP)
					.scheme("bearer")
					.bearerFormat("JWT")
					.in(SecurityScheme.In.HEADER)
					.name("Authorization")
					.description("JWT token for API authentication")
				)
			)
			.security(List.of(new SecurityRequirement().addList("Authorization")))
			.info(new Info()
				.title("Boulderside API")
				.description("REST API for Boulderside - Boulder climbing location management system")
				.version("1.0.0")
				.contact(new Contact()
					.name("Boulderside Team")
					.email("support@boulderside.com"))
			);
	}
}
