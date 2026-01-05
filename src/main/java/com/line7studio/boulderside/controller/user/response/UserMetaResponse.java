package com.line7studio.boulderside.controller.user.response;

import com.line7studio.boulderside.domain.user.User;

public record UserMetaResponse(
    Long userId,
    Boolean pushEnabled,
    Boolean marketingAgreed,
    Boolean privacyAgreed,
    Boolean serviceTermsAgreed,
    Boolean overFourteenAgreed
) {

    public static UserMetaResponse from(User user) {
        return new UserMetaResponse(
            user.getId(),
            user.getPushEnabled(),
            user.getMarketingAgreed(),
            user.getPrivacyAgreed(),
            user.getServiceTermsAgreed(),
            user.getOverFourteenAgreed()
        );
    }
}
