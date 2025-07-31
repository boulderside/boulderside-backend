package com.example.boulderside.controller.boulder.request;

import java.util.List;

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
public class CreateBoulderRequest {

	@NotBlank(message = "바위 이름은 필수입니다")
	private String name;

	private String description;

	@NotNull(message = "위도는 필수입니다")
	private Double latitude;

	@NotNull(message = "경도는 필수입니다")
	private Double longitude;

	private String province;

	private String city;

	private List<String> imageUrls;
}