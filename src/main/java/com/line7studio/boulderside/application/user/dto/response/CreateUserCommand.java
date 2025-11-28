package com.line7studio.boulderside.application.user.dto.response;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.aggregate.user.enums.UserRole;
import com.line7studio.boulderside.domain.aggregate.user.enums.UserSex;
import lombok.Builder;

@Builder
public record CreateUserCommand(
        String nickname,
        String encodedPhone,
        UserRole userRole,
        UserSex userSex,
        Level userLevel,
        String name,
        String email,
        String rawPassword
) {}