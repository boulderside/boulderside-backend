package com.line7studio.boulderside.domain.association.like.service;

import com.line7studio.boulderside.domain.association.like.entity.UserBoulderLike;

public interface UserBoulderLikeService {
	boolean toggle(UserBoulderLike userBoulderLike);

	long getCountByBoulderId(Long boulderId);

	void deleteAllByBoulderId(Long boulderId);
}
