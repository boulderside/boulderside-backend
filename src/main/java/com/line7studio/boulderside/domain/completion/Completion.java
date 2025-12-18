package com.line7studio.boulderside.domain.completion;

import com.line7studio.boulderside.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "completions", uniqueConstraints = {
	@UniqueConstraint(name = "uk_completion_user_route", columnNames = { "user_id", "route_id" })
})
public class Completion extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 연관 루트 (FK) */
	@Column(name = "route_id", nullable = false)
	private Long routeId;

	/** 연관 사용자 (FK) */
	@Column(name = "user_id", nullable = false)
	private Long userId;

	/** 완등 날짜 */
	@Column(name = "completed_date", nullable = false)
	private LocalDate completedDate;

	/** 메모 */
	@Column(name = "memo")
	private String memo;

	public void update(LocalDate completedDate, String memo) {
		this.completedDate = completedDate;
		this.memo = memo;
	}
}
