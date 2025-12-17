package com.line7studio.boulderside.domain.feature.user.repository;

import com.line7studio.boulderside.domain.feature.user.UserCredential;
import com.line7studio.boulderside.domain.feature.user.enums.AuthProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {
	Optional<UserCredential> findByProviderTypeAndProviderUserId(AuthProviderType providerType, String providerUserId);

	Optional<UserCredential> findByUserId(Long userId);
}
