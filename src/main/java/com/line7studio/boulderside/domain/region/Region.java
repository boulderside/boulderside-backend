package com.line7studio.boulderside.domain.region;

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
@Table(name = "regions")
public class Region {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 법정동코드 */
	@Column(name = "official_district_code")
	private String officialDistrictCode;

	/** 시/도 */
	@Column(name = "province")
	private String province;

	/** 시/군/구 */
	@Column(name = "city")
	private String city;

	/** 지역코드 */
	@Column(name = "region_code")
	private String regionCode;

	public void update(String officialDistrictCode, String province, String city, String regionCode) {
		this.officialDistrictCode = officialDistrictCode;
		this.province = province;
		this.city = city;
		this.regionCode = regionCode;
	}
}
