package com.line7studio.boulderside.domain.notice;

import com.line7studio.boulderside.common.exception.InvalidValueException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "notices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 150, nullable = false)
    private String title;

    @Lob
    @Column(name = "content", length = 10000, nullable = false)
    private String content;

    @Column(name = "is_pinned", nullable = false)
    private boolean pinned;

    @Column(name = "view_count", nullable = false)
    private long viewCount;

    @Builder(access = AccessLevel.PRIVATE)
    private Notice(Long id, String title, String content, boolean pinned, Long viewCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.pinned = pinned;
        this.viewCount = viewCount == null ? 0L : viewCount;
    }

    public static Notice create(String title, String content, boolean pinned) {
        validateTitle(title);
        validateContent(content);
        return Notice.builder()
            .title(title)
            .content(content)
            .pinned(pinned)
            .viewCount(0L)
            .build();
    }

    public void update(String title, String content, boolean pinned) {
        validateTitle(title);
        validateContent(content);
        this.title = title;
        this.content = content;
        this.pinned = pinned;
    }

    public void increaseViewCount() {
        this.viewCount += 1;
    }

    private static void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidValueException(ErrorCode.MISSING_REQUIRED_FIELD, "공지 제목은 필수입니다.");
        }
        if (title.length() > 150) {
            throw new InvalidValueException(ErrorCode.INVALID_FIELD_LENGTH, "공지 제목은 150자를 초과할 수 없습니다.");
        }
    }

    private static void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidValueException(ErrorCode.MISSING_REQUIRED_FIELD, "공지 내용은 필수입니다.");
        }
    }
}
