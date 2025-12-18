package com.line7studio.boulderside.controller.user.response;

import com.line7studio.boulderside.domain.user.UserMeta;
import java.time.LocalDateTime;

public record UserMetaResponse(
    Long userId,
    Boolean pushEnabled,
    Boolean marketingAgreed,
    LocalDateTime marketingAgreedCreatedAt,
    LocalDateTime marketingAgreedUpdatedAt,
    Boolean privacyAgreed,
    LocalDateTime privacyAgreedCreatedAt,
    Boolean serviceTermsAgreed,
    LocalDateTime serviceTermsAgreedCreatedAt,
    Boolean overFourteenAgreed,
    LocalDateTime overFourteenAgreedCreatedAt
) {

    public static UserMetaResponse from(UserMeta userMeta) {
        return new UserMetaResponse(
            userMeta.getUserId(),
            userMeta.getPushEnabled(),
            userMeta.getMarketingAgreed(),
            userMeta.getMarketingAgreedCreatedAt(),
            userMeta.getMarketingAgreedUpdatedAt(),
            userMeta.getPrivacyAgreed(),
            userMeta.getPrivacyAgreedCreatedAt(),
            userMeta.getServiceTermsAgreed(),
            userMeta.getServiceTermsAgreedCreatedAt(),
            userMeta.getOverFourteenAgreed(),
            userMeta.getOverFourteenAgreedCreatedAt()
        );
    }
}
