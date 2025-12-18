package com.line7studio.boulderside.domain.user;

import com.line7studio.boulderside.domain.BaseEntity;
import com.line7studio.boulderside.domain.user.enums.ConsentType;
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
@Table(name = "user_meta")
public class UserMeta extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 사용자 FK */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** 푸시 알림 ON/OFF */
    @Column(name = "push_enabled")
    private Boolean pushEnabled;

    /** 마케팅 수신 동의 */
    @Column(name = "marketing_agreed")
    private Boolean marketingAgreed;

    /** 마케팅 수신 동의 생성 시각 */
    @Column(name = "marketing_agreed_created_at")
    private LocalDateTime marketingAgreedCreatedAt;

    /** 마케팅 수신 동의 변경 시각 */
    @Column(name = "marketing_agreed_updated_at")
    private LocalDateTime marketingAgreedUpdatedAt;

    /** 개인정보 수집 및 활용 동의 */
    @Column(name = "privacy_agreed")
    private Boolean privacyAgreed;

    /** 개인정보 수집 및 활용 동의 생성 시각 */
    @Column(name = "privacy_agreed_created_at")
    private LocalDateTime privacyAgreedCreatedAt;

    /** 서비스 이용 약관 동의 */
    @Column(name = "service_terms_agreed")
    private Boolean serviceTermsAgreed;

    /** 서비스 이용 약관 동의 생성 시각 */
    @Column(name = "service_terms_agreed_created_at")
    private LocalDateTime serviceTermsAgreedCreatedAt;

    /** 14세 이상 동의 */
    @Column(name = "over_fourteen_agreed")
    private Boolean overFourteenAgreed;

    /** 14세 이상 동의 생성 시각 */
    @Column(name = "over_fourteen_agreed_created_at")
    private LocalDateTime overFourteenAgreedCreatedAt;

    public void updateConsent(ConsentType type, boolean agreed) {
        LocalDateTime now = LocalDateTime.now();
        switch (type) {
            case PUSH:
                this.pushEnabled = agreed;
                break;
            case MARKETING:
                this.marketingAgreed = agreed;
                this.marketingAgreedUpdatedAt = now;
                break;
            case PRIVACY:
                this.privacyAgreed = agreed;
                break;
            case SERVICE_TERMS:
                this.serviceTermsAgreed = agreed;
                break;
            case OVER_FOURTEEN:
                this.overFourteenAgreed = agreed;
                break;
        }
    }
}
