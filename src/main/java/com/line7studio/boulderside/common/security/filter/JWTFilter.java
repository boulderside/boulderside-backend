package com.line7studio.boulderside.common.security.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.common.security.exception.CustomEntryPoint;
import com.line7studio.boulderside.common.security.provider.TokenProvider;
import com.line7studio.boulderside.domain.aggregate.user.entity.User;
import com.line7studio.boulderside.domain.aggregate.user.enums.UserRole;
import com.line7studio.boulderside.domain.aggregate.user.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
	private final TokenProvider tokenProvider;
	private final CustomEntryPoint customEntryPoint;
	private final UserRepository userRepository;

	// OPTIONS는 CORS의 요청이기 때문에 굳이 JWT 인청 필터를 거칠 필요가 없음.
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		if (request.getMethod().equals("OPTIONS")) {
			return true; //필터 적용 x
		}
		return false; // 필터 적용 o
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String authorizationHeader = request.getHeader("Authorization");
		// 헤더에 토큰이 없는 경우에도 필터 통과하기
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			String token = authorizationHeader.substring(7);
			boolean isExpired = tokenProvider.isExpired(token);
			if (isExpired) {
				//TODO: 토큰이 만료된 경우로 RefreshToken을 요청 로직 추가하기
				return;
			}

			Long userId = tokenProvider.getUserId(token);
			User user = userRepository.findById(userId).orElseThrow();
			CustomUserDetails customUserDetails = CustomUserDetails.from(user);

			UserRole role = tokenProvider.getRole(token);
			List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role.name()));
			AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				customUserDetails,
				null,
				authorities
			);

			// IP, 세션 ID 등 부가 정보 저장(추후 사용을 위해)
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
			securityContext.setAuthentication(authentication);
			SecurityContextHolder.setContext(securityContext);

		} catch (Exception e) {

		}

		filterChain.doFilter(request, response);
	}
}