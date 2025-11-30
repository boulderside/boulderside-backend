package com.line7studio.boulderside.domain.feature.route.interaction.like.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import com.line7studio.boulderside.domain.feature.route.interaction.like.entity.UserRouteLike;
import com.line7studio.boulderside.domain.feature.route.interaction.like.repository.UserRouteLikeRepository;

@ExtendWith(MockitoExtension.class)
class UserRouteLikeServiceImplTest {

	@Mock
	private UserRouteLikeRepository userRouteLikeRepository;

	@InjectMocks
	private UserRouteLikeServiceImpl service;

	@Test
	void getLikesByUser_withoutCursor_fetchesLatest() {
		List<UserRouteLike> likes = List.of(UserRouteLike.builder().id(4L).userId(2L).routeId(100L).build());
		when(userRouteLikeRepository.findByUserIdOrderByIdDesc(eq(2L), any(Pageable.class))).thenReturn(likes);

		List<UserRouteLike> result = service.getLikesByUser(2L, null, 5);

		assertThat(result).isEqualTo(likes);
		verify(userRouteLikeRepository).findByUserIdOrderByIdDesc(eq(2L), any(Pageable.class));
		verify(userRouteLikeRepository, never()).findByUserIdAndIdLessThanOrderByIdDesc(any(), any(), any());
	}

	@Test
	void getLikesByUser_withCursor_fetchesPreviousPage() {
		List<UserRouteLike> likes = List.of(UserRouteLike.builder().id(1L).userId(2L).routeId(101L).build());
		when(userRouteLikeRepository.findByUserIdAndIdLessThanOrderByIdDesc(eq(2L), eq(9L), any(Pageable.class)))
			.thenReturn(likes);

		List<UserRouteLike> result = service.getLikesByUser(2L, 9L, 5);

		assertThat(result).isEqualTo(likes);
		verify(userRouteLikeRepository).findByUserIdAndIdLessThanOrderByIdDesc(eq(2L), eq(9L), any(Pageable.class));
	}
}
