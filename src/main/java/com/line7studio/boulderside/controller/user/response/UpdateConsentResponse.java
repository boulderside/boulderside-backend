package com.line7studio.boulderside.controller.user.response;

import com.line7studio.boulderside.domain.user.enums.ConsentType;
import lombok.Builder;

@Builder
public record UpdateConsentResponse(
	ConsentType consentType,
	boolean agreed
) {
	public static UpdateConsentResponse of(ConsentType consentType, boolean agreed) {
		return UpdateConsentResponse.builder()
			.consentType(consentType)
			.agreed(agreed)
			.build();
	}
}
