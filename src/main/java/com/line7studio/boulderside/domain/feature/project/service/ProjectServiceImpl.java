package com.line7studio.boulderside.domain.feature.project.service;

import com.line7studio.boulderside.common.exception.DomainException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.feature.project.entity.Project;
import com.line7studio.boulderside.domain.feature.project.entity.ProjectAttemptHistory;
import com.line7studio.boulderside.domain.feature.project.repository.ProjectRepository;
import com.line7studio.boulderside.domain.feature.route.Route;
import com.line7studio.boulderside.domain.feature.route.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {
	private final ProjectRepository projectRepository;
	private final RouteService routeService;

	@Override
	public Project create(Long userId, Long routeId, boolean completed, String memo,
		List<ProjectAttemptHistory> attemptHistories) {
		projectRepository.findByUserIdAndRouteId(userId, routeId)
			.ifPresent(existing -> {
				throw new DomainException(ErrorCode.ROUTE_COMPLETION_ALREADY_EXISTS);
			});

		Project project = Project.builder()
			.userId(userId)
			.routeId(routeId)
			.completed(completed)
			.memo(memo)
			.attemptHistories(copyAttemptsOrDefault(attemptHistories))
			.build();

		Route route = routeService.getRouteById(routeId);
		Project saved = projectRepository.save(project);
		if (completed) {
			route.incrementClimberCount();
		}
		return saved;
	}

	@Override
	public Project update(Long userId, Long projectId, boolean completed, String memo,
		List<ProjectAttemptHistory> attemptHistories) {
		Project project = get(userId, projectId);
		Long routeId = project.getRouteId();
		boolean wasCompleted = Boolean.TRUE.equals(project.getCompleted());
		if (wasCompleted != completed) {
			Route route = routeService.getRouteById(routeId);
			if (completed) {
				route.incrementClimberCount();
			} else if (wasCompleted) {
				route.decrementClimberCount();
			}
		}
		project.update(completed, memo, copyAttempts(attemptHistories));
		return project;
	}

	@Override
	@Transactional(readOnly = true)
	public Project get(Long userId, Long projectId) {
		return projectRepository.findByIdAndUserId(projectId, userId)
			.orElseThrow(() -> new DomainException(ErrorCode.ROUTE_COMPLETION_NOT_FOUND));
	}

	@Override
	@Transactional(readOnly = true)
	public Project getByRoute(Long userId, Long routeId) {
		return projectRepository.findByUserIdAndRouteId(userId, routeId)
			.orElseThrow(() -> new DomainException(ErrorCode.ROUTE_COMPLETION_NOT_FOUND));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Project> getAll(Long userId) {
		return projectRepository.findAllByUserId(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Project> getByUser(Long userId, Long cursor, int size) {
		Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));
		if (cursor == null) {
			return projectRepository.findByUserIdOrderByIdDesc(userId, pageable);
		}
		return projectRepository.findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, pageable);
	}

	@Override
	public void delete(Long userId, Long projectId) {
		Project project = get(userId, projectId);
		Long routeId = project.getRouteId();
		projectRepository.delete(project);
		if (Boolean.TRUE.equals(project.getCompleted())) {
			routeService.getRouteById(routeId).decrementClimberCount();
		}
	}

	private List<ProjectAttemptHistory> copyAttempts(List<ProjectAttemptHistory> attemptHistories) {
		return attemptHistories == null ? null : new ArrayList<>(attemptHistories);
	}

	private List<ProjectAttemptHistory> copyAttemptsOrDefault(List<ProjectAttemptHistory> attemptHistories) {
		return attemptHistories == null ? new ArrayList<>() : new ArrayList<>(attemptHistories);
	}
}
