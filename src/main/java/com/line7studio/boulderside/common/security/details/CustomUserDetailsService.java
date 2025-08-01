package com.line7studio.boulderside.common.security.details;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.line7studio.boulderside.common.security.exception.AuthenticationFailureException;
import com.line7studio.boulderside.common.security.exception.SecurityErrorCode;
import com.line7studio.boulderside.domain.aggregate.user.entity.User;
import com.line7studio.boulderside.domain.aggregate.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/* 해당 Service를 직접 호출하는 구조.
   UserDetailsService를 상속하지 않아도 문제는 없지만, 커스텀 인증 규약도 Security 관례를 따름. (+ OAuth2등에서 확장성)
   Spring Security에서는 사용자 정보를 가져올 때 UserDetailsService를 구현한 클래스를 기본 전략
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username)
			.orElseThrow(() -> new AuthenticationFailureException(SecurityErrorCode.INVALID_USERNAME));

		// TODO: 휴먼 사용자 및 추가 인증이 필요할 경우 해당 부분에 로직 추가

		return CustomUserDetails.from(user);
	}
}
