package com.line7studio.boulderside.controller.user.request;

import com.line7studio.boulderside.domain.user.enums.UserStatus;
import com.line7studio.boulderside.domain.user.enums.UserStatusChangeReason;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserStatusRequest(
    @NotNull(message = "사용자 상태는 필수입니다.")
    UserStatus userStatus,

    UserStatusChangeReason reasonType,

    @Size(max = 255, message = "상태 변경 상세 사유는 255자 이하여야 합니다.")
    String reasonDetail
) {
}
