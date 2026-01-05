package com.line7studio.boulderside.usecase.post;

import com.line7studio.boulderside.common.dto.UserInfo;
import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.controller.common.request.UpdatePostStatusRequest;
import com.line7studio.boulderside.controller.matepost.request.CreateMatePostRequest;
import com.line7studio.boulderside.controller.matepost.request.UpdateMatePostRequest;
import com.line7studio.boulderside.controller.matepost.response.MatePostPageResponse;
import com.line7studio.boulderside.controller.matepost.response.MatePostResponse;
import com.line7studio.boulderside.domain.comment.enums.CommentDomainType;
import com.line7studio.boulderside.domain.comment.service.CommentService;
import com.line7studio.boulderside.domain.mate.MatePost;
import com.line7studio.boulderside.domain.mate.enums.MatePostSortType;
import com.line7studio.boulderside.domain.mate.service.MatePostService;
import com.line7studio.boulderside.domain.user.User;
import com.line7studio.boulderside.domain.user.service.UserService;
import com.line7studio.boulderside.domain.user.service.UserBlockService;
import com.line7studio.boulderside.common.util.CursorPageUtil;
import com.line7studio.boulderside.common.util.CursorPageUtil.CursorPageWithSubCursor;
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
@Transactional(readOnly = true)
public class MatePostUseCase {

    private static final int MAX_PAGE_SIZE = 50;

    private final MatePostService matePostService;
    private final UserService userService;
    private final CommentService commentService;
    private final UserBlockService userBlockService;

    public MatePostPageResponse getMatePostPage(Long cursor, String subCursor, int size, MatePostSortType sortType, Long userId) {
        int pageSize = normalizeSize(size);
        List<Long> blockedUserIds = userBlockService.getBlockedOrBlockingUserIds(userId);
        List<MatePost> posts = matePostService.getMatePostsWithCursor(cursor, subCursor, pageSize + 1, sortType, blockedUserIds);
        return buildCursorPageResponse(posts, pageSize, sortType, userId);
    }

    public MatePostPageResponse getMatePostPageForAdmin(Long cursor, String subCursor, int size, MatePostSortType sortType, Long adminUserId) {
        int pageSize = normalizeSize(size);
        List<MatePost> posts = matePostService.getMatePostsWithCursorForAdmin(cursor, subCursor, pageSize + 1, sortType);
        return buildCursorPageResponse(posts, pageSize, sortType, adminUserId);
    }

    public MatePostPageResponse getMyMatePosts(Long cursor, int size, Long userId) {
        int pageSize = normalizeSize(size);
        List<MatePost> posts = matePostService.getMatePostsByUser(userId, cursor, pageSize + 1);
        return buildMyPostPageResponse(posts, pageSize, userId);
    }

    private MatePostPageResponse buildCursorPageResponse(List<MatePost> posts, int size, MatePostSortType sortType, Long userId) {
        CursorPageWithSubCursor<MatePost> page = CursorPageUtil.ofWithSubCursor(
            posts, size, MatePost::getId, p -> getNextSubCursor(p, sortType));
        return buildPostPageResponse(page.content(), page.nextCursor(), page.nextSubCursor(), page.hasNext(), userId);
    }

    private MatePostPageResponse buildMyPostPageResponse(List<MatePost> posts, int size, Long userId) {
        CursorPageUtil<MatePost> page = CursorPageUtil.of(posts, size, MatePost::getId);
        return buildPostPageResponse(page.content(), page.nextCursor(), null, page.hasNext(), userId);
    }

    private MatePostPageResponse buildPostPageResponse(List<MatePost> posts, Long nextCursor, String nextSubCursor, boolean hasNext, Long viewerId) {
        if (posts.isEmpty()) {
            return MatePostPageResponse.of(List.of(), nextCursor, nextSubCursor, hasNext, 0);
        }

        List<Long> userIds = posts.stream()
            .map(MatePost::getUserId)
            .distinct()
            .toList();

        Map<Long, User> userMap = userService.findAllById(userIds).stream()
            .collect(Collectors.toMap(User::getId, u -> u));

        List<Long> postIds = posts.stream()
            .map(MatePost::getId)
            .toList();

        Map<Long, Long> commentCountMap = commentService.countCommentsByDomainIdsAndCommentDomainTypeType(postIds, CommentDomainType.MATE_POST);

        List<MatePostResponse> responses = posts.stream()
            .map(post -> {
                User user = userMap.get(post.getUserId());
                UserInfo userInfo = UserInfo.from(user);
                boolean isMine = post.getUserId().equals(viewerId);
                Long commentCount = commentCountMap.getOrDefault(post.getId(), 0L);
                return MatePostResponse.of(post, userInfo, isMine, commentCount);
            })
            .toList();

        return MatePostPageResponse.of(responses, nextCursor, nextSubCursor, hasNext, posts.size());
    }

    private String getNextSubCursor(MatePost post, MatePostSortType sortType) {
        return switch (sortType) {
            case LATEST_CREATED -> post.getCreatedAt().toString();
            case MOST_VIEWED -> post.getViewCount().toString();
            case NEAREST_MEETING_DATE -> post.getMeetingDate().toString();
        };
    }

    private int normalizeSize(int size) {
        if (size <= 0) {
            return 10;
        }
        return Math.min(size, MAX_PAGE_SIZE);
    }

    @Transactional
    public MatePostResponse getMatePost(Long postId, Long userId) {
        MatePost post = matePostService.getMatePostById(postId);
        if (userBlockService.isBlockedBetween(userId, post.getUserId())) {
            throw new BusinessException(ErrorCode.NO_PERMISSION, "차단한 사용자의 게시글입니다.");
        }
        post.incrementViewCount();
        return buildSinglePostResponse(post, userId);
    }

    @Transactional
    public MatePostResponse getMatePostForAdmin(Long postId, Long adminUserId) {
        MatePost post = matePostService.getMatePostByIdForAdmin(postId);
        post.incrementViewCount();
        return buildSinglePostResponse(post, adminUserId);
    }

    @Transactional
    public MatePostResponse createMatePost(CreateMatePostRequest request, Long userId) {
        User user = userService.getUserById(userId);
        UserInfo userInfo = UserInfo.from(user);

        MatePost savedPost = matePostService.createMatePost(
            user.getId(),
            request.title(),
            request.content(),
            request.meetingDate()
        );

        return MatePostResponse.of(savedPost, userInfo, true, 0L);
    }

    @Transactional
    public MatePostResponse updateMatePost(Long postId, UpdateMatePostRequest request, Long userId) {
        User user = userService.getUserById(userId);
        UserInfo userInfo = UserInfo.from(user);

        MatePost post = matePostService.updateMatePost(
            postId,
            request.title(),
            request.content(),
            request.meetingDate(),
            user.getId()
        );

        boolean isMine = post.getUserId().equals(userId);
        Long commentCount = commentService.countCommentsByDomainIdAndCommentDomainType(postId, CommentDomainType.MATE_POST);

        return MatePostResponse.of(post, userInfo, isMine, commentCount);
    }

    @Transactional
    public void deleteMatePost(Long postId, Long userId) {
        User user = userService.getUserById(userId);
        matePostService.deleteMatePost(postId, user.getId());
    }

    @Transactional
    public MatePostResponse adminCreateMatePost(CreateMatePostRequest request, Long adminUserId) {
        User adminUser = userService.getUserById(adminUserId);
        UserInfo userInfo = UserInfo.from(adminUser);

        MatePost savedPost = matePostService.createMatePost(
            adminUser.getId(),
            request.title(),
            request.content(),
            request.meetingDate()
        );

        return MatePostResponse.of(savedPost, userInfo, true, 0L);
    }

    @Transactional
    public MatePostResponse adminUpdateMatePost(Long postId, UpdateMatePostRequest request) {
        MatePost post = matePostService.updateMatePostAsAdmin(
            postId,
            request.title(),
            request.content(),
            request.meetingDate()
        );

        User user = userService.getUserById(post.getUserId());
        UserInfo userInfo = UserInfo.from(user);
        Long commentCount = commentService.countCommentsByDomainIdAndCommentDomainType(postId, CommentDomainType.MATE_POST);
        return MatePostResponse.of(post, userInfo, false, commentCount);
    }

    @Transactional
    public void adminDeleteMatePost(Long postId) {
        matePostService.deleteMatePostAsAdmin(postId);
    }

    @Transactional
    public void updateMatePostStatus(Long postId, UpdatePostStatusRequest request) {
        matePostService.updateMatePostStatus(postId, request.status());
    }

    private MatePostResponse buildSinglePostResponse(MatePost post, Long userId) {
        User user = userService.getUserById(post.getUserId());
        UserInfo userInfo = UserInfo.from(user);
        boolean isMine = post.getUserId().equals(userId);
        Long commentCount = commentService.countCommentsByDomainIdAndCommentDomainType(post.getId(), CommentDomainType.MATE_POST);
        return MatePostResponse.of(post, userInfo, isMine, commentCount);
    }
}
