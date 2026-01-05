package com.line7studio.boulderside.controller.user.request;

import jakarta.validation.constraints.Size;

public record WithdrawUserRequest(
    @Size(max = 500, message = "탈퇴 사유는 500자 이하여야 합니다.")
    String reason
) {
}
