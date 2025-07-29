package com.example.boulderside.common.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.boulderside.common.security.TokenProvider;
import com.example.boulderside.common.security.exception.CustomDeniedHandler;
import com.example.boulderside.common.security.exception.CustomEntryPoint;
import com.example.boulderside.common.security.filter.JWTFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final TokenProvider tokenProvider;

	private final CustomDeniedHandler customDeniedHandler;
	private final CustomEntryPoint customEntryPoint;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// CSRF 비활성화
		http.csrf(auth -> auth.disable())
			// CORS 설정 적용
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			// Form 로그인 비활성화
			.formLogin(auth -> auth.disable())
			// HTTP Basic 인증 비활성화
			.httpBasic(auth -> auth.disable())
			// 세션 비활성화 (JWT 방식 사용 대비)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			// 명시한 url만 인가 없이 허용
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/").permitAll()
				.anyRequest().authenticated());

		http.exceptionHandling(exception -> exception
			.authenticationEntryPoint(customEntryPoint)
			.accessDeniedHandler(customDeniedHandler));

		//http.addFilterBefore(new JWTFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(new JWTFilter(tokenProvider, customEntryPoint), ExceptionTranslationFilter.class);
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true); // 자격 증명 포함 허용(Authorization 헤더, 쿠기 사용 가능)
		configuration.setAllowedOrigins(List.of("http://localhost:3000")); //허용할 프론트엔드 도메인
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); //허용 메소드
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setExposedHeaders(List.of("*"));

		// 위의 CORS 정책을 모든 경로에 적용
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
