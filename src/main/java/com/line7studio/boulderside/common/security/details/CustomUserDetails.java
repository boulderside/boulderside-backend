package com.line7studio.boulderside.common.security.details;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.line7studio.boulderside.domain.feature.user.entity.User;
import com.line7studio.boulderside.domain.feature.user.enums.UserRole;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record CustomUserDetails(Long userId, String nickname, String profileImageUrl,
                                UserRole userRole) implements UserDetails {
    public static CustomUserDetails from(User user) {
        return CustomUserDetails.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .userRole(user.getUserRole())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return String.valueOf(userId);
    }

}
