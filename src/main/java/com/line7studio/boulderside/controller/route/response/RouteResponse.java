package com.line7studio.boulderside.controller.route.response;

import com.line7studio.boulderside.common.dto.ImageInfo;
import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.feature.route.Route;
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
public class RouteResponse {
	private Long routeId;
	private Long boulderId;
	private String boulderName;
	private String province;
	private String city;
	private String name;
	private String pioneerName;
	private Double latitude;
	private Double longitude;
	private String sectorName;
	private String areaCode;
	private Level routeLevel;
	private Long likeCount;
	private Boolean liked;
	private Long viewCount;
	private Long climberCount;
	private Long commentCount;
	private List<ImageInfo> imageInfoList;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static RouteResponse of(Route route, String province, String city, String boulderName, String sectorName, String areaCode,
		List<ImageInfo> imageInfoList, Long likeCount, Boolean liked) {
		return RouteResponse.builder()
			.routeId(route.getId())
			.boulderId(route.getBoulderId())
			.boulderName(boulderName)
			.province(province)
			.city(city)
			.name(route.getName())
			.pioneerName(route.getPioneerName())
			.latitude(route.getLatitude())
			.longitude(route.getLongitude())
			.sectorName(sectorName)
			.areaCode(areaCode)
			.routeLevel(route.getRouteLevel())
			.likeCount(likeCount)
			.liked(liked)
			.viewCount(route.getViewCount())
			.climberCount(route.getClimberCount())
			.commentCount(route.getCommentCount())
			.imageInfoList(imageInfoList)
			.createdAt(route.getCreatedAt())
			.updatedAt(route.getUpdatedAt())
			.build();
	}
}
