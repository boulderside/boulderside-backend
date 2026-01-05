package com.line7studio.boulderside.controller.boulder.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateBoulderRequest(
    @NotBlank(message = "바위 이름은 필수입니다.")
    @Size(min = 1, max = 100, message = "바위 이름은 1자 이상 100자 이하여야 합니다.")
    String name,

    @Size(max = 1000, message = "설명은 1000자 이하여야 합니다.")
    String description,

    @NotNull(message = "위도는 필수입니다.")
    Double latitude,

    @NotNull(message = "경도는 필수입니다.")
    Double longitude,

    @Size(max = 50, message = "시/도는 50자 이하여야 합니다.")
    String province,

    @Size(max = 50, message = "시/군/구는 50자 이하여야 합니다.")
    String city,

    List<String> imageUrlList
) {}