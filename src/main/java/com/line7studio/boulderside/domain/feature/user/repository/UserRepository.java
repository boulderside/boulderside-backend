package com.line7studio.boulderside.domain.feature.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.line7studio.boulderside.domain.feature.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	Optional<User> findByPhone(String phoneNumber);

	List<User> findAllByIdIn(List<Long> userIdList);

	boolean existsByEmail(String email);

	boolean existsByPhone(String phone);
}
