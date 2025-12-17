package com.line7studio.boulderside.domain.sector;

import com.line7studio.boulderside.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sectors")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Sector extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 섹터 이름 */
	@Column(name = "sector_name", nullable = false)
	private String sectorName;

	/** 구역 */
	@Column(name = "area_code", nullable = false)
	private String areaCode;

	public void update(String sectorName, String areaCode) {
		this.sectorName = sectorName;
		this.areaCode = areaCode;
	}
}
