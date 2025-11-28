package com.line7studio.boulderside.domain.association.like.service;

import com.line7studio.boulderside.domain.association.like.entity.UserRouteLike;

import java.util.List;
import java.util.Map;

public interface UserRouteLikeService {
	boolean toggle(UserRouteLike userRouteLike);

	boolean existsIsLikedByUserId(Long routeId, Long userId);
	
	Map<Long, Boolean> getIsLikedByUserIdForRouteList(List<Long> routeIdList, Long userId);

	void deleteAllLikesByRouteId(Long routeId);
}