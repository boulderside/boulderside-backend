package com.line7studio.boulderside.domain.comment;

import com.line7studio.boulderside.domain.BaseEntity;
import com.line7studio.boulderside.domain.comment.enums.CommentDomainType;
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
    @Column(name = "comment_domain_type", nullable = false)
    private CommentDomainType commentDomainType;

    /** 댓글 내용 */
    @Column(name = "content")
    private String content;

    public void update(String content) {
        this.content = content;
    }
}