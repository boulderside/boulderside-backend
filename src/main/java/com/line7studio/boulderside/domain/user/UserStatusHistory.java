package com.line7studio.boulderside.domain.user;

import com.line7studio.boulderside.domain.BaseEntity;
import com.line7studio.boulderside.domain.user.enums.UserStatus;
import com.line7studio.boulderside.domain.user.enums.UserStatusChangeReason;
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
@Table(name = "user_status_history", indexes = {
    @Index(name = "idx_user_status_history_user_id", columnList = "user_id")
})
public class UserStatusHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 사용자 FK */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** 변경 전 상태 */
    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status", nullable = false)
    private UserStatus previousStatus;

    /** 변경 후 상태 */
    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false)
    private UserStatus newStatus;

    /** 변경 사유 유형 */
    @Enumerated(EnumType.STRING)
    @Column(name = "change_reason_type")
    private UserStatusChangeReason changeReasonType;

    /** 상세 변경 사유 (직접 입력 등) */
    @Column(name = "change_reason_detail")
    private String changeReasonDetail;

    /** 변경 주체 (ID) - 0 또는 null이면 시스템 */
    @Column(name = "changed_by")
    private Long changedBy;
}
