package com.line7studio.boulderside.domain.completion.service;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.exception.InvalidValueException;
import com.line7studio.boulderside.domain.completion.Completion;
import com.line7studio.boulderside.domain.completion.repository.CompletionRepository;
import com.line7studio.boulderside.domain.route.Route;
import com.line7studio.boulderside.domain.route.service.RouteService;
import com.line7studio.boulderside.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CompletionService {
	private final CompletionRepository completionRepository;
	private final RouteService routeService;
	private final ProjectRepository projectRepository;

	public Completion create(Long userId, Long routeId, LocalDate completedDate, String memo) {
		if (completionRepository.existsByUserIdAndRouteId(userId, routeId)) {
			throw new BusinessException(ErrorCode.ROUTE_COMPLETION_ALREADY_EXISTS);
		}
//		if (projectRepository.findByUserIdAndRouteId(userId, routeId).isPresent()) {
//			throw new BusinessException(ErrorCode.ROUTE_COMPLETION_ALREADY_EXISTS);
//		}
		return createInternal(userId, routeId, completedDate, memo, true);
	}

	public Completion update(Long userId, Long completionId, Long routeId, LocalDate completedDate, String memo) {
		Completion completion = get(userId, completionId);
		if (!completion.getRouteId().equals(routeId)) {
			throw new InvalidValueException(ErrorCode.CONSTRAINT_VIOLATION);
		}
		completion.update(completedDate, memo);
		return completion;
	}

	public Completion get(Long userId, Long completionId) {
		return completionRepository.findByIdAndUserId(completionId, userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ROUTE_COMPLETION_NOT_FOUND));
	}

	public Completion getByRoute(Long userId, Long routeId) {
		return completionRepository.findByUserIdAndRouteId(userId, routeId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ROUTE_COMPLETION_NOT_FOUND));
	}

	public List<Completion> getByDate(Long userId, LocalDate date) {
		return completionRepository.findAllByUserIdAndCompletedDate(userId, date);
	}

	public List<Completion> getByLevel(Long userId, Level level) {
		return completionRepository.findAllByUserIdAndRouteLevel(userId, level);
	}

	public List<Completion> getAll(Long userId) {
		return completionRepository.findAllByUserId(userId);
	}

	public List<Completion> getByUser(Long userId, Long cursor, int size) {
		Pageable pageable = PageRequest.of(0, size,
			Sort.by(Sort.Direction.DESC, "completedDate").and(Sort.by(Sort.Direction.DESC, "id")));
		if (cursor == null) {
			return completionRepository.findByUserId(userId, pageable);
		}
		return completionRepository.findByUserIdAndIdLessThan(userId, cursor, pageable);
	}

	public void delete(Long userId, Long completionId) {
		Completion completion = get(userId, completionId);
		deleteInternal(completion, true, true);
	}

	public void syncFromProject(Long userId, Long routeId, LocalDate completedDate, String memo) {
		LocalDate finalCompletedDate = completedDate == null ? LocalDate.now() : completedDate;
		String finalMemo = memo;
		completionRepository.findByUserIdAndRouteId(userId, routeId)
			.ifPresentOrElse(existing -> existing.update(finalCompletedDate, finalMemo),
				() -> createInternal(userId, routeId, finalCompletedDate, finalMemo, false));
	}

	public void deleteByUserAndRoute(Long userId, Long routeId, boolean adjustRouteCount, boolean resetProjectStatus) {
		completionRepository.findByUserIdAndRouteId(userId, routeId)
			.ifPresent(completion -> deleteInternal(completion, adjustRouteCount, resetProjectStatus));
	}

	private Completion createInternal(Long userId, Long routeId, LocalDate completedDate, String memo, boolean adjustRouteCount) {
		if (completedDate == null) {
			completedDate = LocalDate.now();
		}
		Completion completion = Completion.builder()
			.userId(userId)
			.routeId(routeId)
			.completedDate(completedDate)
			.memo(memo)
			.build();
		Completion saved = completionRepository.save(completion);
		if (adjustRouteCount) {
			Route route = routeService.getById(routeId);
			route.incrementClimberCount();
		}
		return saved;
	}

	private void deleteInternal(Completion completion, boolean adjustRouteCount, boolean resetProjectStatus) {
		Long routeId = completion.getRouteId();
		Long userId = completion.getUserId();
		completionRepository.delete(completion);
		if (adjustRouteCount) {
			routeService.getById(routeId).decrementClimberCount();
		}
		if (resetProjectStatus) {
			projectRepository.findByUserIdAndRouteId(userId, routeId)
				.ifPresent(project -> {
					if (Boolean.TRUE.equals(project.getCompleted())) {
						project.update(false, project.getMemo(), project.getSessions());
					}
				});
		}
	}
}
