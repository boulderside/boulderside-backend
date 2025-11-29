package com.line7studio.boulderside.domain.aggregate.sector.repository;

import com.line7studio.boulderside.domain.aggregate.sector.Sector;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectorRepository extends JpaRepository<Sector, Long> {
}
