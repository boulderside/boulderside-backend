package com.example.boulderside.controller.boulder.response;

import java.time.LocalDateTime;
import java.util.List;

import com.example.boulderside.application.boulder.dto.BoulderWithRegion;
import com.example.boulderside.domain.boulder.entity.Boulder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoulderResponse {
	private Long id;
	private String name;
	private String description;
	private Double latitude;
	private Double longitude;
	private Long likeCount;
	private String province;
	private String city;
	private List<String> imageUrlList;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static BoulderResponse from(Boulder boulder, String province, String city) {
		return BoulderResponse.builder()
			.id(boulder.getId())
			.name(boulder.getName())
			.description(boulder.getDescription())
			.latitude(boulder.getLatitude())
			.longitude(boulder.getLongitude())
			.likeCount(boulder.getLikeCount())
			.province(province)
			.city(city)
			.createdAt(boulder.getCreatedAt())
			.updatedAt(boulder.getUpdatedAt())
			.build();
	}

	public static BoulderResponse from(BoulderWithRegion boulderWithRegion) {
		return BoulderResponse.builder()
			.id(boulderWithRegion.getId())
			.name(boulderWithRegion.getName())
			.description(boulderWithRegion.getDescription())
			.latitude(boulderWithRegion.getLatitude())
			.longitude(boulderWithRegion.getLongitude())
			.likeCount(boulderWithRegion.getLikeCount())
			.province(boulderWithRegion.getProvince())
			.city(boulderWithRegion.getCity())
			.createdAt(boulderWithRegion.getCreatedAt())
			.updatedAt(boulderWithRegion.getUpdatedAt())
			.build();
	}
}