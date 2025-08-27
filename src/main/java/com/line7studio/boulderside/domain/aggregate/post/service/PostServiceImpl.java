package com.line7studio.boulderside.domain.aggregate.post.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.aggregate.post.entity.Post;
import com.line7studio.boulderside.domain.aggregate.post.enums.PostSortType;
import com.line7studio.boulderside.domain.aggregate.post.enums.PostType;
import com.line7studio.boulderside.domain.aggregate.post.repository.PostQueryRepository;
import com.line7studio.boulderside.domain.aggregate.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
	private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;

	@Override
	public Post getPostById(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
	}

	@Override
	public List<Post> getPostsWithCursor(Long cursor, String subCursor, int size, PostType postType, PostSortType postSortType) {
        if (postSortType == null) {
            throw new BusinessException(ErrorCode.NOT_SUPPORT_SORT_TYPE);
        }

        if (PostSortType.NEAREST_MEETING_DATE.equals(postSortType) && PostType.BOARD.equals(postType)) {
            throw new BusinessException(ErrorCode.NOT_SUPPORT_SORT_TYPE);
        }

        return postQueryRepository.findPostsWithCursor(cursor, subCursor, size, postType, postSortType);
	}

    @Override
    public Post createPost(Long userId, String title, String content, PostType postType, LocalDate meetingDate) {
        validate(postType, meetingDate);

        Post post = Post.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .postType(postType)
                .viewCount(0L)
                .meetingDate(meetingDate)
                .build();

        return postRepository.save(post);
    }

    @Override
    public Post updatePost(Long postId, String title, String content, PostType postType, LocalDate meetingDate, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION, "게시글 수정은 작성자만 가능합니다.");
        }

        validate(postType, meetingDate);

        post.update(title, content, postType, meetingDate);

        return post;
    }

    @Override
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION, "게시글 삭제는 작성자만 가능합니다.");
        }

        postRepository.deleteById(postId);
    }

    // 검증용 내부 함수
    private void validate(PostType postType, LocalDate meetingDate) {
        if (postType.equals(PostType.MATE) && meetingDate == null) {
            throw new BusinessException(ErrorCode.MISSING_REQUIRED_FIELD, "MATE 게시글은 meetingDate가 반드시 필요합니다.");
        }

        if (postType.equals(PostType.BOARD) && meetingDate != null) {
            throw new BusinessException(ErrorCode.CONSTRAINT_VIOLATION, "BOARD 게시글에는 meetingDate를 설정할 수 없습니다.");
        }

        if (meetingDate != null && meetingDate.isBefore(LocalDate.now())) {
            throw new BusinessException(ErrorCode.CONSTRAINT_VIOLATION, "meetingDate는 현재 시간보다 이후여야 합니다.");
        }
    }
}