package com.line7studio.boulderside.domain.comment;

import com.line7studio.boulderside.domain.BaseEntity;
import com.line7studio.boulderside.domain.comment.enums.CommentDomainType;
import com.line7studio.boulderside.domain.enums.PostStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** 사용자 ID */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** 도메인 ID */
    @Column(name = "domain_id", nullable = false)
    private Long domainId;

    /** 도메인 대상 타입 */
    @Enumerated(EnumType.STRING)
    @Column(name = "comment_domain_type", nullable = false, length = 20)
    private CommentDomainType commentDomainType;

    /** 댓글 내용 */
    @Column(name = "content", length = 1000)
    private String content;

    /** 댓글 상태 */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private PostStatus status = PostStatus.ACTIVE;

    public void update(String content) {
        this.content = content;
    }

    /**
     * 댓글 상태 변경 (관리자용)
     */
    public void updateStatus(PostStatus status) {
        this.status = status;
    }

    /**
     * 댓글 차단
     */
    public void block() {
        this.status = PostStatus.BLOCKED;
    }

    /**
     * 댓글 활성화
     */
    public void activate() {
        this.status = PostStatus.ACTIVE;
    }

    /**
     * 댓글 삭제 처리
     */
    public void delete() {
        this.status = PostStatus.DELETED;
    }
}