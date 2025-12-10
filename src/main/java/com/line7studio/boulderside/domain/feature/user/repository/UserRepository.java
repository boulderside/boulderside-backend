package com.line7studio.boulderside.domain.feature.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.line7studio.boulderside.domain.feature.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	List<User> findAllByIdIn(List<Long> userIdList);

	boolean existsByNickname(String nickname);
}
