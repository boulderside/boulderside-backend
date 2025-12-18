package com.line7studio.boulderside.domain.user;

import com.line7studio.boulderside.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_blocks",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_block_blocker_blocked", columnNames = {"blocker_id", "blocked_id"})
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserBlock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "blocker_id", nullable = false)
    private Long blockerId;

    @Column(name = "blocked_id", nullable = false)
    private Long blockedId;

    @Builder(access = AccessLevel.PRIVATE)
    private UserBlock(Long id, Long blockerId, Long blockedId) {
        this.id = id;
        this.blockerId = blockerId;
        this.blockedId = blockedId;
    }

    public static UserBlock create(Long blockerId, Long blockedId) {
        return UserBlock.builder()
            .blockerId(blockerId)
            .blockedId(blockedId)
            .build();
    }
}
