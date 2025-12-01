package com.line7studio.boulderside.application.post;

import com.line7studio.boulderside.common.dto.UserInfo;
import com.line7studio.boulderside.controller.matepost.request.CreateMatePostRequest;
import com.line7studio.boulderside.controller.matepost.request.UpdateMatePostRequest;
import com.line7studio.boulderside.controller.matepost.response.MatePostPageResponse;
import com.line7studio.boulderside.controller.matepost.response.MatePostResponse;
import com.line7studio.boulderside.domain.feature.comment.enums.CommentDomainType;
import com.line7studio.boulderside.domain.feature.comment.service.CommentService;
import com.line7studio.boulderside.domain.feature.post.entity.MatePost;
import com.line7studio.boulderside.domain.feature.post.enums.MatePostSortType;
import com.line7studio.boulderside.domain.feature.post.service.MatePostService;
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
@Transactional(readOnly = true)
public class MatePostUseCase {

    private final MatePostService matePostService;
    private final UserService userService;
    private final CommentService commentService;

    public MatePostPageResponse getMatePostPage(Long cursor, String subCursor, int size, MatePostSortType sortType, Long userId) {
        List<MatePost> posts = matePostService.getMatePostsWithCursor(cursor, subCursor, size + 1, sortType);
        return buildCursorPageResponse(posts, size, sortType, userId);
    }

    public MatePostPageResponse getMyMatePosts(Long cursor, int size, Long userId) {
        List<MatePost> posts = matePostService.getMatePostsByUser(userId, cursor, size + 1);
        return buildMyPostPageResponse(posts, size, userId);
    }

    private MatePostPageResponse buildCursorPageResponse(List<MatePost> posts, int size, MatePostSortType sortType, Long userId) {
        boolean hasNext = posts.size() > size;
        if (hasNext) {
            posts = posts.subList(0, size);
        }

        Long nextCursor = null;
        String nextSubCursor = null;
        if (hasNext && !posts.isEmpty()) {
            MatePost lastPost = posts.getLast();
            nextCursor = lastPost.getId();
            nextSubCursor = getNextSubCursor(lastPost, sortType);
        }

        return buildPostPageResponse(posts, nextCursor, nextSubCursor, hasNext, userId);
    }

    private MatePostPageResponse buildMyPostPageResponse(List<MatePost> posts, int size, Long userId) {
        boolean hasNext = posts.size() > size;
        if (hasNext) {
            posts = posts.subList(0, size);
        }

        Long nextCursor = null;
        if (hasNext && !posts.isEmpty()) {
            nextCursor = posts.getLast().getId();
        }

        return buildPostPageResponse(posts, nextCursor, null, hasNext, userId);
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

    @Transactional
    public MatePostResponse getMatePost(Long postId, Long userId) {
        MatePost post = matePostService.getMatePostById(postId);
        post.incrementViewCount();
        return buildSinglePostResponse(post, userId);
    }

    @Transactional
    public MatePostResponse createMatePost(CreateMatePostRequest request, Long userId) {
        User user = userService.getUserById(userId);
        UserInfo userInfo = UserInfo.from(user);

        MatePost savedPost = matePostService.createMatePost(
            user.getId(),
            request.getTitle(),
            request.getContent(),
            request.getMeetingDate()
        );

        return MatePostResponse.of(savedPost, userInfo, true, 0L);
    }

    @Transactional
    public MatePostResponse updateMatePost(Long postId, UpdateMatePostRequest request, Long userId) {
        User user = userService.getUserById(userId);
        UserInfo userInfo = UserInfo.from(user);

        MatePost post = matePostService.updateMatePost(
            postId,
            request.getTitle(),
            request.getContent(),
            request.getMeetingDate(),
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

    private MatePostResponse buildSinglePostResponse(MatePost post, Long userId) {
        User user = userService.getUserById(post.getUserId());
        UserInfo userInfo = UserInfo.from(user);
        boolean isMine = post.getUserId().equals(userId);
        Long commentCount = commentService.countCommentsByDomainIdAndCommentDomainType(post.getId(), CommentDomainType.MATE_POST);
        return MatePostResponse.of(post, userInfo, isMine, commentCount);
    }
}
