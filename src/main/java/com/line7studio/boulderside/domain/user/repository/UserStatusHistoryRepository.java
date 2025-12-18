package com.line7studio.boulderside.domain.user.repository;

import com.line7studio.boulderside.domain.user.UserStatusHistory;
import com.line7studio.boulderside.domain.user.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStatusHistoryRepository extends JpaRepository<UserStatusHistory, Long> {
    List<UserStatusHistory> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<UserStatusHistory> findTopByUserIdAndNewStatusOrderByCreatedAtDesc(Long userId, UserStatus newStatus);
}
