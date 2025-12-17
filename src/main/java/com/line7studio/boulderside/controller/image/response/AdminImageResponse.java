package com.line7studio.boulderside.controller.image.response;

import com.line7studio.boulderside.domain.feature.image.Image;
import com.line7studio.boulderside.domain.feature.image.enums.ImageDomainType;

public record AdminImageResponse(
    Long id,
    Long domainId,
    ImageDomainType imageDomainType,
    String imageUrl,
    String originalFileName,
    Integer orderIndex
) {
    public static AdminImageResponse from(Image image) {
        return new AdminImageResponse(
            image.getId(),
            image.getDomainId(),
            image.getImageDomainType(),
            image.getImageUrl(),
            image.getOriginalFileName(),
            image.getOrderIndex()
        );
    }
}