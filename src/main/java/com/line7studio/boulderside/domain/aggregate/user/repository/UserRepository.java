package com.line7studio.boulderside.domain.aggregate.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.line7studio.boulderside.domain.aggregate.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
