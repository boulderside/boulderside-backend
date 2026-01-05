package com.line7studio.boulderside.domain.route.interaction.like.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.exception.InvalidValueException;
import com.line7studio.boulderside.domain.route.interaction.like.UserRouteLike;
import com.line7studio.boulderside.domain.route.interaction.like.repository.UserRouteLikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRouteLikeService {
	private final UserRouteLikeRepository userRouteLikeRepository;

	public boolean toggle(UserRouteLike userRouteLike) {
		Long userId = userRouteLike.getUserId();
		Long routeId = userRouteLike.getRouteId();
		if (userId == null || routeId == null) {
			throw new InvalidValueException(ErrorCode.VALIDATION_FAILED);
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

	public boolean existsIsLikedByUserId(Long routeId, Long userId) {
		return userRouteLikeRepository.existsByUserIdAndRouteId(userId, routeId);
	}

	public long getCountByRouteId(Long routeId) {
		if (routeId == null) {
			throw new InvalidValueException(ErrorCode.VALIDATION_FAILED);
		}
		return userRouteLikeRepository.countByRouteId(routeId);
	}
	
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

	public void deleteAllLikesByRouteId(Long routeId) {
		if (routeId == null) {
			throw new InvalidValueException(ErrorCode.VALIDATION_FAILED);
		}
		userRouteLikeRepository.deleteAllByRouteId(routeId);
	}

	public List<UserRouteLike> getLikesByUser(Long userId, Long cursor, int size) {
		Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));
		if (cursor == null) {
			return userRouteLikeRepository.findByUserIdOrderByIdDesc(userId, pageable);
		}
		return userRouteLikeRepository.findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, pageable);
	}
}
