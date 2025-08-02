package com.line7studio.boulderside.common.security.details;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.line7studio.boulderside.domain.aggregate.user.entity.User;
import com.line7studio.boulderside.domain.aggregate.user.enums.UserRole;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CustomUserDetails implements UserDetails {
	private final Long userId;
	private final String email;
	private final String password;
	private final String nickname;
	private final UserRole userRole;

	public static CustomUserDetails from(User user) {
		return CustomUserDetails.builder()
			.userId(user.getId())
			.email(user.getEmail())
			.password(user.getPassword())
			.nickname(user.getNickname())
			.userRole(user.getUserRole())
			.build();
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(userRole.name()));
	}

}
