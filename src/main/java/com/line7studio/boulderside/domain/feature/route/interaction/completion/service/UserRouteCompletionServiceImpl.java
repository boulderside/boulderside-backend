package com.line7studio.boulderside.domain.feature.route.interaction.completion.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.line7studio.boulderside.common.exception.DomainException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.feature.route.interaction.completion.entity.UserRouteCompletion;
import com.line7studio.boulderside.domain.feature.route.interaction.completion.repository.UserRouteCompletionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserRouteCompletionServiceImpl implements UserRouteCompletionService {
	private final UserRouteCompletionRepository userRouteCompletionRepository;

	@Override
	public UserRouteCompletion create(Long userId, Long routeId, boolean completed, String memo) {
		userRouteCompletionRepository.findByUserIdAndRouteId(userId, routeId)
			.ifPresent(existing -> {
				throw new DomainException(ErrorCode.ROUTE_COMPLETION_ALREADY_EXISTS);
			});

		UserRouteCompletion completion = UserRouteCompletion.builder()
			.userId(userId)
			.routeId(routeId)
			.completed(completed)
			.memo(memo)
			.build();
		return userRouteCompletionRepository.save(completion);
	}

	@Override
	public UserRouteCompletion update(Long userId, Long routeId, boolean completed, String memo) {
		UserRouteCompletion completion = get(userId, routeId);
		completion.update(completed, memo);
		return completion;
	}

	@Override
	@Transactional(readOnly = true)
	public UserRouteCompletion get(Long userId, Long routeId) {
		return userRouteCompletionRepository.findByUserIdAndRouteId(userId, routeId)
			.orElseThrow(() -> new DomainException(ErrorCode.ROUTE_COMPLETION_NOT_FOUND));
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserRouteCompletion> getAll(Long userId) {
		return userRouteCompletionRepository.findAllByUserId(userId);
	}

	@Override
	public void delete(Long userId, Long routeId) {
		UserRouteCompletion completion = get(userId, routeId);
		userRouteCompletionRepository.delete(completion);
	}
}
