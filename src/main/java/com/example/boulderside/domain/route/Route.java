package com.example.boulderside.domain.route;

import com.example.boulderside.common.enums.Level;

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
@Table(name = "routes")
public class Route {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 연관 바위 ID (FK) */
	@Column(name = "boulder_id", nullable = false)
	private Long boulderId;

	/** 루트 이름 */
	@Column(name = "name")
	private String name;

	/** 난이도 레벨 */
	@Column(name = "route_level")
	private Level routeLevel;
}
