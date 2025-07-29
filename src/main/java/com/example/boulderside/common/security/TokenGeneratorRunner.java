package com.example.boulderside.common.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.boulderside.domain.user.enums.UserRole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenGeneratorRunner implements CommandLineRunner {
	private final TokenProvider tokenProvider;

	@Override
	public void run(String... args) {
		String token = tokenProvider.create("access", 1L, UserRole.ROLE_USER);
		log.info("--------------------------");
		log.info("Access Token : Bearer {}", token);
		log.info("--------------------------");
	}
}
