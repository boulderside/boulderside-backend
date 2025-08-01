package com.example.boulderside.common.security.details;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.boulderside.domain.user.entity.User;
import com.example.boulderside.domain.user.enums.UserRole;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails {
	private final Long userId;
	private final String email;
	private final String password;
	private final String nickname;
	private final UserRole userRole;

	private CustomUserDetails(Long userId, String email, String password, String nickname, UserRole userRole) {
		this.userId = userId;
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.userRole = userRole;
	}

	public static CustomUserDetails from(User user) {
		return new CustomUserDetails(user.getId(), user.getEmail(), user.getPassword(), user.getNickname(),
			user.getUserRole());
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
