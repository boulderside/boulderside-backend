package com.example.boulderside.domain.boulder;

import java.util.ArrayList;
import java.util.List;

import com.example.boulderside.domain.BaseEntity;

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
@Table(name = "boulders")
public class Boulder extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 바위 이름 */
	@Column(name = "name", nullable = false)
	private String name;

	/** 바위 설명 */
	@Column(name = "description")
	private String description;

	/** 바위 위치 위도 */
	@Column(name = "latitude", nullable = false)
	private Double latitude;

	/** 바위 위치 경도 */
	@Column(name = "longitude", nullable = false)
	private Double longitude;

	/** 좋아요 수 */
	@Column(name = "like_count", nullable = false)
	private Long likeCount = 0L;

	/** 시/도 */
	@Column(name = "province")
	private String province;

	/** 시/군/구 */
	@Column(name = "city")
	private String city;

	/** 바위 이미지 URL 목록 */
	@ElementCollection
	@CollectionTable(
		name = "boulder_images",
		joinColumns = @JoinColumn(name = "boulder_id")
	)
	@Column(name = "image_url", length = 2048)
	private List<String> imageUrls = new ArrayList<>();
}

