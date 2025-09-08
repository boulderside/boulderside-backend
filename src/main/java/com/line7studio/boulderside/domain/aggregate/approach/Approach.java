package com.line7studio.boulderside.domain.aggregate.approach;

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
@Table(name = "approaches")
public class Approach extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 연관 바위 ID (FK) */
	@Column(name = "boulder_id", nullable = false)
	private Long boulderId;

	/** 진입로 인덱스 */
	@Column(name = "order_index")
	private Integer orderIndex;

	/** 대중교통 정보 */
	@Column(name = "transport_info")
	private String transportInfo;

	/** 자차 정보 */
	@Column(name = "parking_info")
	private String parkingInfo;

	/** 이동 시간 (분) */
	@Column(name = "duration")
	private Integer duration;

	/** 팁 */
	@Column(name = "tip")
	private String tip;
}
