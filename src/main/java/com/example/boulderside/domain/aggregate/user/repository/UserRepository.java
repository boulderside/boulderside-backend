package com.example.boulderside.domain.aggregate.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.boulderside.domain.aggregate.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
