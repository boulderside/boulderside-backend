package com.line7studio.boulderside.controller.user.response;

import java.time.LocalDateTime;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.feature.user.entity.User;
import com.line7studio.boulderside.domain.feature.user.enums.UserRole;
import com.line7studio.boulderside.domain.feature.user.enums.UserSex;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserResponse {
	private Long userId;
	private String nickname;
	private String name;
	private String email;
	private String phone;
	private UserRole userRole;
	private UserSex userSex;
	private Level userLevel;
	private String profileImageUrl;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static AdminUserResponse from(User user) {
		return AdminUserResponse.builder()
			.userId(user.getId())
			.nickname(user.getNickname())
			.name(user.getName())
			.email(user.getEmail())
			.phone(user.getPhone())
			.userRole(user.getUserRole())
			.userSex(user.getUserSex())
			.userLevel(user.getUserLevel())
			.profileImageUrl(user.getProfileImageUrl())
			.createdAt(user.getCreatedAt())
			.updatedAt(user.getUpdatedAt())
			.build();
	}
}
