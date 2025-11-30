package com.line7studio.boulderside.domain.feature.boulder.interaction.like.service;

import com.line7studio.boulderside.domain.feature.boulder.interaction.like.entity.UserBoulderLike;

import java.util.List;
import java.util.Map;

public interface UserBoulderLikeService {
	boolean toggle(UserBoulderLike userBoulderLike);

	long getCountByBoulderId(Long boulderId);

	boolean existsIsLikedByUserId(Long boulderId, Long userId);
	
	Map<Long, Boolean> getIsLikedByUserIdForBoulderList(List<Long> boulderIdList, Long userId);

	void deleteAllLikesByBoulderId(Long boulderId);
}
