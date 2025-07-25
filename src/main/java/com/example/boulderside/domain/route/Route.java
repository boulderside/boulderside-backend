package com.example.boulderside.domain.route;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
	@Column(name = "level")
	private Integer level;

	/** 루트 이미지 URL 목록 */
	@ElementCollection
	@CollectionTable(
		name = "route_images",
		joinColumns = @JoinColumn(name = "route_id")
	)
	@Column(name = "image_url", length = 2048)
	private List<String> imageUrls = new ArrayList<>();
}
