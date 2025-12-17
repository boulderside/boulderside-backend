package com.line7studio.boulderside.domain.feature.post.entity;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.exception.InvalidValueException;
import com.line7studio.boulderside.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "board_posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "comment_count")
    private Long commentCount;

    @Builder(access = AccessLevel.PRIVATE)
    private BoardPost(Long id, Long userId, String title, String content, Long viewCount, Long commentCount) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount == null ? 0L : viewCount;
        this.commentCount = commentCount == null ? 0L : commentCount;
    }

    /**
     * 정적 팩토리 메서드 - 일반 게시글 생성
     */
    public static BoardPost create(Long userId, String title, String content) {
        validateUserId(userId);
        validateTitle(title);
        validateContent(content);

        return BoardPost.builder()
            .userId(userId)
            .title(title)
            .content(content)
            .viewCount(0L)
            .commentCount(0L)
            .build();
    }

    /**
     * 게시글 정보 업데이트 (검증 포함)
     */
    public void update(String title, String content) {
        validateTitle(title);
        validateContent(content);

        this.title = title;
        this.content = content;
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
            throw new BusinessException(ErrorCode.NO_PERMISSION, "게시글 작업은 작성자만 가능합니다.");
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
            throw new InvalidValueException(ErrorCode.MISSING_REQUIRED_FIELD, "작성자 ID는 필수입니다.");
        }
    }

    private static void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidValueException(ErrorCode.MISSING_REQUIRED_FIELD, "제목은 필수입니다.");
        }
        if (title.length() > 100) {
            throw new InvalidValueException(ErrorCode.INVALID_FIELD_LENGTH, "제목은 100자를 초과할 수 없습니다.");
        }
    }

    private static void validateContent(String content) {
        // content는 nullable이므로 null 체크만
        if (content != null && content.length() > 5000) {
            throw new InvalidValueException(ErrorCode.INVALID_FIELD_LENGTH, "내용은 5000자를 초과할 수 없습니다.");
        }
    }
}