package com.line7studio.boulderside.domain.mate;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.exception.InvalidValueException;
import com.line7studio.boulderside.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "mate_posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatePost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "comment_count")
    private Long commentCount;

    @Column(name = "meeting_date")
    private LocalDate meetingDate;

    @Builder(access = AccessLevel.PRIVATE)
    private MatePost(Long id, Long userId, String title, String content, Long viewCount, Long commentCount, LocalDate meetingDate) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount == null ? 0L : viewCount;
        this.commentCount = commentCount == null ? 0L : commentCount;
        this.meetingDate = meetingDate;
    }

    /**
     * 정적 팩토리 메서드 - 동행 게시글 생성
     */
    public static MatePost create(Long userId, String title, String content, LocalDate meetingDate) {
        validateUserId(userId);
        validateTitle(title);
        validateContent(content);
        validateMeetingDate(meetingDate);

        return MatePost.builder()
            .userId(userId)
            .title(title)
            .content(content)
            .viewCount(0L)
            .commentCount(0L)
            .meetingDate(meetingDate)
            .build();
    }

    /**
     * 게시글 정보 업데이트 (검증 포함)
     */
    public void update(String title, String content, LocalDate meetingDate) {
        validateTitle(title);
        validateContent(content);
        validateMeetingDate(meetingDate);

        this.title = title;
        this.content = content;
        this.meetingDate = meetingDate;
    }

    /**
     * 소유자 확인
     */
    public boolean isOwner(Long userId) {
        return this.userId.equals(userId);
    }

    /**
     * 소유자 검증 (아니면 예외 발생)
     */
    public void verifyOwner(Long userId) {
        if (!isOwner(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }
    }

    public void incrementViewCount() {
        this.viewCount = this.viewCount + 1;
    }

    public void incrementCommentCount() {
        this.commentCount = this.commentCount + 1;
    }

    public void decrementCommentCount() {
        this.commentCount = this.commentCount <= 0 ? 0 : this.commentCount - 1;
    }

    // === Private 검증 메서드들 ===

    private static void validateUserId(Long userId) {
        if (userId == null) {
            throw new InvalidValueException(ErrorCode.MISSING_REQUIRED_FIELD);
        }
    }

    private static void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidValueException(ErrorCode.MISSING_REQUIRED_FIELD);
        }
        if (title.length() > 100) {
            throw new InvalidValueException(ErrorCode.INVALID_FIELD_LENGTH);
        }
    }

    private static void validateContent(String content) {
        // content는 nullable이므로 null 체크만
        if (content != null && content.length() > 5000) {
            throw new InvalidValueException(ErrorCode.INVALID_FIELD_LENGTH);
        }
    }

    private static void validateMeetingDate(LocalDate meetingDate) {
        if (meetingDate == null) {
            throw new InvalidValueException(ErrorCode.MISSING_REQUIRED_FIELD);
        }
        if (meetingDate.isBefore(LocalDate.now())) {
            throw new InvalidValueException(ErrorCode.INVALID_DATE_VALUE);
        }
    }
}