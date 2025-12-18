package com.line7studio.boulderside.domain.user.repository;

import com.line7studio.boulderside.domain.user.UserLoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginHistoryRepository extends JpaRepository<UserLoginHistory, Long> {
}
