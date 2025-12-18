package com.line7studio.boulderside.domain.project.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.project.Project;
import com.line7studio.boulderside.domain.project.Attempt;
import com.line7studio.boulderside.domain.project.enums.ProjectSortType;
import com.line7studio.boulderside.domain.project.repository.ProjectRepository;
import com.line7studio.boulderside.domain.route.Route;
import com.line7studio.boulderside.domain.route.service.RouteService;
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
public class ProjectService {
	private final ProjectRepository projectRepository;
	private final RouteService routeService;

	public Project create(Long userId, Long routeId, boolean completed, String memo,
		List<Attempt> attempts) {
		projectRepository.findByUserIdAndRouteId(userId, routeId)
			.ifPresent(existing -> {
				throw new BusinessException(ErrorCode.ROUTE_COMPLETION_ALREADY_EXISTS);
			});

		Project project = Project.builder()
			.userId(userId)
			.routeId(routeId)
			.completed(completed)
			.memo(memo)
			.attempts(copyAttemptsOrDefault(attempts))
			.build();

		Route route = routeService.getById(routeId);
		Project saved = projectRepository.save(project);
		if (completed) {
			route.incrementClimberCount();
		}
		return saved;
	}

	public Project update(Long userId, Long projectId, boolean completed, String memo,
		List<Attempt> attempts) {
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
		project.update(completed, memo, copyAttempts(attempts));
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
		}
	}

	private List<Attempt> copyAttempts(List<Attempt> attempts) {
		return attempts == null ? null : new ArrayList<>(attempts);
	}

	private List<Attempt> copyAttemptsOrDefault(List<Attempt> attempts) {
		return attempts == null ? new ArrayList<>() : new ArrayList<>(attempts);
	}
}
