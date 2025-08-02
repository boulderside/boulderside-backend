package com.line7studio.boulderside.common.security.filter;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.common.security.exception.AuthenticationFailureException;
import com.line7studio.boulderside.common.security.exception.CustomEntryPoint;
import com.line7studio.boulderside.common.security.exception.SecurityErrorCode;
import com.line7studio.boulderside.common.security.provider.TokenProvider;
import com.line7studio.boulderside.common.security.vo.LoginRequest;
import com.line7studio.boulderside.common.security.vo.LoginResponse;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	private final TokenProvider tokenProvider;
	private final CustomEntryPoint customEntryPoint;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public LoginFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider,
		CustomEntryPoint customEntryPoint) {
		this.authenticationManager = authenticationManager;
		this.tokenProvider = tokenProvider;
		this.customEntryPoint = customEntryPoint;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		try {
			LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
			String id = loginRequest.id();
			String password = loginRequest.password();

			request.setAttribute("loginId", id);
			log.info("로그인 시도: ID={}", id);

			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				id, password, null);

			return authenticationManager.authenticate(authToken);

		} catch (IOException e) {
			throw new AuthenticationFailureException(SecurityErrorCode.AUTHENTICATION_PAYLOAD_INVALID);

		}

	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authentication) throws IOException {
		String id = (String)request.getAttribute("loginId");
		log.info("로그인 성공: ID={}", id);

		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();

		String accessToken = tokenProvider.create("access", userDetails.getUserId(), userDetails.getUserRole());
		String refreshToken = tokenProvider.create("refresh", userDetails.getUserId(), userDetails.getUserRole());

		LoginResponse loginResponse = new LoginResponse(userDetails.getEmail(), userDetails.getNickname(), accessToken,
			refreshToken);

		ApiResponse<LoginResponse> apiResponse = ApiResponse.of(loginResponse);
		String json = objectMapper.writeValueAsString(apiResponse);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpStatus.OK.value());
		response.getWriter().write(json);
		response.getWriter().flush();
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		org.springframework.security.core.AuthenticationException failed) throws IOException, ServletException {
		String id = (String)request.getAttribute("loginId");
		log.warn("로그인 실패: ID={}, 이유={}", id, failed.getMessage());

		customEntryPoint.commence(request, response, failed);
	}
}
