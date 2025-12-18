package com.line7studio.boulderside.controller.user.response;

import com.line7studio.boulderside.domain.user.UserLoginHistory;
import java.time.LocalDateTime;

public record UserLoginHistoryResponse(
    Long id,
    Long userId,
    String ipAddress,
    String userAgent,
    LocalDateTime loginAt
) {

    public static UserLoginHistoryResponse from(UserLoginHistory history) {
        return new UserLoginHistoryResponse(
            history.getId(),
            history.getUserId(),
            history.getIpAddress(),
            history.getUserAgent(),
            history.getLoginAt()
        );
    }
}
