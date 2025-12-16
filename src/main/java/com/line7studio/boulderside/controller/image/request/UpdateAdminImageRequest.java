package com.line7studio.boulderside.controller.image.request;

import com.line7studio.boulderside.domain.feature.image.enums.ImageDomainType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateAdminImageRequest(
    @NotNull(message = "연관 도메인 ID는 필수입니다.") Long domainId,
    @NotNull(message = "이미지 도메인 타입은 필수입니다.") ImageDomainType imageDomainType,
    @NotBlank(message = "이미지 URL은 필수입니다.") String imageUrl,
    String originalFileName,
    Integer orderIndex
) {}