package com.line7studio.boulderside.common.config;

import com.line7studio.boulderside.common.security.constants.SecurityWhitelist;
import com.line7studio.boulderside.common.security.exception.CustomDeniedHandler;
import com.line7studio.boulderside.common.security.exception.CustomEntryPoint;
import com.line7studio.boulderside.common.security.filter.JWTFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final CustomDeniedHandler customDeniedHandler;
	private final CustomEntryPoint customEntryPoint;

	private final JWTFilter jwtFilter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// CSRF 비활성화
		http.csrf(AbstractHttpConfigurer::disable)
			// CORS 설정 적용
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			// Form 로그인 비활성화
			.formLogin(AbstractHttpConfigurer::disable)
			// HTTP Basic 인증 비활성화
			.httpBasic(AbstractHttpConfigurer::disable)
			// 세션 비활성화 (JWT 방식 사용 대비)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			// 명시한 url만 인가 없이 허용
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(SecurityWhitelist.SWAGGER).permitAll()
				.requestMatchers(SecurityWhitelist.PUBLIC).permitAll()
                    .requestMatchers(SecurityWhitelist.ADMIN_PUBLIC).permitAll()
				.requestMatchers(SecurityWhitelist.ADMIN).hasRole("ADMIN")
				.anyRequest().authenticated());

		http.exceptionHandling(exception -> exception
			.authenticationEntryPoint(customEntryPoint)
			.accessDeniedHandler(customDeniedHandler));

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true); // 자격 증명 포함 허용(Authorization 헤더, 쿠기 사용 가능)
		configuration.setAllowedOriginPatterns(List.of("*")); // 모든 origin 허용 (개발 환경)
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); //허용 메소드
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setExposedHeaders(List.of("*"));

		// 위의 CORS 정책을 모든 경로에 적용
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
