package com.line7studio.boulderside.controller.boulder.response;

import com.line7studio.boulderside.application.boulder.dto.BoulderWithRegion;
import com.line7studio.boulderside.common.dto.ImageInfo;
import com.line7studio.boulderside.domain.feature.boulder.entity.Boulder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoulderResponse {
	private Long boulderId;
	private String name;
	private String description;
	private Double latitude;
	private Double longitude;
	private String sectorName;
	private String areaCode;
	private String province;
	private String city;
	private Long likeCount;
	private Long viewCount;
	private Boolean liked;
	private List<ImageInfo> imageInfoList;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static BoulderResponse of(Boulder boulder, String province, String city, String sectorName, String areaCode,
		List<ImageInfo> imageInfoList, Long likeCount, boolean isLiked) {
		return BoulderResponse.builder()
			.boulderId(boulder.getId())
			.name(boulder.getName())
			.description(boulder.getDescription())
			.latitude(boulder.getLatitude())
			.longitude(boulder.getLongitude())
			.sectorName(sectorName)
			.areaCode(areaCode)
			.province(province)
			.city(city)
			.imageInfoList(imageInfoList)
			.likeCount(likeCount)
			.viewCount(boulder.getViewCount())
			.liked(isLiked)
			.createdAt(boulder.getCreatedAt())
			.updatedAt(boulder.getUpdatedAt())
			.build();
	}

	public static BoulderResponse of(BoulderWithRegion boulderWithRegion, List<ImageInfo> imageInfoList,
		Long likeCount, Boolean isLiked) {
		return BoulderResponse.builder()
			.boulderId(boulderWithRegion.getId())
			.name(boulderWithRegion.getName())
			.description(boulderWithRegion.getDescription())
			.latitude(boulderWithRegion.getLatitude())
			.longitude(boulderWithRegion.getLongitude())
			.sectorName(boulderWithRegion.getSectorName())
			.areaCode(boulderWithRegion.getAreaCode())
			.province(boulderWithRegion.getProvince())
			.city(boulderWithRegion.getCity())
			.imageInfoList(imageInfoList)
			.likeCount(likeCount)
			.viewCount(boulderWithRegion.getViewCount())
			.liked(isLiked)
			.createdAt(boulderWithRegion.getCreatedAt())
			.updatedAt(boulderWithRegion.getUpdatedAt())
			.build();
	}
}
