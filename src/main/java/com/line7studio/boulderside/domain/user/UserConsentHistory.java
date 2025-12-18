package com.line7studio.boulderside.domain.user;

import com.line7studio.boulderside.domain.BaseEntity;
import com.line7studio.boulderside.domain.user.enums.ConsentType;
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
@Table(name = "user_consent_history")
public class UserConsentHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 사용자 ID */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** 동의 유형 */
    @Enumerated(EnumType.STRING)
    @Column(name = "consent_type", nullable = false)
    private ConsentType consentType;

    /** 동의 여부 */
    @Column(name = "agreed", nullable = false)
    private Boolean agreed;

    /** 약관 버전 */
    @Column(name = "consent_version")
    private String consentVersion;

    /** IP 주소 */
    @Column(name = "ip_address")
    private String ipAddress;
}
