package com.line7studio.boulderside.domain.feature.post.entity;

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

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "view_count", nullable = false)
    private Long viewCount;

    @Column(name = "comment_count", nullable = false)
    private Long commentCount;

    @Column(name = "meeting_date", nullable = false)
    private LocalDate meetingDate;

    @Builder
    public MatePost(Long id, Long userId, String title, String content, Long viewCount, Long commentCount, LocalDate meetingDate) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount == null ? 0L : viewCount;
        this.commentCount = commentCount == null ? 0L : commentCount;
        this.meetingDate = meetingDate;
    }

    public static MatePost create(Long userId, String title, String content, LocalDate meetingDate) {
        return MatePost.builder()
            .userId(userId)
            .title(title)
            .content(content)
            .viewCount(0L)
            .commentCount(0L)
            .meetingDate(meetingDate)
            .build();
    }

    public void update(String title, String content, LocalDate meetingDate) {
        this.title = title;
        this.content = content;
        this.meetingDate = meetingDate;
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
}
