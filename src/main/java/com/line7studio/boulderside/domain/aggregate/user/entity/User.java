package com.line7studio.boulderside.domain.aggregate.user.entity;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.BaseEntity;
import com.line7studio.boulderside.domain.aggregate.user.enums.UserRole;
import com.line7studio.boulderside.domain.aggregate.user.enums.UserSex;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "users")
public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nickname;

	private String phone;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole userRole;

	@Enumerated(EnumType.STRING)
	private UserSex userSex;

	@Enumerated(EnumType.STRING)
	private Level userLevel;

	@Column(length = 2048)
	private String profileImageUrl;

	private String name;

	@Column(nullable = false, unique = true)
	private String email;
}
