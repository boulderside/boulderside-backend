package com.line7studio.boulderside.domain.user.repository;

import com.line7studio.boulderside.domain.user.UserConsentHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConsentHistoryRepository extends JpaRepository<UserConsentHistory, Long> {
    List<UserConsentHistory> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}
