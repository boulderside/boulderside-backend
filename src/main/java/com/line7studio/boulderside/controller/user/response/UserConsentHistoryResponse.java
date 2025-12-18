package com.line7studio.boulderside.controller.user.response;

import com.line7studio.boulderside.domain.user.UserConsentHistory;
import com.line7studio.boulderside.domain.user.enums.ConsentType;
import java.time.LocalDateTime;

public record UserConsentHistoryResponse(
    Long id,
    Long userId,
    ConsentType consentType,
    Boolean agreed,
    String consentVersion,
    String ipAddress,
    LocalDateTime createdAt
) {

    public static UserConsentHistoryResponse from(UserConsentHistory history) {
        return new UserConsentHistoryResponse(
            history.getId(),
            history.getUserId(),
            history.getConsentType(),
            history.getAgreed(),
            history.getConsentVersion(),
            history.getIpAddress(),
            history.getCreatedAt()
        );
    }
}
