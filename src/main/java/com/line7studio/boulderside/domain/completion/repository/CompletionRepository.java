package com.line7studio.boulderside.domain.completion.repository;

import com.line7studio.boulderside.domain.completion.Completion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompletionRepository extends JpaRepository<Completion, Long> {
	Optional<Completion> findByIdAndUserId(Long id, Long userId);

	Optional<Completion> findByUserIdAndRouteId(Long userId, Long routeId);

	boolean existsByUserIdAndRouteId(Long userId, Long routeId);

	List<Completion> findByUserId(Long userId, Pageable pageable);

	List<Completion> findByUserIdAndIdLessThan(Long userId, Long id, Pageable pageable);

	List<Completion> findAllByUserId(Long userId);
}
