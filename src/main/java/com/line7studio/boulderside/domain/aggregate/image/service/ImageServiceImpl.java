package com.line7studio.boulderside.domain.aggregate.image.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.line7studio.boulderside.domain.aggregate.image.entity.Image;
import com.line7studio.boulderside.domain.aggregate.image.enums.TargetType;
import com.line7studio.boulderside.domain.aggregate.image.repository.ImageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

	private final ImageRepository imageRepository;

	@Override
	public List<Image> getImageListByTargetTypeAndTargetIdList(TargetType targetType, List<Long> targetIdList) {
		return imageRepository.findByTargetTypeAndTargetIdIn(TargetType.BOULDER, targetIdList);
	}

	@Override
	public List<Image> getImageListByTargetTypeAndTargetId(TargetType targetType, Long targetId) {
		return imageRepository.findByTargetTypeAndTargetId(TargetType.BOULDER, targetId);
	}

	@Override
	public List<Image> createImages(List<Image> imageList) {
		return imageRepository.saveAll(imageList);
	}

	@Override
	public void deleteImagesByTargetTypeAndTargetId(TargetType targetType, Long targetId) {
		imageRepository.deleteByTargetTypeAndTargetId(TargetType.BOULDER, targetId);
	}
}
