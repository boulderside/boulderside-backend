package com.line7studio.boulderside.common.config;

import com.line7studio.boulderside.common.security.constants.SecurityWhitelist;
import com.line7studio.boulderside.common.security.exception.CustomDeniedHandler;
import com.line7studio.boulderside.common.security.exception.CustomEntryPoint;
import com.line7studio.boulderside.common.security.filter.JWTFilter;
import com.line7studio.boulderside.common.security.filter.LoginFilter;
import com.line7studio.boulderside.common.security.provider.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
	private final TokenProvider tokenProvider;
	private final AuthenticationConfiguration authenticationConfiguration;

	private final CustomDeniedHandler customDeniedHandler;
	private final CustomEntryPoint customEntryPoint;

	private final JWTFilter jwtFilter;

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
				.requestMatchers(SecurityWhitelist.SWAGGER).permitAll()
				.requestMatchers(SecurityWhitelist.PUBLIC).permitAll()
				.requestMatchers(SecurityWhitelist.ADMIN).hasRole("ADMIN")
				.anyRequest().authenticated());

		http.exceptionHandling(exception -> exception
			.authenticationEntryPoint(customEntryPoint)
			.accessDeniedHandler(customDeniedHandler));

		LoginFilter loginFilter = new LoginFilter(authenticationConfiguration.getAuthenticationManager(),
			tokenProvider, customEntryPoint);
		loginFilter.setFilterProcessesUrl("/users/login");
		http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true); // 자격 증명 포함 허용(Authorization 헤더, 쿠기 사용 가능)
		configuration.setAllowedOrigins(List.of("http://localhost:5500", "http://127.0.0.1:5500", "http://localhost:63342")); //허용할 프론트엔드 도메인
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); //허용 메소드
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setExposedHeaders(List.of("*"));

		// 위의 CORS 정책을 모든 경로에 적용
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}