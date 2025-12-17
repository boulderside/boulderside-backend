package com.line7studio.boulderside.usecase.boulder.dto;

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
	private Long sectorId;
	private String sectorName;
	private String areaCode;
	private Long viewCount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	// Region
	private Long regionId;
	private String province;
	private String city;
	private String regionCode;
	private String officialDistrictCode;
}
