package com.line7studio.boulderside.domain.aggregate.image.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.line7studio.boulderside.domain.aggregate.image.entity.Image;
import com.line7studio.boulderside.domain.aggregate.image.enums.TargetType;

public interface ImageRepository extends JpaRepository<Image, Long> {
	List<Image> findByTargetTypeAndTargetIdIn(TargetType targetType, List<Long> targetIdList);

	List<Image> findByTargetTypeAndTargetId(TargetType targetType, Long targetId);

	void deleteByTargetTypeAndTargetId(TargetType targetType, Long targetId);
}
