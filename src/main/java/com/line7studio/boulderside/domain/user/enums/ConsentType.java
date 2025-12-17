package com.line7studio.boulderside.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConsentType {
    PUSH("푸시 알림 동의"),
    MARKETING("마케팅 정보 수신 동의"),
    PRIVACY("개인정보 수집 및 이용 동의"),
    SERVICE_TERMS("서비스 이용 약관 동의"),
    OVER_FOURTEEN("14세 이상 동의");

    private final String description;
}
