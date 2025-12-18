package com.line7studio.boulderside.controller.user.request;

import com.line7studio.boulderside.domain.user.enums.UserStatus;
import com.line7studio.boulderside.domain.user.enums.UserStatusChangeReason;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserStatusRequest(
    @NotNull
    UserStatus userStatus,
    UserStatusChangeReason reasonType,
    @Size(max = 255)
    String reasonDetail
) {
}
