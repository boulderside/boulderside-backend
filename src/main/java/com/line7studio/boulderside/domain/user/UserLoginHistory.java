package com.line7studio.boulderside.domain.user;

import com.line7studio.boulderside.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_login_history", indexes = {
    @Index(name = "idx_user_login_history_user_id", columnList = "user_id")
})
public class UserLoginHistory extends BaseEntity {

    /** 로그인 이력 ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 사용자 ID */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** 로그인 시도 IP 주소 */
    @Column(name = "ip_address")
    private String ipAddress;

    /** 사용자 브라우저 및 디바이스 정보 (User-Agent) */
    @Column(name = "user_agent")
    private String userAgent;

    /** 로그인 시각 */
    @Column(name = "login_at", nullable = false)
    private LocalDateTime loginAt;
}
