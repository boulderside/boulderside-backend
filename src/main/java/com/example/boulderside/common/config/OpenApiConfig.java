package com.example.boulderside.common.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
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
					.scheme("bearer")                             // bearer 명시
					.bearerFormat("JWT")                          // JWT 포맷 명시 (Swagger 문서 설명용)
					.in(SecurityScheme.In.HEADER)                 // HEADER 그대로 유지
					.name("Authorization")                        // Authorization 헤더
				)
			)
			.security(List.of(new SecurityRequirement().addList("Authorization")))
			.info(new Info()
				.title("Boulderside API Document")
				.version("v1.0.0")
			);
	}
}
