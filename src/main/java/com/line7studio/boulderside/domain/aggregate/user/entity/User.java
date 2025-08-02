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
	@Column(name = "id")
	private Long id;

	/** 사용자 닉네임 */
	@Column(name = "nickname", nullable = false)
	private String nickname;

	/** 휴대폰 번호 */
	@Column(name = "phone")
	private String phone;

	/** 사용자 역할 (예: ADMIN, USER 등) */
	@Enumerated(EnumType.STRING)
	@Column(name = "user_role", nullable = false)
	private UserRole userRole;

	/** 성별 */
	@Enumerated(EnumType.STRING)
	@Column(name = "user_sex")
	private UserSex userSex;

	/** 사용자 레벨 (공통 Level enum 사용) */
	@Enumerated(EnumType.STRING)
	@Column(name = "user_level")
	private Level userLevel;

	/** 실제 이름 */
	@Column(name = "name")
	private String name;

	/** 이메일 */
	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "password")
	private String password;
}