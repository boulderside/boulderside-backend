package com.line7studio.boulderside.controller.image.request;

import com.line7studio.boulderside.domain.feature.image.enums.ImageDomainType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdminImageRequest {
	@NotNull(message = "연관 도메인 ID는 필수입니다.")
	private Long domainId;

	@NotNull(message = "이미지 도메인 타입은 필수입니다.")
	private ImageDomainType imageDomainType;

	@NotBlank(message = "이미지 URL은 필수입니다.")
	private String imageUrl;

	private String originalFileName;

	private Integer orderIndex;
}
