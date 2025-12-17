package com.line7studio.boulderside.domain.boulder.interaction.like.service;

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
import com.line7studio.boulderside.domain.boulder.interaction.like.UserBoulderLike;
import com.line7studio.boulderside.domain.boulder.interaction.like.repository.UserBoulderLikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserBoulderLikeService {
	private final UserBoulderLikeRepository userBoulderLikeRepository;

	public boolean toggle(UserBoulderLike userBoulderLike) {
		Long userId = userBoulderLike.getUserId();
		Long boulderId = userBoulderLike.getBoulderId();
		if (userId == null || boulderId == null) {
			throw new InvalidValueException(ErrorCode.VALIDATION_FAILED);
		}

		boolean alreadyExists = userBoulderLikeRepository.existsByUserIdAndBoulderId(userId, boulderId);

		if (alreadyExists) {
			userBoulderLikeRepository.deleteByUserIdAndBoulderId(userId, boulderId);
			return false;
		} else {
			userBoulderLikeRepository.save(userBoulderLike);
			return true;
		}
	}

	public long getCountByBoulderId(Long boulderId) {
		if (boulderId == null) {
			throw new InvalidValueException(ErrorCode.VALIDATION_FAILED);
		}
		return userBoulderLikeRepository.countByBoulderId(boulderId);
	}

	public boolean existsIsLikedByUserId(Long boulderId, Long userId) {
		return userBoulderLikeRepository.existsByUserIdAndBoulderId(userId, boulderId);
	}
	
	public Map<Long, Boolean> getIsLikedByUserIdForBoulderList(List<Long> boulderIdList, Long userId) {
		if (boulderIdList == null || boulderIdList.isEmpty() || userId == null) {
			return Map.of();
		}

		List<UserBoulderLike> userBoulderLikeList = userBoulderLikeRepository.findByUserIdAndBoulderIdIn(userId, boulderIdList);
		Set<Long> likedBoulderIdList = userBoulderLikeList.stream()
			.map(UserBoulderLike::getBoulderId)
			.collect(Collectors.toSet());

		return boulderIdList.stream()
			.collect(Collectors.toMap(
				boulderId -> boulderId,
                    likedBoulderIdList::contains
			));
	}

	public void deleteAllLikesByBoulderId(Long boulderId) {
		if (boulderId == null) {
			throw new InvalidValueException(ErrorCode.VALIDATION_FAILED);
		}
		userBoulderLikeRepository.deleteAllByBoulderId(boulderId);
	}

	public List<UserBoulderLike> getLikesByUser(Long userId, Long cursor, int size) {
		Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));
		if (cursor == null) {
			return userBoulderLikeRepository.findByUserIdOrderByIdDesc(userId, pageable);
		}
		return userBoulderLikeRepository.findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, pageable);
	}
}
