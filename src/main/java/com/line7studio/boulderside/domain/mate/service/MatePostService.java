package com.line7studio.boulderside.domain.mate.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.enums.PostStatus;
import com.line7studio.boulderside.domain.mate.MatePost;
import com.line7studio.boulderside.domain.mate.enums.MatePostSortType;
import com.line7studio.boulderside.domain.mate.repository.MatePostQueryRepository;
import com.line7studio.boulderside.domain.mate.repository.MatePostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatePostService {

    private final MatePostRepository matePostRepository;
    private final MatePostQueryRepository matePostQueryRepository;

    public MatePost getMatePostById(Long postId) {
        log.info(String.valueOf(postId));
        return matePostRepository.findByIdAndStatus(postId, PostStatus.ACTIVE)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }

    public List<MatePost> getMatePostsByIds(List<Long> postIds) {
        return matePostRepository.findAllById(postIds);
    }

    public List<MatePost> getMatePostsWithCursor(Long cursor, String subCursor, int size, MatePostSortType postSortType, List<Long> excludedUserIds) {
        if (postSortType == null) {
            throw new BusinessException(ErrorCode.NOT_SUPPORT_SORT_TYPE);
        }
        return matePostQueryRepository.findMatePostsWithCursor(cursor, subCursor, size, postSortType, true, excludedUserIds);
    }

    public List<MatePost> getMatePostsWithCursorForAdmin(Long cursor, String subCursor, int size, MatePostSortType postSortType) {
        if (postSortType == null) {
            throw new BusinessException(ErrorCode.NOT_SUPPORT_SORT_TYPE);
        }
        return matePostQueryRepository.findMatePostsWithCursor(cursor, subCursor, size, postSortType, false, List.of());
    }

    public List<MatePost> getMatePostsByUser(Long userId, Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));
        if (cursor == null) {
            return matePostRepository.findByUserIdAndStatusOrderByIdDesc(userId, PostStatus.ACTIVE, pageable);
        }
        return matePostRepository.findByUserIdAndIdLessThanAndStatusOrderByIdDesc(userId, cursor, PostStatus.ACTIVE, pageable);
    }

    public MatePost createMatePost(Long userId, String title, String content, LocalDate meetingDate) {
        // Entity의 정적 팩토리 메서드가 검증을 수행
        MatePost matePost = MatePost.create(userId, title, content, meetingDate);
        return matePostRepository.save(matePost);
    }

    public MatePost updateMatePost(Long postId, String title, String content, LocalDate meetingDate, Long userId) {
        MatePost matePost = matePostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        // Entity가 소유자 검증 및 업데이트 검증 수행
        matePost.verifyOwner(userId);
        matePost.update(title, content, meetingDate);

        return matePostRepository.save(matePost);
    }

    public void deleteMatePost(Long postId, Long userId) {
        MatePost matePost = matePostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        // Entity가 소유자 검증 수행
        matePost.verifyOwner(userId);
        matePostRepository.deleteById(postId);
    }

    public MatePost updateMatePostAsAdmin(Long postId, String title, String content, LocalDate meetingDate) {
        MatePost matePost = matePostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        // Entity의 update가 검증 수행
        matePost.update(title, content, meetingDate);
        return matePostRepository.save(matePost);
    }

    public void deleteMatePostAsAdmin(Long postId) {
        MatePost matePost = matePostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        matePostRepository.delete(matePost);
    }

    public void updateMatePostStatus(Long postId, PostStatus status) {
        MatePost matePost = matePostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        matePost.updateStatus(status);
        matePostRepository.save(matePost);
    }

    public MatePost getMatePostByIdForAdmin(Long postId) {
        return matePostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }

    public void updateMatePostsStatusByUser(Long userId, PostStatus status) {
        List<MatePost> posts = matePostRepository.findAllByUserId(userId);
        posts.forEach(post -> post.updateStatus(status));
        matePostRepository.saveAll(posts);
    }

    public void restoreMatePostsStatusByUser(Long userId) {
        List<MatePost> posts = matePostRepository.findAllByUserId(userId);
        posts.forEach(MatePost::activate);
        matePostRepository.saveAll(posts);
    }
}
