package com.line7studio.boulderside.domain.instagram.interaction.like.service;

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
import com.line7studio.boulderside.domain.instagram.interaction.like.UserInstagramLike;
import com.line7studio.boulderside.domain.instagram.interaction.like.repository.UserInstagramLikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserInstagramLikeService {
	private final UserInstagramLikeRepository userInstagramLikeRepository;

	public boolean toggle(UserInstagramLike userInstagramLike) {
		Long userId = userInstagramLike.getUserId();
		Long instagramId = userInstagramLike.getInstagramId();
		if (userId == null || instagramId == null) {
			throw new InvalidValueException(ErrorCode.VALIDATION_FAILED);
		}

		boolean alreadyExists = userInstagramLikeRepository.existsByUserIdAndInstagramId(userId, instagramId);

		if (alreadyExists) {
			userInstagramLikeRepository.deleteByUserIdAndInstagramId(userId, instagramId);
			return false;
		} else {
			userInstagramLikeRepository.save(userInstagramLike);
			return true;
		}
	}

	public long getCountByInstagramId(Long instagramId) {
		if (instagramId == null) {
			throw new InvalidValueException(ErrorCode.VALIDATION_FAILED);
		}
		return userInstagramLikeRepository.countByInstagramId(instagramId);
	}

	public boolean existsIsLikedByUserId(Long instagramId, Long userId) {
		return userInstagramLikeRepository.existsByUserIdAndInstagramId(userId, instagramId);
	}

	public Map<Long, Boolean> getIsLikedByUserIdForInstagramList(List<Long> instagramIdList, Long userId) {
		if (instagramIdList == null || instagramIdList.isEmpty() || userId == null) {
			return Map.of();
		}

		List<UserInstagramLike> userInstagramLikeList = userInstagramLikeRepository.findByUserIdAndInstagramIdIn(userId, instagramIdList);
		Set<Long> likedInstagramIdList = userInstagramLikeList.stream()
			.map(UserInstagramLike::getInstagramId)
			.collect(Collectors.toSet());

		return instagramIdList.stream()
			.collect(Collectors.toMap(
				instagramId -> instagramId,
                    likedInstagramIdList::contains
			));
	}

	public void deleteAllLikesByInstagramId(Long instagramId) {
		if (instagramId == null) {
			throw new InvalidValueException(ErrorCode.VALIDATION_FAILED);
		}
		userInstagramLikeRepository.deleteAllByInstagramId(instagramId);
	}

	public List<UserInstagramLike> getLikesByUser(Long userId, Long cursor, int size) {
		Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));
		if (cursor == null) {
			return userInstagramLikeRepository.findByUserIdOrderByIdDesc(userId, pageable);
		}
		return userInstagramLikeRepository.findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, pageable);
	}
}