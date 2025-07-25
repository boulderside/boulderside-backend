package com.example.boulderside.feature.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.boulderside.feature.like.entity.UserRouteLike;

public interface UserRouteLikeRepository extends JpaRepository<UserRouteLike, Long> {
}
