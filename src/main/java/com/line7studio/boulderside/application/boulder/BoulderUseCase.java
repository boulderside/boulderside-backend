package com.line7studio.boulderside.application.boulder;

import java.util.List;

import org.springframework.stereotype.Service;

import com.line7studio.boulderside.application.boulder.dto.BoulderWithRegion;
import com.line7studio.boulderside.controller.boulder.request.CreateBoulderRequest;
import com.line7studio.boulderside.controller.boulder.request.UpdateBoulderRequest;
import com.line7studio.boulderside.controller.boulder.response.BoulderPageResponse;
import com.line7studio.boulderside.controller.boulder.response.BoulderResponse;
import com.line7studio.boulderside.domain.aggregate.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.aggregate.boulder.service.BoulderQueryService;
import com.line7studio.boulderside.domain.aggregate.boulder.service.BoulderService;
import com.line7studio.boulderside.domain.aggregate.region.entity.Region;
import com.line7studio.boulderside.domain.aggregate.region.service.RegionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoulderUseCase {
	private final RegionService regionService;
	private final BoulderService boulderService;
	private final BoulderQueryService boulderQueryService;

	public BoulderPageResponse getBoulderPage(Long cursor, int size) {
		List<BoulderWithRegion> boulderWithRegionList = boulderQueryService.getBoulderWithRegionList(cursor, size);
		int boulderWithRegionListSize = boulderWithRegionList.size();

		boolean hasNext = boulderWithRegionListSize > size;
		List<BoulderWithRegion> boulderWithRegionPage = boulderWithRegionList.subList(0,
			Math.min(size, boulderWithRegionListSize));

		List<BoulderResponse> boulderResponseList = boulderWithRegionPage.stream()
			.map(BoulderResponse::from)
			.toList();

		Long nextCursor = hasNext
			? boulderResponseList.getLast().getId()
			: null;

		return BoulderPageResponse.of(boulderResponseList, nextCursor, hasNext,
			Math.min(size, boulderWithRegionListSize));
	}

	public BoulderResponse getBoulderById(Long boulderId) {
		BoulderWithRegion boulderWithRegion = boulderQueryService.getBoulderWithRegion(boulderId);
		return BoulderResponse.from(boulderWithRegion);
	}

	public BoulderResponse createBoulder(CreateBoulderRequest request) {
		Region region = regionService.getRegionByProvinceAndCity(request.getProvince(), request.getCity());

		Boulder boulder = Boulder.builder()
			.name(request.getName())
			.description(request.getDescription())
			.latitude(request.getLatitude())
			.longitude(request.getLongitude())
			.regionId(region.getId())
			.build();

		Boulder savedBoulder = boulderService.createBoulder(boulder);
		return BoulderResponse.from(savedBoulder, region.getProvince(), region.getCity());
	}

	public BoulderResponse updateBoulder(Long boulderId, UpdateBoulderRequest request) {
		Boulder boulder = boulderService.getBoulderById(boulderId);
		Region region = regionService.getRegionByProvinceAndCity(request.getProvince(), request.getCity());

		boulder.update(
			request.getName(),
			request.getDescription(),
			request.getLatitude(),
			request.getLongitude(),
			region.getId()
		);

		return BoulderResponse.from(boulder, region.getProvince(), region.getCity());
	}

	public void deleteBoulder(Long boulderId) {
		boulderService.deleteBoulder(boulderId);
	}
}
