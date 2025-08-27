package com.line7studio.boulderside.application.comment;

import com.line7studio.boulderside.common.dto.UserInfo;
import com.line7studio.boulderside.controller.comment.request.CreateCommentRequest;
import com.line7studio.boulderside.controller.comment.request.UpdateCommentRequest;
import com.line7studio.boulderside.controller.comment.response.CommentPageResponse;
import com.line7studio.boulderside.controller.comment.response.CommentResponse;
import com.line7studio.boulderside.domain.aggregate.comment.entity.Comment;
import com.line7studio.boulderside.domain.aggregate.comment.enums.CommentDomainType;
import com.line7studio.boulderside.domain.aggregate.comment.service.CommentService;
import com.line7studio.boulderside.domain.aggregate.user.entity.User;
import com.line7studio.boulderside.domain.aggregate.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentUseCase {
    private final CommentService commentService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public CommentPageResponse getCommentPage(Long cursor, int size, Long domainId, CommentDomainType commentDomainType, Long userId) {
        // 댓글 조회
        List<Comment> commentList = commentService.getCommentsWithCursor(cursor, size + 1, domainId, commentDomainType);

        boolean hasNext = commentList.size() > size;
        if (hasNext) {
            commentList = commentList.subList(0, size);
        }

        // 다음 커서 위치 지정
        Long nextCursor = hasNext && !commentList.isEmpty() ? commentList.getLast().getId() : null;

        // 댓글 작성자 ID 취합
        List<Long> userIdList = commentList.stream()
                .map(Comment::getUserId)
                .distinct()
                .toList();

        // 댓글마다 작성자 매핑
        Map<Long, User> userMap = userService.findAllById(userIdList).stream()
                .collect(Collectors.toMap(User::getId, u -> u));

        // 댓글 - 유저 정보로 응답 작성
        List<CommentResponse> commentResponses = commentList.stream()
                .map(comment -> {
                    User user = userMap.get(comment.getUserId());
                    UserInfo userInfo = UserInfo.from(user);
                    Boolean isMine = comment.getUserId().equals(userId);
                    return CommentResponse.of(comment, userInfo, isMine);
                })
                .toList();

        return CommentPageResponse.of(commentResponses, nextCursor, hasNext, size);
    }

    @Transactional
    public CommentResponse createComment(Long postId, CommentDomainType commentDomainType, CreateCommentRequest request, Long userId) {
        User user = userService.getUserById(userId);
        UserInfo userInfo = UserInfo.from(user);

        Comment savedComment = commentService.createComment(
                user.getId(),
                postId,
                commentDomainType,
                request.getContent()
        );

        return CommentResponse.of(savedComment, userInfo, true);
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, UpdateCommentRequest request, Long userId) {
        User user = userService.getUserById(userId);
        UserInfo userInfo = UserInfo.from(user);

        Comment comment = commentService.updateComment(
                commentId,
                request.getContent(),
                user.getId()
        );

        Boolean isMine = comment.getUserId().equals(userId);

        return CommentResponse.of(comment, userInfo, isMine);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        User user = userService.getUserById(userId);
        commentService.deleteComment(commentId, user.getId());
    }
}