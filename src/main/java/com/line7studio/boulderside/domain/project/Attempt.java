package com.line7studio.boulderside.domain.project;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 프로젝트 시도 Attempt.
 */
@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attempt {

	/** 시도 날짜 */
	@Column(name = "attempted_date", nullable = false)
	private LocalDate attemptedDate;

	/** 해당 날짜 시도 횟수 */
	@Column(name = "attempt_count", nullable = false)
	private Integer attemptCount;
}
