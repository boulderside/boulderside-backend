package com.line7studio.boulderside.domain.user.repository;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.line7studio.boulderside.domain.user.User;
import com.line7studio.boulderside.domain.user.enums.AuthProviderType;

public interface UserRepository extends JpaRepository<User, Long> {
	List<User> findAllByIdIn(List<Long> userIdList);

	boolean existsByNickname(String nickname);

	Optional<User> findByProviderTypeAndProviderUserId(AuthProviderType providerType, String providerUserId);
}
