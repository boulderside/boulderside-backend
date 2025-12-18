package com.line7studio.boulderside.usecase.post;

import com.line7studio.boulderside.common.dto.UserInfo;
import com.line7studio.boulderside.controller.boardpost.request.CreateBoardPostRequest;
import com.line7studio.boulderside.controller.boardpost.request.UpdateBoardPostRequest;
import com.line7studio.boulderside.controller.boardpost.response.BoardPostPageResponse;
import com.line7studio.boulderside.controller.boardpost.response.BoardPostResponse;
import com.line7studio.boulderside.controller.common.request.UpdatePostStatusRequest;
import com.line7studio.boulderside.domain.comment.enums.CommentDomainType;
import com.line7studio.boulderside.domain.comment.service.CommentService;
import com.line7studio.boulderside.domain.board.BoardPost;
import com.line7studio.boulderside.domain.board.enums.BoardPostSortType;
import com.line7studio.boulderside.domain.board.service.BoardPostService;
import com.line7studio.boulderside.domain.user.User;
import com.line7studio.boulderside.domain.user.service.UserService;
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
public class BoardPostUseCase {

    private final BoardPostService boardPostService;
    private final UserService userService;
    private final CommentService commentService;

    public BoardPostPageResponse getBoardPostPage(Long cursor, String subCursor, int size, BoardPostSortType sortType, Long userId) {
        List<BoardPost> posts = boardPostService.getBoardPostsWithCursor(cursor, subCursor, size + 1, sortType);
        return buildCursorPageResponse(posts, size, sortType, userId);
    }

    public BoardPostPageResponse getBoardPostPageForAdmin(Long cursor, String subCursor, int size, BoardPostSortType sortType, Long adminUserId) {
        List<BoardPost> posts = boardPostService.getBoardPostsWithCursorForAdmin(cursor, subCursor, size + 1, sortType);
        return buildCursorPageResponse(posts, size, sortType, adminUserId);
    }

    public BoardPostPageResponse getMyBoardPosts(Long cursor, int size, Long userId) {
        List<BoardPost> posts = boardPostService.getBoardPostsByUser(userId, cursor, size + 1);
        return buildMyPostPageResponse(posts, size, userId);
    }

    private BoardPostPageResponse buildCursorPageResponse(List<BoardPost> posts, int size, BoardPostSortType sortType, Long userId) {
        CursorPageWithSubCursor<BoardPost> page = CursorPageUtil.ofWithSubCursor(
            posts, size, BoardPost::getId, p -> getNextSubCursor(p, sortType));
        return buildPostPageResponse(page.content(), page.nextCursor(), page.nextSubCursor(), page.hasNext(), userId);
    }

    private BoardPostPageResponse buildMyPostPageResponse(List<BoardPost> posts, int size, Long userId) {
        CursorPageUtil<BoardPost> page = CursorPageUtil.of(posts, size, BoardPost::getId);
        return buildPostPageResponse(page.content(), page.nextCursor(), null, page.hasNext(), userId);
    }

    private BoardPostPageResponse buildPostPageResponse(List<BoardPost> posts, Long nextCursor, String nextSubCursor, boolean hasNext, Long viewerId) {
        if (posts.isEmpty()) {
            return BoardPostPageResponse.of(List.of(), nextCursor, nextSubCursor, hasNext, 0);
        }

        List<Long> userIds = posts.stream()
            .map(BoardPost::getUserId)
            .distinct()
            .toList();

        Map<Long, User> userMap = userService.findAllById(userIds).stream()
            .collect(Collectors.toMap(User::getId, u -> u));

        List<Long> postIds = posts.stream()
            .map(BoardPost::getId)
            .toList();

        Map<Long, Long> commentCountMap = commentService.countCommentsByDomainIdsAndCommentDomainTypeType(postIds, CommentDomainType.BOARD_POST);

        List<BoardPostResponse> responses = posts.stream()
            .map(post -> {
                User user = userMap.get(post.getUserId());
                UserInfo userInfo = UserInfo.from(user);
                boolean isMine = post.getUserId().equals(viewerId);
                Long commentCount = commentCountMap.getOrDefault(post.getId(), 0L);
                return BoardPostResponse.of(post, userInfo, isMine, commentCount);
            })
            .toList();

        return BoardPostPageResponse.of(responses, nextCursor, nextSubCursor, hasNext, posts.size());
    }

    private String getNextSubCursor(BoardPost post, BoardPostSortType sortType) {
        return switch (sortType) {
            case LATEST_CREATED -> post.getCreatedAt().toString();
            case MOST_VIEWED -> post.getViewCount().toString();
        };
    }

    @Transactional
    public BoardPostResponse getBoardPost(Long postId, Long userId) {
        BoardPost post = boardPostService.getBoardPostById(postId);
        post.incrementViewCount();
        return buildSinglePostResponse(post, userId);
    }

    @Transactional
    public BoardPostResponse getBoardPostForAdmin(Long postId, Long adminUserId) {
        BoardPost post = boardPostService.getBoardPostByIdForAdmin(postId);
        post.incrementViewCount();
        return buildSinglePostResponse(post, adminUserId);
    }

    @Transactional
    public BoardPostResponse createBoardPost(CreateBoardPostRequest request, Long userId) {
        User user = userService.getUserById(userId);
        UserInfo userInfo = UserInfo.from(user);

        BoardPost savedPost = boardPostService.createBoardPost(
            user.getId(),
            request.title(),
            request.content()
        );

        return BoardPostResponse.of(savedPost, userInfo, true, 0L);
    }

    @Transactional
    public BoardPostResponse updateBoardPost(Long postId, UpdateBoardPostRequest request, Long userId) {
        User user = userService.getUserById(userId);
        UserInfo userInfo = UserInfo.from(user);

        BoardPost post = boardPostService.updateBoardPost(
            postId,
            request.title(),
            request.content(),
            user.getId()
        );

        boolean isMine = post.getUserId().equals(userId);
        Long commentCount = commentService.countCommentsByDomainIdAndCommentDomainType(postId, CommentDomainType.BOARD_POST);

        return BoardPostResponse.of(post, userInfo, isMine, commentCount);
    }

    @Transactional
    public void deleteBoardPost(Long postId, Long userId) {
        User user = userService.getUserById(userId);
        boardPostService.deleteBoardPost(postId, user.getId());
    }

    @Transactional
    public BoardPostResponse adminUpdateBoardPost(Long postId, UpdateBoardPostRequest request) {
        BoardPost post = boardPostService.updateBoardPostAsAdmin(postId, request.title(), request.content());
        User user = userService.getUserById(post.getUserId());
        UserInfo userInfo = UserInfo.from(user);
        Long commentCount = commentService.countCommentsByDomainIdAndCommentDomainType(postId, CommentDomainType.BOARD_POST);
        return BoardPostResponse.of(post, userInfo, false, commentCount);
    }

    @Transactional
    public void adminDeleteBoardPost(Long postId) {
        boardPostService.deleteBoardPostAsAdmin(postId);
    }

    @Transactional
    public BoardPostResponse adminCreateBoardPost(CreateBoardPostRequest request, Long adminUserId) {
        User adminUser = userService.getUserById(adminUserId);
        UserInfo userInfo = UserInfo.from(adminUser);

        BoardPost savedPost = boardPostService.createBoardPost(
            adminUser.getId(),
            request.title(),
            request.content()
        );

        return BoardPostResponse.of(savedPost, userInfo, true, 0L);
    }

    @Transactional
    public void updateBoardPostStatus(Long postId, UpdatePostStatusRequest request) {
        boardPostService.updateBoardPostStatus(postId, request.status());
    }

    private BoardPostResponse buildSinglePostResponse(BoardPost post, Long userId) {
        User user = userService.getUserById(post.getUserId());
        UserInfo userInfo = UserInfo.from(user);
        boolean isMine = post.getUserId().equals(userId);
        Long commentCount = commentService.countCommentsByDomainIdAndCommentDomainType(post.getId(), CommentDomainType.BOARD_POST);
        return BoardPostResponse.of(post, userInfo, isMine, commentCount);
    }
}
