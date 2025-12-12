package com.line7studio.boulderside.domain.feature.project.service;

import java.util.List;

import com.line7studio.boulderside.domain.feature.project.entity.Project;
import com.line7studio.boulderside.domain.feature.project.entity.ProjectAttemptHistory;

public interface ProjectService {
	Project create(Long userId, Long routeId, boolean completed, String memo,
		List<ProjectAttemptHistory> attemptHistories);

	Project update(Long userId, Long projectId, boolean completed, String memo,
		List<ProjectAttemptHistory> attemptHistories);

	Project get(Long userId, Long projectId);

	Project getByRoute(Long userId, Long routeId);

	List<Project> getAll(Long userId);

	List<Project> getByUser(Long userId, Long cursor, int size);

	void delete(Long userId, Long projectId);
}
