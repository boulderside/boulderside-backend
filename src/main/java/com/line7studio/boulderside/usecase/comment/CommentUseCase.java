package com.line7studio.boulderside.usecase.comment;

import com.line7studio.boulderside.common.dto.UserInfo;
import com.line7studio.boulderside.common.notification.NotificationDomainType;
import com.line7studio.boulderside.common.notification.NotificationTarget;
import com.line7studio.boulderside.common.notification.PushMessage;
import com.line7studio.boulderside.controller.comment.request.CreateAdminCommentRequest;
import com.line7studio.boulderside.controller.comment.request.CreateCommentRequest;
import com.line7studio.boulderside.controller.comment.request.UpdateCommentRequest;
import com.line7studio.boulderside.controller.comment.response.CommentPageResponse;
import com.line7studio.boulderside.controller.comment.response.CommentResponse;
import com.line7studio.boulderside.controller.comment.response.MyCommentPageResponse;
import com.line7studio.boulderside.controller.comment.response.MyCommentResponse;
import com.line7studio.boulderside.controller.common.request.UpdatePostStatusRequest;
import com.line7studio.boulderside.domain.comment.Comment;
import com.line7studio.boulderside.domain.comment.enums.CommentDomainType;
import com.line7studio.boulderside.domain.comment.service.CommentService;
import com.line7studio.boulderside.domain.board.BoardPost;
import com.line7studio.boulderside.domain.mate.MatePost;
import com.line7studio.boulderside.domain.board.service.BoardPostReadService;
import com.line7studio.boulderside.domain.board.service.BoardPostService;
import com.line7studio.boulderside.domain.mate.service.MatePostReadService;
import com.line7studio.boulderside.domain.mate.service.MatePostService;
import com.line7studio.boulderside.domain.route.Route;
import com.line7studio.boulderside.domain.route.service.RouteService;
import com.line7studio.boulderside.domain.user.User;
import com.line7studio.boulderside.domain.user.service.UserService;
import com.line7studio.boulderside.domain.user.service.UserBlockService;
import com.line7studio.boulderside.common.util.CursorPageUtil;
import com.line7studio.boulderside.infrastructure.fcm.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private final UserBlockService userBlockService;
    private final FcmService fcmService;

    @Transactional(readOnly = true)
    public CommentPageResponse getCommentPage(Long cursor, int size, Long domainId, CommentDomainType commentDomainType, Long userId) {
        List<Long> blockedUserIds = userBlockService.getBlockedOrBlockingUserIds(userId);
        return getCommentPageInternal(cursor, size, domainId, commentDomainType, userId, true, blockedUserIds);
    }

    public CommentPageResponse getCommentPageForAdmin(Long cursor, int size, Long domainId, CommentDomainType commentDomainType, Long adminUserId) {
        return getCommentPageInternal(cursor, size, domainId, commentDomainType, adminUserId, false, List.of());
    }

    private CommentPageResponse getCommentPageInternal(Long cursor, int size, Long domainId, CommentDomainType commentDomainType, Long userId, boolean activeOnly, List<Long> blockedUserIds) {
        List<Comment> commentList = commentService.getCommentsWithCursor(cursor, size + 1, domainId, commentDomainType, activeOnly, blockedUserIds);

        CursorPageUtil<Comment> page = CursorPageUtil.of(commentList, size, Comment::getId);

        if (page.content().isEmpty()) {
            return CommentPageResponse.of(Collections.emptyList(), null, false, size);
        }

        // 댓글 작성자 ID 취합
        List<Long> userIdList = page.content().stream()
                .map(Comment::getUserId)
                .distinct()
                .toList();

        // 댓글마다 작성자 매핑
        Map<Long, User> userMap = userService.findAllById(userIdList).stream()
                .collect(Collectors.toMap(User::getId, u -> u));

        // 댓글 - 유저 정보로 응답 작성
        List<CommentResponse> commentResponses = page.content().stream()
                .map(comment -> {
                    User user = userMap.get(comment.getUserId());
                    UserInfo userInfo = UserInfo.from(user);
                    Boolean isMine = comment.getUserId().equals(userId);
                    return CommentResponse.of(comment, userInfo, isMine);
                })
                .toList();

        return CommentPageResponse.of(commentResponses, page.nextCursor(), page.hasNext(), page.size());
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

        CursorPageUtil<Comment> page = CursorPageUtil.of(commentList, size, Comment::getId);

        if (page.content().isEmpty()) {
            return MyCommentPageResponse.of(Collections.emptyList(), null, false, size);
        }

        User user = userService.getUserById(userId);
        UserInfo userInfo = UserInfo.from(user);

        // Group comments by DomainType and collect IDs
        List<Long> routeIds = new ArrayList<>();
        List<Long> boardPostIds = new ArrayList<>();
        List<Long> matePostIds = new ArrayList<>();

        for (Comment comment : page.content()) {
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
        List<MyCommentResponse> commentResponses = page.content().stream()
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

        return MyCommentPageResponse.of(commentResponses, page.nextCursor(), page.hasNext(), page.size());
    }

    @Transactional
    public CommentResponse createComment(Long postId, CommentDomainType commentDomainType, CreateCommentRequest request, Long userId) {
        User user = userService.getUserById(userId);
        UserInfo userInfo = UserInfo.from(user);
        PostAuthorInfo postAuthorInfo = getPostAuthorInfo(postId, commentDomainType);

        Comment savedComment = commentService.createComment(
                user.getId(),
                postId,
                commentDomainType,
                request.content()
        );

        Long commentCount = 0L;
        if (commentDomainType == CommentDomainType.BOARD_POST) {
            commentCount = boardPostReadService.incrementCommentCount(postId);
        } else if (commentDomainType == CommentDomainType.MATE_POST) {
            commentCount = matePostReadService.incrementCommentCount(postId);
        } else if (commentDomainType == CommentDomainType.ROUTE) {
            Route route = routeService.getById(postId);
            route.incrementCommentCount();
            commentCount = route.getCommentCount();
        }

        publishCommentPushAfterCommit(commentDomainType, postId, postAuthorInfo, userId);
        return CommentResponse.of(savedComment, userInfo, true, commentCount);
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, UpdateCommentRequest request, Long userId) {
        User user = userService.getUserById(userId);
        UserInfo userInfo = UserInfo.from(user);

        Comment comment = commentService.updateComment(
                commentId,
                request.content(),
                user.getId()
        );

        Long commentCount = 0L;
        if (comment.getCommentDomainType() == CommentDomainType.BOARD_POST) {
            commentCount = boardPostReadService.getCommentCount(comment.getDomainId());
        } else if (comment.getCommentDomainType() == CommentDomainType.MATE_POST) {
            commentCount = matePostReadService.getCommentCount(comment.getDomainId());
        } else if (comment.getCommentDomainType() == CommentDomainType.ROUTE) {
            Route route = routeService.getById(comment.getDomainId());
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
            Route route = routeService.getById(comment.getDomainId());
            route.decrementCommentCount();
            commentCount = route.getCommentCount();
        }
        return commentCount.intValue();
    }

    @Transactional
    public CommentResponse adminUpdateComment(Long commentId, UpdateCommentRequest request) {
        Comment comment = commentService.updateCommentAsAdmin(commentId, request.content());
        User user = userService.getUserById(comment.getUserId());
        UserInfo userInfo = UserInfo.from(user);

        Long commentCount = 0L;
        if (comment.getCommentDomainType() == CommentDomainType.BOARD_POST) {
            commentCount = boardPostReadService.getCommentCount(comment.getDomainId());
        } else if (comment.getCommentDomainType() == CommentDomainType.MATE_POST) {
            commentCount = matePostReadService.getCommentCount(comment.getDomainId());
        } else if (comment.getCommentDomainType() == CommentDomainType.ROUTE) {
            Route route = routeService.getById(comment.getDomainId());
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
            Route route = routeService.getById(comment.getDomainId());
            route.decrementCommentCount();
            commentCount = route.getCommentCount();
        }
        return commentCount.intValue();
    }

    @Transactional
    public CommentResponse adminCreateComment(CreateAdminCommentRequest request, Long adminUserId) {
        Long targetUserId = request.userId() != null ? request.userId() : adminUserId;
        User user = userService.getUserById(targetUserId);
        UserInfo userInfo = UserInfo.from(user);

        CommentDomainType domainType = CommentDomainType.fromPath(request.domainType());
        PostAuthorInfo postAuthorInfo = getPostAuthorInfo(request.domainId(), domainType);

        Comment savedComment = commentService.createComment(
            user.getId(),
            request.domainId(),
            domainType,
            request.content()
        );

        Long commentCount = 0L;
        if (domainType == CommentDomainType.BOARD_POST) {
            commentCount = boardPostReadService.incrementCommentCount(request.domainId());
        } else if (domainType == CommentDomainType.MATE_POST) {
            commentCount = matePostReadService.incrementCommentCount(request.domainId());
        } else if (domainType == CommentDomainType.ROUTE) {
            Route route = routeService.getById(request.domainId());
            route.incrementCommentCount();
            commentCount = route.getCommentCount();
        }

        boolean isMine = targetUserId.equals(adminUserId);
        publishCommentPushAfterCommit(domainType, request.domainId(), postAuthorInfo, targetUserId);
        return CommentResponse.of(savedComment, userInfo, isMine, commentCount);
    }

    @Transactional
    public void updateCommentStatus(Long commentId, UpdatePostStatusRequest request) {
        commentService.updateCommentStatus(commentId, request.status());
    }

    private PostAuthorInfo getPostAuthorInfo(Long postId, CommentDomainType commentDomainType) {
        if (commentDomainType == CommentDomainType.BOARD_POST) {
            BoardPost post = boardPostService.getBoardPostById(postId);
            return new PostAuthorInfo(post.getUserId(), post.getTitle());
        }
        if (commentDomainType == CommentDomainType.MATE_POST) {
            MatePost post = matePostService.getMatePostById(postId);
            return new PostAuthorInfo(post.getUserId(), post.getTitle());
        }
        return new PostAuthorInfo(null, null);
    }

    private void publishCommentPushAfterCommit(CommentDomainType domainType, Long postId, PostAuthorInfo postAuthorInfo, Long commenterId) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            sendCommentPush(domainType, postId, postAuthorInfo, commenterId);
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                sendCommentPush(domainType, postId, postAuthorInfo, commenterId);
            }
        });
    }

    private void sendCommentPush(CommentDomainType domainType, Long postId, PostAuthorInfo postAuthorInfo, Long commenterId) {
        if (postAuthorInfo.userId() == null || postAuthorInfo.userId().equals(commenterId)) {
            return;
        }
        NotificationDomainType targetType = switch (domainType) {
            case MATE_POST -> NotificationDomainType.MATE_POST;
            case BOARD_POST -> NotificationDomainType.BOARD_POST;
            default -> null;
        };
        if (targetType == null) {
            return;
        }
        Optional<String> token = userService.getFcmTokenForPush(postAuthorInfo.userId());
        if (token.isEmpty()) {
            return;
        }
        String title = domainType == CommentDomainType.MATE_POST ? "내 동행글 댓글" : "내 게시글 댓글";
        String body = postAuthorInfo.title() != null ? postAuthorInfo.title() : "새 댓글이 달렸어요";
        PushMessage message = new PushMessage(title, body, new NotificationTarget(targetType, String.valueOf(postId)));
        fcmService.sendMessageToAll(List.of(token.get()), message);
    }

    private record PostAuthorInfo(Long userId, String title) {}
}
