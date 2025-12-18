package com.line7studio.boulderside.controller.user.request;

import com.line7studio.boulderside.domain.user.enums.ConsentType;
import jakarta.validation.constraints.NotNull;

public record UpdateConsentRequest(
    @NotNull(message = "Consent type is required")
    ConsentType consentType,

    @NotNull(message = "Agreed value is required")
    Boolean agreed
) {}
