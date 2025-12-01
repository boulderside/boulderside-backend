package com.line7studio.boulderside.domain.feature.post.entity;

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

    @Column(name = "view_count", nullable = false)
    private Long viewCount;

    @Column(name = "comment_count", nullable = false)
    private Long commentCount;

    @Builder
    public BoardPost(Long id, Long userId, String title, String content, Long viewCount, Long commentCount) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount == null ? 0L : viewCount;
        this.commentCount = commentCount == null ? 0L : commentCount;
    }

    public static BoardPost create(Long userId, String title, String content) {
        return BoardPost.builder()
            .userId(userId)
            .title(title)
            .content(content)
            .viewCount(0L)
            .commentCount(0L)
            .build();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
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
