package com.line7studio.boulderside.domain.aggregate.image.repository;

import com.line7studio.boulderside.domain.aggregate.image.entity.Image;
import com.line7studio.boulderside.domain.aggregate.image.enums.ImageDomainType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
	List<Image> findByImageDomainTypeAndTargetIdIn(ImageDomainType imageDomainType, List<Long> targetIdList);

	List<Image> findByImageDomainTypeAndTargetId(ImageDomainType imageDomainType, Long targetId);

	void deleteByImageDomainTypeAndTargetId(ImageDomainType imageDomainType, Long targetId);
}
