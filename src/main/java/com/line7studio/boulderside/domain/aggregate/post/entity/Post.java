package com.line7studio.boulderside.domain.aggregate.post.entity;

import com.line7studio.boulderside.domain.BaseEntity;
import com.line7studio.boulderside.domain.aggregate.post.enums.PostType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** 사용자 ID */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** 게시글 제목 */
    @Column(name = "title", nullable = false)
    private String title;

    /** 게시글 내용 */
    @Column(name = "content")
    private String content;

    /** 게시글 유형 */
    @Enumerated(EnumType.STRING)
    @Column(name = "post_type", nullable = false)
    private PostType postType;

    /** 조회 수 */
    @Column(name = "view_count")
    private Long viewCount;

    /** 동행 날짜 */
    @Column(name = "meeting_date")
    private LocalDate meetingDate;

    /** 댓글 수 */
    @Column(name = "comment_count")
    private Long commentCount;

    public void update(String title, String content, PostType postType, LocalDate meetingDate) {
        this.title = title;
        this.content = content;
        this.postType = postType;
        this.meetingDate = meetingDate;
    }

    public void incrementViewCount() {
        this.viewCount = this.viewCount + 1;
    }
}