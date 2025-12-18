package com.line7studio.boulderside.controller.user.response;

import com.line7studio.boulderside.domain.user.UserStatusHistory;
import com.line7studio.boulderside.domain.user.enums.UserStatus;
import com.line7studio.boulderside.domain.user.enums.UserStatusChangeReason;
import java.time.LocalDateTime;

public record UserStatusHistoryResponse(
    Long id,
    Long userId,
    UserStatus previousStatus,
    UserStatus newStatus,
    UserStatusChangeReason changeReasonType,
    String changeReasonDetail,
    Long changedBy,
    LocalDateTime createdAt
) {

    public static UserStatusHistoryResponse from(UserStatusHistory history) {
        return new UserStatusHistoryResponse(
            history.getId(),
            history.getUserId(),
            history.getPreviousStatus(),
            history.getNewStatus(),
            history.getChangeReasonType(),
            history.getChangeReasonDetail(),
            history.getChangedBy(),
            history.getCreatedAt()
        );
    }
}
