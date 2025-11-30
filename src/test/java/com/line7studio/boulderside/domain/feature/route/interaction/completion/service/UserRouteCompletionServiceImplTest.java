package com.line7studio.boulderside.domain.feature.route.interaction.completion.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.line7studio.boulderside.common.exception.DomainException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.feature.route.interaction.completion.entity.UserRouteCompletion;
import com.line7studio.boulderside.domain.feature.route.interaction.completion.repository.UserRouteCompletionRepository;

@ExtendWith(MockitoExtension.class)
class UserRouteCompletionServiceImplTest {

	@Mock
	private UserRouteCompletionRepository userRouteCompletionRepository;

	@InjectMocks
	private UserRouteCompletionServiceImpl service;

	@Test
	void create_ShouldPersistNewCompletion() {
		when(userRouteCompletionRepository.findByUserIdAndRouteId(1L, 100L))
			.thenReturn(Optional.empty());
		when(userRouteCompletionRepository.save(any(UserRouteCompletion.class)))
			.thenAnswer(invocation -> invocation.getArgument(0));

		UserRouteCompletion completion = service.create(1L, 100L, true, "sent it");

		assertThat(completion.getUserId()).isEqualTo(1L);
		assertThat(completion.getRouteId()).isEqualTo(100L);
		assertThat(completion.getCompleted()).isTrue();
		assertThat(completion.getMemo()).isEqualTo("sent it");
		verify(userRouteCompletionRepository).save(any(UserRouteCompletion.class));
	}

	@Test
	void create_Duplicate_ShouldThrow() {
		when(userRouteCompletionRepository.findByUserIdAndRouteId(1L, 100L))
			.thenReturn(Optional.of(UserRouteCompletion.builder().build()));

		DomainException exception = assertThrows(DomainException.class,
			() -> service.create(1L, 100L, true, "memo"));

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.ROUTE_COMPLETION_ALREADY_EXISTS);
		verify(userRouteCompletionRepository, never()).save(any());
	}

	@Test
	void update_ShouldMutateExistingCompletion() {
		UserRouteCompletion completion = UserRouteCompletion.builder()
			.userId(1L)
			.routeId(100L)
			.completed(false)
			.memo("project")
			.build();
		when(userRouteCompletionRepository.findByUserIdAndRouteId(1L, 100L))
			.thenReturn(Optional.of(completion));

		UserRouteCompletion updated = service.update(1L, 100L, true, "sent");

		assertThat(updated.getCompleted()).isTrue();
		assertThat(updated.getMemo()).isEqualTo("sent");
	}

	@Test
	void get_NotFound_ShouldThrow() {
		when(userRouteCompletionRepository.findByUserIdAndRouteId(2L, 200L))
			.thenReturn(Optional.empty());

		DomainException exception = assertThrows(DomainException.class,
			() -> service.get(2L, 200L));

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.ROUTE_COMPLETION_NOT_FOUND);
	}
}
