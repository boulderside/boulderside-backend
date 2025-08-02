package com.line7studio.boulderside.domain.aggregate.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.line7studio.boulderside.domain.aggregate.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	Optional<User> findById(Long id);
}
