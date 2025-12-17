package com.line7studio.boulderside.domain.user.repository;

import com.line7studio.boulderside.domain.user.UserMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserMetaRepository extends JpaRepository<UserMeta, Long> {
    Optional<UserMeta> findByUserId(Long userId);
}
