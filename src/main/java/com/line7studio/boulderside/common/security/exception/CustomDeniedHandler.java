package com.line7studio.boulderside.common.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.line7studio.boulderside.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomDeniedHandler implements AccessDeniedHandler {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException {
		SecurityErrorCode errorCode = SecurityErrorCode.ACCESS_DENIED;
		log.warn("AccessDeniedException: uri={}, message={}", request.getRequestURI(),
			accessDeniedException.getMessage());

		response.setStatus(errorCode.getHttpStatus().value());
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage());
		objectMapper.writeValue(response.getWriter(), errorResponse);
		response.getWriter().flush();
	}
}
