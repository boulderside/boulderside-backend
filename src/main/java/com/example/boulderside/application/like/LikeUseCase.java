package com.example.boulderside.application.like;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.boulderside.domain.boulder.entity.Boulder;
import com.example.boulderside.domain.boulder.service.BoulderService;
import com.example.boulderside.domain.user.entity.User;
import com.example.boulderside.domain.user.service.UserService;
import com.example.boulderside.feature.like.entity.UserBoulderLike;
import com.example.boulderside.feature.like.service.UserBoulderLikeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeUseCase {
	private final UserService userService;
	private final BoulderService boulderService;
	private final UserBoulderLikeService userBoulderLikeService;

	@Transactional
	public void likeBoulder(Long userId, Long boulderId) {
		User user = userService.getUserById(userId);
		Boulder boulder = boulderService.getBoulderById(boulderId);

		UserBoulderLike userBoulderLike = UserBoulderLike.builder()
			.userId(user.getId())
			.boulderId(boulder.getId())
			.build();

		userBoulderLikeService.save(userBoulderLike);
	}
}
