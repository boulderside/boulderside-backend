package com.line7studio.boulderside.application.project;

import com.line7studio.boulderside.controller.project.request.ProjectAttemptHistoryRequest;
import com.line7studio.boulderside.controller.project.response.ProjectPageResponse;
import com.line7studio.boulderside.controller.project.response.ProjectResponse;
import com.line7studio.boulderside.domain.feature.project.entity.Project;
import com.line7studio.boulderside.domain.feature.project.entity.ProjectAttemptHistory;
import com.line7studio.boulderside.domain.feature.project.enums.ProjectSortType;
import com.line7studio.boulderside.domain.feature.project.service.ProjectService;
import com.line7studio.boulderside.domain.feature.route.entity.Route;
import com.line7studio.boulderside.domain.feature.route.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectUseCase {
	private static final int MAX_PAGE_SIZE = 50;

	private final ProjectService projectService;
	private final RouteService routeService;

	@Transactional(readOnly = true)
	public ProjectResponse getProject(Long userId, Long projectId) {
		Project project = projectService.get(userId, projectId);
		Route route = routeService.getById(project.getRouteId());
		return ProjectResponse.from(project, route);
	}

	@Transactional(readOnly = true)
	public ProjectResponse getProjectByRoute(Long userId, Long routeId) {
		Project project = projectService.getByRoute(userId, routeId);
		Route route = routeService.getById(project.getRouteId());
		return ProjectResponse.from(project, route);
	}

	public ProjectResponse createProject(Long userId, Long routeId, boolean completed, String memo,
		List<ProjectAttemptHistoryRequest> attemptHistories) {
		Project project = projectService.create(
			userId, routeId, completed, memo, mapAttemptHistories(attemptHistories));
		Route route = routeService.getById(project.getRouteId());
		return ProjectResponse.from(project, route);
	}

	public ProjectResponse updateProject(Long userId, Long projectId, boolean completed, String memo,
		List<ProjectAttemptHistoryRequest> attemptHistories) {
		Project project = projectService.update(
			userId, projectId, completed, memo, mapAttemptHistories(attemptHistories));
		Route route = routeService.getById(project.getRouteId());
		return ProjectResponse.from(project, route);
	}

	public void deleteProject(Long userId, Long projectId) {
		projectService.delete(userId, projectId);
	}

	@Transactional(readOnly = true)
	public ProjectPageResponse getProjectPage(Long userId, Boolean isCompleted, Long cursor, int size, ProjectSortType sortType) {
		int pageSize = normalizeSize(size);
		List<Project> projects = projectService.getByUser(userId, isCompleted, cursor, pageSize + 1, sortType);

		boolean hasNext = projects.size() > pageSize;
		if (hasNext) {
			projects = projects.subList(0, pageSize);
		}
		Long nextCursor = hasNext && !projects.isEmpty() ? projects.getLast().getId() : null;

		// N+1 방지: 모든 routeId를 수집하여 한 번에 조회
		List<Long> routeIds = projects.stream()
			.map(Project::getRouteId)
			.distinct()
			.toList();
		Map<Long, Route> routeMap = routeService.getRoutesByIds(routeIds).stream()
			.collect(Collectors.toMap(Route::getId, Function.identity()));

		List<ProjectResponse> content = projects.stream()
			.map(project -> {
				Route route = routeMap.get(project.getRouteId());
				return ProjectResponse.from(project, route);
			})
			.sorted(getSortComparator(sortType))
			.toList();

		return ProjectPageResponse.of(content, nextCursor, hasNext, content.size());
	}

	private Comparator<ProjectResponse> getSortComparator(ProjectSortType sortType) {
		return switch (sortType) {
			case LATEST_CREATED -> Comparator.comparing(ProjectResponse::createdAt).reversed();
			case LATEST_UPDATED -> Comparator.comparing(ProjectResponse::updatedAt).reversed();
		};
	}

	private List<ProjectAttemptHistory> mapAttemptHistories(List<ProjectAttemptHistoryRequest> attemptHistories) {
		if (attemptHistories == null) {
			return List.of();
		}
		return attemptHistories.stream()
			.map(history -> ProjectAttemptHistory.builder()
				.attemptedDate(history.attemptedDate())
				.attemptCount(history.attemptCount())
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
