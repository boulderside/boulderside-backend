package com.line7studio.boulderside.controller.user.response;

import com.line7studio.boulderside.domain.user.User;
import com.line7studio.boulderside.domain.user.UserBlock;

import java.time.LocalDateTime;

public record BlockedUserResponse(
    Long userId,
    String nickname,
    LocalDateTime blockedAt
) {

    public static BlockedUserResponse of(UserBlock userBlock, User user) {
        return new BlockedUserResponse(
            userBlock.getBlockedId(),
            user != null ? user.getNickname() : null,
            userBlock.getCreatedAt()
        );
    }
}
