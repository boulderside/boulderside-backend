package com.line7studio.boulderside.domain.feature.boulder.interaction.like.service;

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

import com.line7studio.boulderside.domain.feature.boulder.interaction.like.entity.UserBoulderLike;
import com.line7studio.boulderside.domain.feature.boulder.interaction.like.repository.UserBoulderLikeRepository;

@ExtendWith(MockitoExtension.class)
class UserBoulderLikeServiceImplTest {

	@Mock
	private UserBoulderLikeRepository userBoulderLikeRepository;

	@InjectMocks
	private UserBoulderLikeServiceImpl service;

	@Test
	void getLikesByUser_withoutCursor_fetchesLatest() {
		List<UserBoulderLike> likes = List.of(UserBoulderLike.builder().id(5L).userId(1L).boulderId(10L).build());
		when(userBoulderLikeRepository.findByUserIdOrderByIdDesc(eq(1L), any(Pageable.class))).thenReturn(likes);

		List<UserBoulderLike> result = service.getLikesByUser(1L, null, 10);

		assertThat(result).isEqualTo(likes);
		verify(userBoulderLikeRepository).findByUserIdOrderByIdDesc(eq(1L), any(Pageable.class));
		verify(userBoulderLikeRepository, never()).findByUserIdAndIdLessThanOrderByIdDesc(any(), any(), any());
	}

	@Test
	void getLikesByUser_withCursor_fetchesPreviousPage() {
		List<UserBoulderLike> likes = List.of(UserBoulderLike.builder().id(3L).userId(1L).boulderId(11L).build());
		when(userBoulderLikeRepository.findByUserIdAndIdLessThanOrderByIdDesc(eq(1L), eq(7L), any(Pageable.class)))
			.thenReturn(likes);

		List<UserBoulderLike> result = service.getLikesByUser(1L, 7L, 10);

		assertThat(result).isEqualTo(likes);
		verify(userBoulderLikeRepository).findByUserIdAndIdLessThanOrderByIdDesc(eq(1L), eq(7L), any(Pageable.class));
	}
}
