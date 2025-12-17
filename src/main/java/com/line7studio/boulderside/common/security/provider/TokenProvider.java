package com.line7studio.boulderside.common.security.provider;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.line7studio.boulderside.domain.user.enums.UserRole;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;

@Service
public class TokenProvider {
	@Value("${jwt.secret}")
	private String SECRET_KEY;
	private final long TOKEN_EXPIRATION_MS = 864000000L; // 10일

	private SecretKey secretKey;

	@PostConstruct
	public void init() {
		this.secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS256.key().build().getAlgorithm());
	}

	public String create(String category, Long userId, UserRole role) {
		return Jwts.builder()
			.claim("category", category) // access or refresh
			.claim("userId", userId)
			.claim("role", role)
			.issuedAt(new Date(System.currentTimeMillis())) // 발급 시간
			.expiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_MS)) //토큰 만료시간 설정
			.signWith(secretKey)
			.compact();
	}

	public String getCategory(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("category", String.class);
	}

	public Long getUserId(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("userId", Long.class);
	}

	public UserRole getRole(String token) {
		return UserRole.valueOf(
			Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.get("role", String.class)
		);
	}

	public Boolean isExpired(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.getExpiration()
			.before(new Date());
	}
}
