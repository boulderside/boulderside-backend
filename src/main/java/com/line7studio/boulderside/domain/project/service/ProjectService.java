package com.line7studio.boulderside.domain.project.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.project.Project;
import com.line7studio.boulderside.domain.project.Session;
import com.line7studio.boulderside.domain.project.enums.ProjectSortType;
import com.line7studio.boulderside.domain.project.repository.ProjectRepository;
import com.line7studio.boulderside.domain.route.Route;
import com.line7studio.boulderside.domain.route.service.RouteService;
import com.line7studio.boulderside.domain.completion.repository.CompletionRepository;
import com.line7studio.boulderside.domain.completion.service.CompletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {
	private final ProjectRepository projectRepository;
	private final RouteService routeService;
	private final CompletionRepository completionRepository;
	private final CompletionService completionService;

	public Project create(Long userId, Long routeId, boolean completed, String memo,
		List<Session> sessions) {
		projectRepository.findByUserIdAndRouteId(userId, routeId)
			.ifPresent(existing -> {
				throw new BusinessException(ErrorCode.ROUTE_COMPLETION_ALREADY_EXISTS);
			});
		if (completionRepository.existsByUserIdAndRouteId(userId, routeId)) {
			throw new BusinessException(ErrorCode.ROUTE_COMPLETION_ALREADY_EXISTS);
		}

		Project project = Project.builder()
			.userId(userId)
			.routeId(routeId)
			.completed(completed)
			.memo(memo)
			.sessions(copyAttemptsOrDefault(sessions))
			.build();

		Route route = routeService.getById(routeId);
		Project saved = projectRepository.save(project);
		if (completed) {
			route.incrementClimberCount();
			LocalDate completedDate = resolveCompletedDate(saved);
			completionService.syncFromProject(userId, routeId, completedDate, memo);
		}
		return saved;
	}

	public Project update(Long userId, Long projectId, boolean completed, String memo,
		List<Session> sessions) {
		Project project = get(userId, projectId);
		Long routeId = project.getRouteId();
		boolean wasCompleted = Boolean.TRUE.equals(project.getCompleted());
		if (wasCompleted != completed) {
			Route route = routeService.getById(routeId);
			if (completed) {
				route.incrementClimberCount();
			} else if (wasCompleted) {
				route.decrementClimberCount();
			}
		}
		project.update(completed, memo, copyAttempts(sessions));
		if (!wasCompleted && completed) {
			LocalDate completedDate = resolveCompletedDate(project);
			completionService.syncFromProject(userId, routeId, completedDate, memo);
		} else if (wasCompleted && !completed) {
			completionService.deleteByUserAndRoute(userId, routeId, false, false);
		}
		return project;
	}

	@Transactional(readOnly = true)
	public Project get(Long userId, Long projectId) {
		return projectRepository.findByIdAndUserId(projectId, userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ROUTE_COMPLETION_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public Project getByRoute(Long userId, Long routeId) {
		return projectRepository.findByUserIdAndRouteId(userId, routeId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ROUTE_COMPLETION_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public List<Project> getAll(Long userId) {
		return projectRepository.findAllByUserId(userId);
	}

	@Transactional(readOnly = true)
	public List<Project> getByUser(Long userId, Boolean isCompleted, Long cursor, int size, ProjectSortType sortType) {
		String sortField = getSortField(sortType);
		Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, sortField).and(Sort.by(Sort.Direction.DESC, "id")));
		if (isCompleted == null) {
			if (cursor == null) {
				return projectRepository.findByUserId(userId, pageable);
			}
			return projectRepository.findByUserIdAndIdLessThan(userId, cursor, pageable);
		}
		if (cursor == null) {
			return projectRepository.findByUserIdAndCompleted(userId, isCompleted, pageable);
		}
		return projectRepository.findByUserIdAndCompletedAndIdLessThan(userId, isCompleted, cursor,
			pageable);
	}

	private String getSortField(ProjectSortType sortType) {
		return switch (sortType) {
			case LATEST_CREATED -> "createdAt";
			case LATEST_UPDATED -> "updatedAt";
		};
	}

	public void delete(Long userId, Long projectId) {
		Project project = get(userId, projectId);
		Long routeId = project.getRouteId();
		projectRepository.delete(project);
		if (Boolean.TRUE.equals(project.getCompleted())) {
			routeService.getById(routeId).decrementClimberCount();
			completionService.deleteByUserAndRoute(userId, routeId, false, false);
		}
	}

	private List<Session> copyAttempts(List<Session> sessions) {
		return sessions == null ? null : new ArrayList<>(sessions);
	}

	private List<Session> copyAttemptsOrDefault(List<Session> sessions) {
		return sessions == null ? new ArrayList<>() : new ArrayList<>(sessions);
	}

	private LocalDate resolveCompletedDate(Project project) {
		if (project.getSessions() != null && !project.getSessions().isEmpty()) {
			return project.getSessions().stream()
				.map(Session::getSessiondDate)
				.filter(date -> date != null)
				.max(LocalDate::compareTo)
				.orElse(LocalDate.now());
		}
		return LocalDate.now();
	}
}
