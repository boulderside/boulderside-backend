package com.line7studio.boulderside.domain.completion.repository;

import com.line7studio.boulderside.common.enums.Level;
import com.line7studio.boulderside.domain.completion.Completion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CompletionRepository extends JpaRepository<Completion, Long> {
	Optional<Completion> findByIdAndUserId(Long id, Long userId);

	Optional<Completion> findByUserIdAndRouteId(Long userId, Long routeId);

	boolean existsByUserIdAndRouteId(Long userId, Long routeId);

	List<Completion> findAllByUserIdAndRouteIdIn(Long userId, List<Long> routeIds);

	List<Completion> findByUserId(Long userId, Pageable pageable);

	List<Completion> findByUserIdAndIdLessThan(Long userId, Long id, Pageable pageable);

	List<Completion> findAllByUserId(Long userId);

	List<Completion> findAllByUserIdAndCompletedDate(Long userId, LocalDate completedDate);

	@Query("SELECT c FROM Completion c WHERE c.userId = :userId AND c.routeId IN (SELECT r.id FROM Route r WHERE r.routeLevel = :level)")
	List<Completion> findAllByUserIdAndRouteLevel(@Param("userId") Long userId, @Param("level") Level level);
}
