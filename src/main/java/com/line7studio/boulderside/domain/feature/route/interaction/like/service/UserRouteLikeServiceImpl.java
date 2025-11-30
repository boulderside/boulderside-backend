package com.line7studio.boulderside.domain.feature.route.interaction.like.service;

import org.springframework.stereotype.Service;

import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.exception.ValidationException;
import com.line7studio.boulderside.domain.feature.route.interaction.like.entity.UserRouteLike;
import com.line7studio.boulderside.domain.feature.route.interaction.like.repository.UserRouteLikeRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRouteLikeServiceImpl implements UserRouteLikeService {
	private final UserRouteLikeRepository userRouteLikeRepository;

	@Override
	public boolean toggle(UserRouteLike userRouteLike) {
		Long userId = userRouteLike.getUserId();
		Long routeId = userRouteLike.getRouteId();
		if (userId == null || routeId == null) {
			throw new ValidationException(ErrorCode.VALIDATION_FAILED);
		}

		boolean alreadyExists = userRouteLikeRepository.existsByUserIdAndRouteId(userId, routeId);

		if (alreadyExists) {
			userRouteLikeRepository.deleteByUserIdAndRouteId(userId, routeId);
			return false;
		} else {
			userRouteLikeRepository.save(userRouteLike);
			return true;
		}
	}

	@Override
	public boolean existsIsLikedByUserId(Long routeId, Long userId) {
		return userRouteLikeRepository.existsByUserIdAndRouteId(userId, routeId);
	}
	
	@Override
	public Map<Long, Boolean> getIsLikedByUserIdForRouteList(List<Long> routeIdList, Long userId) {
		if (routeIdList == null || routeIdList.isEmpty() || userId == null) {
			return Map.of();
		}

		List<UserRouteLike> userRouteLikeList = userRouteLikeRepository.findByUserIdAndRouteIdIn(userId, routeIdList);
		Set<Long> likedRouteIdList = userRouteLikeList.stream()
			.map(UserRouteLike::getRouteId)
			.collect(Collectors.toSet());

		return routeIdList.stream()
			.collect(Collectors.toMap(
				routeId -> routeId,
                    likedRouteIdList::contains
			));
	}

	@Override
	public void deleteAllLikesByRouteId(Long routeId) {
		if (routeId == null) {
			throw new ValidationException(ErrorCode.VALIDATION_FAILED);
		}
		userRouteLikeRepository.deleteAllByRouteId(routeId);
	}
}