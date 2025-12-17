package com.line7studio.boulderside.controller.boulder.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateBoulderRequest(
    @NotBlank(message = "바위 이름은 필수입니다") String name,
    String description,
    @NotNull(message = "위도는 필수입니다") Double latitude,
    @NotNull(message = "경도는 필수입니다") Double longitude,
    @NotNull(message = "섹터 ID는 필수입니다") Long sectorId,
    String province,
    String city,
    List<String> imageUrlList
) {}