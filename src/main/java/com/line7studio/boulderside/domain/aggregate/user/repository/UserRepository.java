package com.line7studio.boulderside.domain.aggregate.user.repository;

import com.line7studio.boulderside.domain.aggregate.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	Optional<User> findById(Long id);

	Optional<User> findByPhone(String phoneNumber);


  	List<User> findAllByIdIn(List<Long> userIdList);

	boolean existsByEmail(String email);

	boolean existsByPhone(String phone);
}
