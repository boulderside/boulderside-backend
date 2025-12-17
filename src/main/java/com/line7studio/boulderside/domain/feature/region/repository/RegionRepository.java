package com.line7studio.boulderside.domain.feature.region.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.line7studio.boulderside.domain.feature.region.Region;

public interface RegionRepository extends JpaRepository<Region, Long> {
	Optional<Region> findByProvinceAndCity(String province, String city);
}
