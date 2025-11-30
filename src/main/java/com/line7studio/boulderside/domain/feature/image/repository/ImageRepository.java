package com.line7studio.boulderside.domain.feature.image.repository;

import com.line7studio.boulderside.domain.feature.image.entity.Image;
import com.line7studio.boulderside.domain.feature.image.enums.ImageDomainType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
	List<Image> findByImageDomainTypeAndDomainIdIn(ImageDomainType imageDomainType, List<Long> domainIdList);

	List<Image> findByImageDomainTypeAndDomainId(ImageDomainType imageDomainType, Long domainId);

	List<Image> findByImageDomainTypeAndDomainIdAndOrderIndex(ImageDomainType imageDomainType, Long domainId, Integer orderIndex);

	void deleteByImageDomainTypeAndDomainId(ImageDomainType imageDomainType, Long domainId);
}
