package com.line7studio.boulderside.common.dto;

import com.line7studio.boulderside.domain.aggregate.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Long id;
    private String nickname;
    private String profileImageUrl;

    public static UserInfo from(User user) {
        return UserInfo.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}

