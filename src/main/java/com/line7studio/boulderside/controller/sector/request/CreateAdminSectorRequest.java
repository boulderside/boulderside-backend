package com.line7studio.boulderside.controller.sector.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdminSectorRequest {
    @NotBlank(message = "섹터 이름은 필수입니다.")
    private String sectorName;

    @NotBlank(message = "구역 코드는 필수입니다.")
    private String areaCode;
}
