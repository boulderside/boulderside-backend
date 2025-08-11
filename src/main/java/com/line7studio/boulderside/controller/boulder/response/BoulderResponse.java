package com.line7studio.boulderside.controller.boulder.response;

import java.time.LocalDateTime;
import java.util.List;

import com.line7studio.boulderside.application.boulder.dto.BoulderWithRegion;
import com.line7studio.boulderside.common.dto.ImageInfo;
import com.line7studio.boulderside.domain.aggregate.boulder.entity.Boulder;

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
	private String province;
	private String city;
	private Long likeCount;
	private boolean liked;
	private List<ImageInfo> imageInfoList;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static BoulderResponse of(Boulder boulder, String province, String city, List<ImageInfo> imageInfoList,
		long likeCount) {
		return BoulderResponse.builder()
			.id(boulder.getId())
			.name(boulder.getName())
			.description(boulder.getDescription())
			.latitude(boulder.getLatitude())
			.longitude(boulder.getLongitude())
			.province(province)
			.city(city)
			.imageInfoList(imageInfoList)
			.likeCount(likeCount)
			.createdAt(boulder.getCreatedAt())
			.updatedAt(boulder.getUpdatedAt())
			.build();
	}

	public static BoulderResponse of(BoulderWithRegion boulderWithRegion, List<ImageInfo> imageInfoList) {
		return BoulderResponse.builder()
			.id(boulderWithRegion.getId())
			.name(boulderWithRegion.getName())
			.description(boulderWithRegion.getDescription())
			.latitude(boulderWithRegion.getLatitude())
			.longitude(boulderWithRegion.getLongitude())
			.province(boulderWithRegion.getProvince())
			.city(boulderWithRegion.getCity())
			.imageInfoList(imageInfoList)
			.likeCount(0L)
			.createdAt(boulderWithRegion.getCreatedAt())
			.updatedAt(boulderWithRegion.getUpdatedAt())
			.build();
	}

	public static BoulderResponse of(BoulderWithRegion boulderWithRegion, List<ImageInfo> imageInfoList,
		long likeCount, boolean isLiked) {
		return BoulderResponse.builder()
			.id(boulderWithRegion.getId())
			.name(boulderWithRegion.getName())
			.description(boulderWithRegion.getDescription())
			.latitude(boulderWithRegion.getLatitude())
			.longitude(boulderWithRegion.getLongitude())
			.province(boulderWithRegion.getProvince())
			.city(boulderWithRegion.getCity())
			.imageInfoList(imageInfoList)
			.likeCount(likeCount)
			.liked(isLiked)
			.createdAt(boulderWithRegion.getCreatedAt())
			.updatedAt(boulderWithRegion.getUpdatedAt())
			.build();
	}
}