package com.example.boulderside.application.boulder.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoulderWithRegion {
	// Boulder
	private Long id;
	private String name;
	private String description;
	private Double latitude;
	private Double longitude;
	private Long likeCount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	// Region
	private Long regionId;
	private String province;
	private String city;
	private String regionCode;
	private String officialDistrictCode;
}