package com.line7studio.boulderside.domain.feature.post.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.feature.post.entity.MatePost;
import com.line7studio.boulderside.domain.feature.post.enums.MatePostSortType;
import com.line7studio.boulderside.domain.feature.post.repository.MatePostQueryRepository;
import com.line7studio.boulderside.domain.feature.post.repository.MatePostRepository;
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
        return matePostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }

    public List<MatePost> getMatePostsByIds(List<Long> postIds) {
        return matePostRepository.findAllById(postIds);
    }

    public List<MatePost> getMatePostsWithCursor(Long cursor, String subCursor, int size, MatePostSortType postSortType) {
        if (postSortType == null) {
            throw new BusinessException(ErrorCode.NOT_SUPPORT_SORT_TYPE);
        }
        return matePostQueryRepository.findMatePostsWithCursor(cursor, subCursor, size, postSortType);
    }

    public List<MatePost> getMatePostsByUser(Long userId, Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));
        if (cursor == null) {
            return matePostRepository.findByUserIdOrderByIdDesc(userId, pageable);
        }
        return matePostRepository.findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, pageable);
    }

    public MatePost createMatePost(Long userId, String title, String content, LocalDate meetingDate) {
        validateMeetingDate(meetingDate);
        MatePost matePost = MatePost.create(userId, title, content, meetingDate);
        return matePostRepository.save(matePost);
    }

    public MatePost updateMatePost(Long postId, String title, String content, LocalDate meetingDate, Long userId) {
        MatePost matePost = matePostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        validateOwner(matePost.getUserId(), userId);
        validateMeetingDate(meetingDate);

        matePost.update(title, content, meetingDate);
        return matePostRepository.save(matePost);
    }

    public void deleteMatePost(Long postId, Long userId) {
        MatePost matePost = matePostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        validateOwner(matePost.getUserId(), userId);
        matePostRepository.deleteById(postId);
    }

    public MatePost updateMatePostAsAdmin(Long postId, String title, String content, LocalDate meetingDate) {
        MatePost matePost = matePostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        validateMeetingDate(meetingDate);
        matePost.update(title, content, meetingDate);
        return matePostRepository.save(matePost);
    }

    public void deleteMatePostAsAdmin(Long postId) {
        MatePost matePost = matePostRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        matePostRepository.delete(matePost);
    }

    private void validateOwner(Long ownerId, Long userId) {
        if (!ownerId.equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION, "게시글 작업은 작성자만 가능합니다.");
        }
    }

    private void validateMeetingDate(LocalDate meetingDate) {
        if (meetingDate == null) {
            throw new BusinessException(ErrorCode.MISSING_REQUIRED_FIELD, "동행 게시글에는 meetingDate가 필요합니다.");
        }

        if (meetingDate.isBefore(LocalDate.now())) {
            throw new BusinessException(ErrorCode.CONSTRAINT_VIOLATION, "meetingDate는 현재 시간보다 이후여야 합니다.");
        }
    }
}
