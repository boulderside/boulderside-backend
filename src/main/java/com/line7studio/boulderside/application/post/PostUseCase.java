package com.line7studio.boulderside.application.post;

import com.line7studio.boulderside.common.dto.UserInfo;
import com.line7studio.boulderside.controller.post.request.CreatePostRequest;
import com.line7studio.boulderside.controller.post.request.UpdatePostRequest;
import com.line7studio.boulderside.controller.post.response.PostPageResponse;
import com.line7studio.boulderside.controller.post.response.PostResponse;
import com.line7studio.boulderside.domain.feature.comment.enums.CommentDomainType;
import com.line7studio.boulderside.domain.feature.comment.service.CommentService;
import com.line7studio.boulderside.domain.feature.post.entity.Post;
import com.line7studio.boulderside.domain.feature.post.enums.PostSortType;
import com.line7studio.boulderside.domain.feature.post.enums.PostType;
import com.line7studio.boulderside.domain.feature.post.service.PostService;
import com.line7studio.boulderside.domain.feature.user.entity.User;
import com.line7studio.boulderside.domain.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostUseCase {
	private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;

	public PostPageResponse getPostPage(Long cursor, String subCursor, int size, PostType postType, PostSortType sortType, Long userId) {
        // 게시글 조회
		List<Post> postList = postService.getPostsWithCursor(cursor, subCursor, size + 1, postType, sortType);
		
		boolean hasNext = postList.size() > size;
		if (hasNext) {
            postList = postList.subList(0, size);
		}

        // 다음 커서 위치 지정
        Long nextCursor = null;
        String nextSubCursor = null;
        if (hasNext && !postList.isEmpty()) {
            Post lastPost = postList.getLast();
            nextCursor = lastPost.getId();
            nextSubCursor = getNextSubCursor(lastPost, sortType);
        }

        // 게시글 id 취합
        List<Long> userIdList = postList.stream()
                .map(Post::getUserId)
                .distinct()
                .toList();

        // 게시글마다 작성자 매핑
        Map<Long, User> userMap = userService.findAllById(userIdList).stream()
                .collect(Collectors.toMap(User::getId, u -> u));

        // 게시글 id 취합
        List<Long> postIdList = postList.stream()
                .map(Post::getId)
                .toList();

        // 게시글별 댓글 수 조회
        Map<Long, Long> commentCountMap = commentService.countCommentsByDomainIdsAndCommentDomainTypeType(postIdList, CommentDomainType.POST);

        // 게시글 - 유저 정보로 응답 작성
        List<PostResponse> postResponses = postList.stream()
                .map(post -> {
                    User user = userMap.get(post.getUserId());
                    UserInfo userInfo = UserInfo.from(user);
                    Boolean isMine = post.getUserId().equals(userId);
                    Long commentCount = commentCountMap.getOrDefault(post.getId(), 0L);
                    return PostResponse.of(post, userInfo, isMine, commentCount);
                })
                .toList();

		return PostPageResponse.of(postResponses, nextCursor, nextSubCursor, hasNext, postList.size());
	}

	public PostPageResponse getMyPosts(Long cursor, int size, Long userId) {
		List<Post> postList = postService.getPostsByUser(userId, cursor, size + 1);

		boolean hasNext = postList.size() > size;
		if (hasNext) {
			postList = postList.subList(0, size);
		}

		Long nextCursor = null;
		if (hasNext && !postList.isEmpty()) {
			nextCursor = postList.getLast().getId();
		}

		User user = userService.getUserById(userId);
		UserInfo userInfo = UserInfo.from(user);

		List<Long> postIdList = postList.stream()
			.map(Post::getId)
			.toList();

		Map<Long, Long> commentCountMap = commentService.countCommentsByDomainIdsAndCommentDomainTypeType(
			postIdList, CommentDomainType.POST);

		List<PostResponse> postResponses = postList.stream()
			.map(post -> {
				Long commentCount = commentCountMap.getOrDefault(post.getId(), 0L);
				return PostResponse.of(post, userInfo, true, commentCount);
			})
			.toList();

		return PostPageResponse.of(postResponses, nextCursor, null, hasNext, postList.size());
	}

	private String getNextSubCursor(Post post, PostSortType sortType) {
		return switch (sortType) {
            case LATEST_CREATED -> post.getCreatedAt().toString();
			case MOST_VIEWED -> post.getViewCount().toString();
            case NEAREST_MEETING_DATE -> post.getMeetingDate() != null ? 
				post.getMeetingDate().toString() : null;
		};
	}

    @Transactional
	public PostResponse getPostById(Long postId, Long userId) {
        Post post = postService.getPostById(postId);
		post.incrementViewCount();
        User user = userService.getUserById(post.getUserId());
        UserInfo userInfo = UserInfo.from(user);

        Boolean isMine = post.getUserId().equals(userId);

        Long commentCount = commentService.countCommentsByDomainIdAndCommentDomainType(postId, CommentDomainType.POST);

		return PostResponse.of(post, userInfo, isMine, commentCount);
	}

    @Transactional
	public PostResponse createPost(CreatePostRequest request, Long userId) {
        User user = userService.getUserById(userId);
        UserInfo userInfo = UserInfo.from(user);

        Post savedPost = postService.createPost(
                user.getId(),
                request.getTitle(),
                request.getContent(),
                request.getPostType(),
                request.getMeetingDate()
        );

		return PostResponse.of(savedPost, userInfo, true, 0L);
	}

    @Transactional
	public PostResponse updatePost(Long postId, UpdatePostRequest request, Long userId) {
        User user = userService.getUserById(userId);
        UserInfo userInfo = UserInfo.from(user);

        Post post = postService.updatePost(
                postId,
                request.getTitle(),
                request.getContent(),
                request.getPostType(),
                request.getMeetingDate(),
                user.getId()
        );

        Boolean isMine = post.getUserId().equals(userId);
        Long commentCount = commentService.countCommentsByDomainIdAndCommentDomainType(postId, CommentDomainType.POST);

        return PostResponse.of(post, userInfo, isMine, commentCount);
    }

    @Transactional
	public void deletePost(Long postId, Long userId) {
        User user = userService.getUserById(userId);
        postService.deletePost(postId, user.getId());
	}
}
