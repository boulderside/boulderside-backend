package com.line7studio.boulderside.usecase.project;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.controller.project.request.SessionRequest;
import com.line7studio.boulderside.controller.project.response.ProjectPageResponse;
import com.line7studio.boulderside.controller.project.response.ProjectRecordSummaryResponse;
import com.line7studio.boulderside.controller.project.response.ProjectRecordSummaryResponse.CompletedRouteCountResponse;
import com.line7studio.boulderside.controller.project.response.ProjectResponse;
import com.line7studio.boulderside.domain.completion.Completion;
import com.line7studio.boulderside.domain.completion.service.CompletionService;
import com.line7studio.boulderside.domain.project.Session;
import com.line7studio.boulderside.domain.project.Project;
import com.line7studio.boulderside.domain.project.enums.ProjectSortType;
import com.line7studio.boulderside.domain.project.service.ProjectService;
import com.line7studio.boulderside.domain.route.Route;
import com.line7studio.boulderside.domain.route.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectUseCase {
	private static final int MAX_PAGE_SIZE = 50;

	private final ProjectService projectService;
	private final RouteService routeService;
	private final CompletionService completionService;

	@Transactional(readOnly = true)
	public ProjectResponse getProject(Long userId, Long projectId) {
		Project project = projectService.get(userId, projectId);
		return ProjectResponse.from(project);
	}

	@Transactional(readOnly = true)
	public ProjectResponse getProjectByRoute(Long userId, Long routeId) {
		Project project = projectService.getByRoute(userId, routeId);
		return ProjectResponse.from(project);
	}

	public ProjectResponse createProject(Long userId, Long routeId, boolean completed, String memo,
		List<SessionRequest> attempts) {
		Project project = projectService.create(
			userId, routeId, completed, memo, mapAttempts(attempts));
		return ProjectResponse.from(project);
	}

	public ProjectResponse updateProject(Long userId, Long projectId, boolean completed, String memo,
		List<SessionRequest> attempts) {
		Project project = projectService.update(
			userId, projectId, completed, memo, mapAttempts(attempts));
		return ProjectResponse.from(project);
	}

	public void deleteProject(Long userId, Long projectId) {
		projectService.delete(userId, projectId);
	}

	@Transactional(readOnly = true)
	public ProjectRecordSummaryResponse getProjectSummary(Long userId) {
		List<Completion> completions = completionService.getAll(userId);
		List<Project> projects = projectService.getAll(userId);

		List<Project> completedProjects = projects.stream()
			.filter(ProjectUseCase::isCompleted)
			.toList();

		long ongoingCount = projects.size() - completedProjects.size();

		List<Long> routeIds = Stream.concat(
				completions.stream().map(Completion::getRouteId),
				completedProjects.stream().map(Project::getRouteId)
			)
			.distinct()
			.toList();
		Map<Long, Route> routeMap = routeService.getRoutesByIds(routeIds).stream()
			.collect(Collectors.toMap(Route::getId, Function.identity()));

		Map<LocalDate, Long> completionCountByDate = completions.stream()
			.map(Completion::getCompletedDate)
			.filter(Objects::nonNull)
			.collect(Collectors.groupingBy(date -> date, TreeMap::new, Collectors.counting()));

		List<CompletedRouteCountResponse> completedRoutes = new ArrayList<>();
		long cumulativeCount = 0;
		for (Map.Entry<LocalDate, Long> entry : completionCountByDate.entrySet()) {
			cumulativeCount += entry.getValue();
			completedRoutes.add(CompletedRouteCountResponse.of(entry.getKey(), cumulativeCount));
		}

		Map<Level, List<Long>> completionIdsByLevel = new EnumMap<>(Level.class);
		for (Level level : Level.values()) {
			completionIdsByLevel.put(level, new ArrayList<>());
		}
		completions.forEach(completion -> {
			Route route = routeMap.get(completion.getRouteId());
			if (route == null || route.getRouteLevel() == null) {
				return;
			}
			completionIdsByLevel.get(route.getRouteLevel()).add(completion.getId());
		});

		long completedCount = completions.size();

		Level highestLevel = completions.stream()
			.map(completion -> {
				Route route = routeMap.get(completion.getRouteId());
				return route != null ? route.getRouteLevel() : null;
			})
			.filter(Objects::nonNull)
			.max(Comparator.comparingInt(Level::ordinal))
			.orElse(null);

		return new ProjectRecordSummaryResponse(
			highestLevel,
			completedCount,
			ongoingCount,
			completedRoutes,
			completionIdsByLevel
		);
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

		List<ProjectResponse> content = projects.stream()
			.map(ProjectResponse::from)
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

	private List<Session> mapAttempts(List<SessionRequest> attempts) {
		if (attempts == null) {
			return List.of();
		}
		return attempts.stream()
			.map(history -> Session.builder()
				.sessiondDate(history.sessionDate())
				.sessionCount(history.sessionCount())
				.build())
			.toList();
	}

	private static boolean isCompleted(Project project) {
		return Boolean.TRUE.equals(project.getCompleted());
	}

	private int normalizeSize(int size) {
		if (size <= 0) {
			return 10;
		}
		return Math.min(size, MAX_PAGE_SIZE);
	}
}
