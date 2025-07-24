package com.example.boulderside.feature.like;

import com.example.boulderside.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_boulder_likes")
public class UserBoulderLike extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 연관 바위 ID (FK) */
	@Column(name = "boulder_id", nullable = false)
	private Long boulderId;

	/** 연관 사용자 ID (FK) */
	@Column(name = "user_id", nullable = false)
	private Long userId;
}
