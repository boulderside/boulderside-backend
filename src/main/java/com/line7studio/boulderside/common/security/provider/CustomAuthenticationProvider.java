package com.line7studio.boulderside.common.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.line7studio.boulderside.common.security.exception.AuthenticationFailureException;
import com.line7studio.boulderside.common.security.exception.SecurityErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String id = authentication.getName();
		String password = authentication.getCredentials().toString();

		UserDetails userDetails = userDetailsService.loadUserByUsername(id);

		if (!password.equals(userDetails.getPassword())) {
			throw new AuthenticationFailureException(SecurityErrorCode.INVALID_PASSWORD);
		}

		//  추후에 프론트 화면이 만들어지면 교체하기
		// if (!passwordEncoder.matches(password, userDetails.getPassword())) {
		// 	throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
		// }

		return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
	}

	// Authentication 객체를 처리할 수 있는지 판단하는 기준. false 반환 시 Security는 해당 Provider 호출 X
	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
