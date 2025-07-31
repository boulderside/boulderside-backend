package com.example.boulderside.domain.association.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.boulderside.domain.association.like.entity.UserRouteLike;

public interface UserRouteLikeRepository extends JpaRepository<UserRouteLike, Long> {
}
