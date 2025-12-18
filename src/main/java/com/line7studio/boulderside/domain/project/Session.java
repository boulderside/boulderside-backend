package com.line7studio.boulderside.domain.project;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 프로젝트 시도 Attempt.
 */
@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session {

	/** 시도 날짜 */
	@Column(name = "session_date", nullable = false)
	private LocalDate sessiondDate;

	/** 해당 날짜 시도 횟수 */
	@Column(name = "session_count", nullable = false)
	private Integer sessionCount;
}
