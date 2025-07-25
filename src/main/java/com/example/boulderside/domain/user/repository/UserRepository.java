package com.example.boulderside.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.boulderside.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
