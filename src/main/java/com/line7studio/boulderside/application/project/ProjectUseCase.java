package com.line7studio.boulderside.application.project;

import com.line7studio.boulderside.controller.project.request.ProjectAttemptHistoryRequest;
import com.line7studio.boulderside.controller.project.response.ProjectPageResponse;
import com.line7studio.boulderside.controller.project.response.ProjectResponse;
import com.line7studio.boulderside.domain.feature.project.entity.Project;
import com.line7studio.boulderside.domain.feature.project.entity.ProjectAttemptHistory;
import com.line7studio.boulderside.domain.feature.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectUseCase {
	private static final int MAX_PAGE_SIZE = 50;

	private final ProjectService projectService;

	@Transactional(readOnly = true)
	public ProjectResponse getProject(Long userId, Long projectId) {
		return ProjectResponse.from(projectService.get(userId, projectId));
	}

	@Transactional(readOnly = true)
	public ProjectResponse getProjectByRoute(Long userId, Long routeId) {
		return ProjectResponse.from(projectService.getByRoute(userId, routeId));
	}

	public ProjectResponse createProject(Long userId, Long routeId, boolean completed, String memo,
		List<ProjectAttemptHistoryRequest> attemptHistories) {
		return ProjectResponse.from(projectService.create(
			userId, routeId, completed, memo, mapAttemptHistories(attemptHistories)));
	}

	public ProjectResponse updateProject(Long userId, Long projectId, boolean completed, String memo,
		List<ProjectAttemptHistoryRequest> attemptHistories) {
		return ProjectResponse.from(projectService.update(
			userId, projectId, completed, memo, mapAttemptHistories(attemptHistories)));
	}

	public void deleteProject(Long userId, Long projectId) {
		projectService.delete(userId, projectId);
	}

	@Transactional(readOnly = true)
	public ProjectPageResponse getProjectPage(Long userId, Long cursor, int size) {
		int pageSize = normalizeSize(size);
		List<Project> projects = projectService.getByUser(userId, cursor, pageSize + 1);

		boolean hasNext = projects.size() > pageSize;
		if (hasNext) {
			projects = projects.subList(0, pageSize);
		}
		Long nextCursor = hasNext && !projects.isEmpty() ? projects.getLast().getId() : null;

		List<ProjectResponse> content = projects.stream()
			.map(ProjectResponse::from)
			.toList();

		return ProjectPageResponse.of(content, nextCursor, hasNext, content.size());
	}

	private List<ProjectAttemptHistory> mapAttemptHistories(List<ProjectAttemptHistoryRequest> attemptHistories) {
		if (attemptHistories == null) {
			return List.of();
		}
		return attemptHistories.stream()
			.map(history -> ProjectAttemptHistory.builder()
				.attemptedDate(history.getAttemptedDate())
				.attemptCount(history.getAttemptCount())
				.build())
			.toList();
	}

	private int normalizeSize(int size) {
		if (size <= 0) {
			return 10;
		}
		return Math.min(size, MAX_PAGE_SIZE);
	}
}
