package com.line7studio.boulderside.usecase.completion;

import com.line7studio.boulderside.controller.completion.request.CompletionRequest;
import com.line7studio.boulderside.controller.completion.response.CompletionPageResponse;
import com.line7studio.boulderside.controller.completion.response.CompletionResponse;
import com.line7studio.boulderside.domain.completion.Completion;
import com.line7studio.boulderside.domain.completion.service.CompletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CompletionUseCase {
	private static final int MAX_PAGE_SIZE = 50;

	private final CompletionService completionService;

	@Transactional(readOnly = true)
	public CompletionResponse getCompletion(Long userId, Long completionId) {
		Completion completion = completionService.get(userId, completionId);
		return CompletionResponse.from(completion);
	}

	@Transactional(readOnly = true)
	public CompletionResponse getCompletionByRoute(Long userId, Long routeId) {
		Completion completion = completionService.getByRoute(userId, routeId);
		return CompletionResponse.from(completion);
	}

	public CompletionResponse createCompletion(Long userId, CompletionRequest request) {
		Completion completion = completionService.create(
			userId, request.routeId(), request.completedDate(), request.memo());
		return CompletionResponse.from(completion);
	}

	public CompletionResponse updateCompletion(Long userId, Long completionId, CompletionRequest request) {
		Completion completion = completionService.update(
			userId, completionId, request.routeId(), request.completedDate(), request.memo());
		return CompletionResponse.from(completion);
	}

	public void deleteCompletion(Long userId, Long completionId) {
		completionService.delete(userId, completionId);
	}

	@Transactional(readOnly = true)
	public CompletionPageResponse getCompletionPage(Long userId, Long cursor, int size) {
		int pageSize = normalizeSize(size);
		List<Completion> completions = completionService.getByUser(userId, cursor, pageSize + 1);

		boolean hasNext = completions.size() > pageSize;
		if (hasNext) {
			completions = completions.subList(0, pageSize);
		}
		Long nextCursor = hasNext && !completions.isEmpty() ? completions.getLast().getId() : null;

		List<CompletionResponse> content = completions.stream()
			.map(CompletionResponse::from)
			.toList();

		return CompletionPageResponse.of(content, nextCursor, hasNext, content.size());
	}

	private int normalizeSize(int size) {
		if (size <= 0) {
			return 10;
		}
		return Math.min(size, MAX_PAGE_SIZE);
	}
}
