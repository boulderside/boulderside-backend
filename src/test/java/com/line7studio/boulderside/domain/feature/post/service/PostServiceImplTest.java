package com.line7studio.boulderside.domain.feature.post.service;

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

import com.line7studio.boulderside.domain.feature.post.entity.Post;
import com.line7studio.boulderside.domain.feature.post.repository.PostQueryRepository;
import com.line7studio.boulderside.domain.feature.post.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

	@Mock
	private PostRepository postRepository;

	@Mock
	private PostQueryRepository postQueryRepository;

	@InjectMocks
	private PostServiceImpl postService;

	@Test
	void getPostsByUser_withoutCursor_fetchesLatestPosts() {
		List<Post> posts = List.of(Post.builder().id(3L).userId(10L).build());
		when(postRepository.findByUserIdOrderByIdDesc(eq(10L), any(Pageable.class))).thenReturn(posts);

		List<Post> result = postService.getPostsByUser(10L, null, 5);

		assertThat(result).isEqualTo(posts);
		verify(postRepository).findByUserIdOrderByIdDesc(eq(10L), any(Pageable.class));
		verify(postRepository, never()).findByUserIdAndIdLessThanOrderByIdDesc(any(), any(), any());
	}

	@Test
	void getPostsByUser_withCursor_fetchesPreviousPage() {
		List<Post> posts = List.of(Post.builder().id(1L).userId(10L).build());
		when(postRepository.findByUserIdAndIdLessThanOrderByIdDesc(eq(10L), eq(5L), any(Pageable.class)))
			.thenReturn(posts);

		List<Post> result = postService.getPostsByUser(10L, 5L, 5);

		assertThat(result).isEqualTo(posts);
		verify(postRepository).findByUserIdAndIdLessThanOrderByIdDesc(eq(10L), eq(5L), any(Pageable.class));
	}
}
