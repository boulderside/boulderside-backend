package com.line7studio.boulderside.controller.user.response;

import com.line7studio.boulderside.domain.user.UserMeta;
import lombok.Builder;

@Builder
public record UserMetaResponse(
	Boolean pushEnabled,
	Boolean marketingAgreed
) {
	public static UserMetaResponse from(UserMeta userMeta) {
		return UserMetaResponse.builder()
			.pushEnabled(userMeta.getPushEnabled())
			.marketingAgreed(userMeta.getMarketingAgreed())
			.build();
	}
}
