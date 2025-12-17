package com.line7studio.boulderside.domain.project;

import com.line7studio.boulderside.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projects")
public class Project extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 연관 루트 (FK) */
    @Column(name = "route_id", nullable = false)
	private Long routeId;

	/** 연관 사용자 (FK) */
    @Column(name = "user_id", nullable = false)
	private Long userId;

	/** 완료 여부 */
	@Column(name = "is_completed")
	@Default
	private Boolean completed = Boolean.FALSE;

	/** 메모 */
	@Column(name = "memo")
	private String memo;

	/** 프로젝트 시도 기록 */
	@ElementCollection
	@CollectionTable(name = "project_attempts", joinColumns = @JoinColumn(name = "project_id"))
	@Default
	private List<ProjectAttemptHistory> attemptHistories = new ArrayList<>();

	public void update(boolean completed, String memo, List<ProjectAttemptHistory> attemptHistories) {
		this.completed = completed;
		this.memo = memo;
		if (attemptHistories != null) {
			this.attemptHistories.clear();
			this.attemptHistories.addAll(attemptHistories);
		}
	}
}
