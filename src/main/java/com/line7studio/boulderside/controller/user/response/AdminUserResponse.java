package com.line7studio.boulderside.controller.user.response;

import java.time.LocalDateTime;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.user.User;
import com.line7studio.boulderside.domain.user.enums.AuthProviderType;
import com.line7studio.boulderside.domain.user.enums.UserRole;
import com.line7studio.boulderside.domain.user.enums.UserStatus;

public record AdminUserResponse(
    Long userId,
    String nickname,
    String providerEmail,
    AuthProviderType providerType,
    UserRole userRole,
    UserStatus userStatus,
    Level userLevel,
    String profileImageUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static AdminUserResponse from(User user) {
        return new AdminUserResponse(
            user.getId(),
            user.getNickname(),
            user.getProviderEmail(),
            user.getProviderType(),
            user.getUserRole(),
            user.getUserStatus(),
            user.getUserLevel(),
            user.getProfileImageUrl(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
