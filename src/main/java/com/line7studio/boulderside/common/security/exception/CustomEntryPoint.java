package com.line7studio.boulderside.common.security.exception;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.line7studio.boulderside.common.response.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// TODO: JWT 인증을 제외한 다양한 상황에 대한 예외도 처리하기
@Component
public class CustomEntryPoint implements AuthenticationEntryPoint {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		SecurityErrorCode errorCode;
		if (authException instanceof AuthenticationFailureException authEx) {
			errorCode = authEx.getErrorCode();
		} else {
			errorCode = SecurityErrorCode.UNKNOWN_AUTHENTICATION_ERROR;
		}

		ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(),
			errorCode.getMessage());

		String jsonResponse = objectMapper.writeValueAsString(errorResponse);
		response.getWriter().write(jsonResponse);
		response.getWriter().flush();
	}
}
