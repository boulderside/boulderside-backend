package com.line7studio.boulderside.application.comment;

import com.line7studio.boulderside.common.dto.UserInfo;
import com.line7studio.boulderside.controller.comment.request.CreateAdminCommentRequest;
import com.line7studio.boulderside.controller.comment.request.CreateCommentRequest;
import com.line7studio.boulderside.controller.comment.request.UpdateCommentRequest;
import com.line7studio.boulderside.controller.comment.response.CommentPageResponse;
import com.line7studio.boulderside.controller.comment.response.CommentResponse;
import com.line7studio.boulderside.controller.comment.response.MyCommentPageResponse;
import com.line7studio.boulderside.controller.comment.response.MyCommentResponse;
import com.line7studio.boulderside.domain.feature.comment.entity.Comment;
import com.line7studio.boulderside.domain.feature.comment.enums.CommentDomainType;
import com.line7studio.boulderside.domain.feature.comment.service.CommentService;
import com.line7studio.boulderside.domain.feature.post.entity.BoardPost;
import com.line7studio.boulderside.domain.feature.post.entity.MatePost;
import com.line7studio.boulderside.domain.feature.post.service.BoardPostReadService;
import com.line7studio.boulderside.domain.feature.post.service.BoardPostService;
import com.line7studio.boulderside.domain.feature.post.service.MatePostReadService;
import com.line7studio.boulderside.domain.feature.post.service.MatePostService;
import com.line7studio.boulderside.domain.feature.route.entity.Route;
import com.line7studio.boulderside.domain.feature.route.service.RouteService;
import com.line7studio.boulderside.domain.feature.user.entity.User;
import com.line7studio.boulderside.domain.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentUseCase {
    private final CommentService commentService;
    private final UserService userService;
    private final RouteService routeService;
    private final BoardPostService boardPostService;
    private final MatePostService matePostService;
    private final BoardPostReadService boardPostReadService;
    private final MatePostReadService matePostReadService;

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

    @Transactional(readOnly = true)
    public MyCommentPageResponse getMyComments(Long cursor, int size, Long userId, String domainType) {
        List<Comment> commentList;

        if (domainType != null) {
            CommentDomainType type = CommentDomainType.fromPath(domainType);
            commentList = commentService.getCommentsByUserAndTypeWithCursor(cursor, size + 1, userId, type);
        } else {
            commentList = commentService.getCommentsByUserWithCursor(cursor, size + 1, userId);
        }

        boolean hasNext = commentList.size() > size;
        if (hasNext) {
            commentList = commentList.subList(0, size);
        }

        Long nextCursor = hasNext && !commentList.isEmpty() ? commentList.getLast().getId() : null;

        User user = userService.getUserById(userId);
        UserInfo userInfo = UserInfo.from(user);

        // Group comments by DomainType and collect IDs
        List<Long> routeIds = new ArrayList<>();
        List<Long> boardPostIds = new ArrayList<>();
        List<Long> matePostIds = new ArrayList<>();

        for (Comment comment : commentList) {
            if (comment.getCommentDomainType() == CommentDomainType.ROUTE) {
                routeIds.add(comment.getDomainId());
            } else if (comment.getCommentDomainType() == CommentDomainType.BOARD_POST) {
                boardPostIds.add(comment.getDomainId());
            } else if (comment.getCommentDomainType() == CommentDomainType.MATE_POST) {
                matePostIds.add(comment.getDomainId());
            }
        }

        // Fetch Titles/Names
        Map<Long, String> routeNames = routeIds.isEmpty() ? Collections.emptyMap() :
                routeService.getRoutesByIds(routeIds).stream()
                        .collect(Collectors.toMap(Route::getId, Route::getName));
        
        Map<Long, String> boardPostTitles = boardPostIds.isEmpty() ? Collections.emptyMap() :
                boardPostService.getBoardPostsByIds(boardPostIds).stream()
                        .collect(Collectors.toMap(BoardPost::getId, BoardPost::getTitle));

        Map<Long, String> matePostTitles = matePostIds.isEmpty() ? Collections.emptyMap() :
                matePostService.getMatePostsByIds(matePostIds).stream()
                        .collect(Collectors.toMap(MatePost::getId, MatePost::getTitle));

        // Map response
        List<MyCommentResponse> commentResponses = commentList.stream()
                .map(comment -> {
                    String title = null;
                    if (comment.getCommentDomainType() == CommentDomainType.ROUTE) {
                        title = routeNames.get(comment.getDomainId());
                    } else if (comment.getCommentDomainType() == CommentDomainType.BOARD_POST) {
                        title = boardPostTitles.get(comment.getDomainId());
                    } else if (comment.getCommentDomainType() == CommentDomainType.MATE_POST) {
                        title = matePostTitles.get(comment.getDomainId());
                    }
                    return MyCommentResponse.of(comment, userInfo, true, title);
                })
                .toList();

        return MyCommentPageResponse.of(commentResponses, nextCursor, hasNext, size);
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

        Long commentCount = 0L;
        if (commentDomainType == CommentDomainType.BOARD_POST) {
            commentCount = boardPostReadService.incrementCommentCount(postId);
        } else if (commentDomainType == CommentDomainType.MATE_POST) {
            commentCount = matePostReadService.incrementCommentCount(postId);
        } else if (commentDomainType == CommentDomainType.ROUTE) {
            Route route = routeService.getRouteById(postId);
            route.incrementCommentCount();
            commentCount = route.getCommentCount();
        }

        return CommentResponse.of(savedComment, userInfo, true, commentCount);
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

        Long commentCount = 0L;
        if (comment.getCommentDomainType() == CommentDomainType.BOARD_POST) {
            commentCount = boardPostReadService.getCommentCount(comment.getDomainId());
        } else if (comment.getCommentDomainType() == CommentDomainType.MATE_POST) {
            commentCount = matePostReadService.getCommentCount(comment.getDomainId());
        } else if (comment.getCommentDomainType() == CommentDomainType.ROUTE) {
            Route route = routeService.getRouteById(comment.getDomainId());
            commentCount = route.getCommentCount();
        }

        Boolean isMine = comment.getUserId().equals(userId);

        return CommentResponse.of(comment, userInfo, isMine, commentCount);
    }

    @Transactional
    public Integer deleteComment(Long commentId, Long userId) {
        User user = userService.getUserById(userId);

        Comment comment = commentService.getCommentById(commentId);
        
        commentService.deleteComment(commentId, user.getId());

        Long commentCount = 0L;
        if (comment.getCommentDomainType() == CommentDomainType.BOARD_POST) {
            commentCount = boardPostReadService.decrementCommentCount(comment.getDomainId());
        } else if (comment.getCommentDomainType() == CommentDomainType.MATE_POST) {
            commentCount = matePostReadService.decrementCommentCount(comment.getDomainId());
        } else if (comment.getCommentDomainType() == CommentDomainType.ROUTE) {
            Route route = routeService.getRouteById(comment.getDomainId());
            route.decrementCommentCount();
            commentCount = route.getCommentCount();
        }
        return commentCount.intValue();
    }

    @Transactional
    public CommentResponse adminUpdateComment(Long commentId, UpdateCommentRequest request) {
        Comment comment = commentService.updateCommentAsAdmin(commentId, request.getContent());
        User user = userService.getUserById(comment.getUserId());
        UserInfo userInfo = UserInfo.from(user);

        Long commentCount = 0L;
        if (comment.getCommentDomainType() == CommentDomainType.BOARD_POST) {
            commentCount = boardPostReadService.getCommentCount(comment.getDomainId());
        } else if (comment.getCommentDomainType() == CommentDomainType.MATE_POST) {
            commentCount = matePostReadService.getCommentCount(comment.getDomainId());
        } else if (comment.getCommentDomainType() == CommentDomainType.ROUTE) {
            Route route = routeService.getRouteById(comment.getDomainId());
            commentCount = route.getCommentCount();
        }

        return CommentResponse.of(comment, userInfo, false, commentCount);
    }

    @Transactional
    public Integer adminDeleteComment(Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        commentService.deleteCommentAsAdmin(commentId);

        Long commentCount = 0L;
        if (comment.getCommentDomainType() == CommentDomainType.BOARD_POST) {
            commentCount = boardPostReadService.decrementCommentCount(comment.getDomainId());
        } else if (comment.getCommentDomainType() == CommentDomainType.MATE_POST) {
            commentCount = matePostReadService.decrementCommentCount(comment.getDomainId());
        } else if (comment.getCommentDomainType() == CommentDomainType.ROUTE) {
            Route route = routeService.getRouteById(comment.getDomainId());
            route.decrementCommentCount();
            commentCount = route.getCommentCount();
        }
        return commentCount.intValue();
    }

    @Transactional
    public CommentResponse adminCreateComment(CreateAdminCommentRequest request, Long adminUserId) {
        Long targetUserId = request.getUserId() != null ? request.getUserId() : adminUserId;
        User user = userService.getUserById(targetUserId);
        UserInfo userInfo = UserInfo.from(user);

        CommentDomainType domainType = CommentDomainType.fromPath(request.getDomainType());

        Comment savedComment = commentService.createComment(
            user.getId(),
            request.getDomainId(),
            domainType,
            request.getContent()
        );

        Long commentCount = 0L;
        if (domainType == CommentDomainType.BOARD_POST) {
            commentCount = boardPostReadService.incrementCommentCount(request.getDomainId());
        } else if (domainType == CommentDomainType.MATE_POST) {
            commentCount = matePostReadService.incrementCommentCount(request.getDomainId());
        } else if (domainType == CommentDomainType.ROUTE) {
            Route route = routeService.getRouteById(request.getDomainId());
            route.incrementCommentCount();
            commentCount = route.getCommentCount();
        }

        boolean isMine = targetUserId.equals(adminUserId);
        return CommentResponse.of(savedComment, userInfo, isMine, commentCount);
    }
}
