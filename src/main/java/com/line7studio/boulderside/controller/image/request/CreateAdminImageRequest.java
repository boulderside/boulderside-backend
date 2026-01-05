package com.line7studio.boulderside.controller.image.request;

import com.line7studio.boulderside.domain.image.enums.ImageDomainType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAdminImageRequest(
    @NotNull(message = "연관 도메인 ID는 필수입니다.")
    Long domainId,

    @NotNull(message = "이미지 도메인 타입은 필수입니다.")
    ImageDomainType imageDomainType,

    @NotBlank(message = "이미지 URL은 필수입니다.")
    @Size(min = 1, max = 2000, message = "이미지 URL은 1자 이상 2000자 이하여야 합니다.")
    String imageUrl,

    @Size(max = 255, message = "원본 파일명은 255자 이하여야 합니다.")
    String originalFileName,

    Integer orderIndex
) {}