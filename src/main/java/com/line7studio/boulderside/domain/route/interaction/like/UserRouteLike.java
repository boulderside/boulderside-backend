package com.line7studio.boulderside.domain.route.interaction.like;

import com.line7studio.boulderside.domain.BaseEntity;

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
@Table(name = "user_route_likes")
public class UserRouteLike extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 연관 루트 ID (FK) */
	@Column(name = "route_id", nullable = false)
	private Long routeId;

	/** 연관 사용자 ID (FK) */
	@Column(name = "user_id", nullable = false)
	private Long userId;
}
