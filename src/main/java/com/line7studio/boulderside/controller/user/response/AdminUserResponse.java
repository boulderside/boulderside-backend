package com.line7studio.boulderside.controller.user.response;

import java.time.LocalDateTime;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.feature.user.User;
import com.line7studio.boulderside.domain.feature.user.enums.UserRole;
import com.line7studio.boulderside.domain.feature.user.enums.UserSex;

public record AdminUserResponse(
    Long userId,
    String nickname,
    String name,
    String email,
    String phone,
    UserRole userRole,
    UserSex userSex,
    Level userLevel,
    String profileImageUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static AdminUserResponse from(User user) {
        return new AdminUserResponse(
            user.getId(),
            user.getNickname(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getUserRole(),
            user.getUserSex(),
            user.getUserLevel(),
            user.getProfileImageUrl(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}