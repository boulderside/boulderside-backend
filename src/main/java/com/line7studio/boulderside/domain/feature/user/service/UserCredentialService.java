package com.line7studio.boulderside.domain.feature.user.service;

import com.line7studio.boulderside.domain.feature.user.User;
import com.line7studio.boulderside.domain.feature.user.UserCredential;
import com.line7studio.boulderside.domain.feature.user.enums.AuthProviderType;
import com.line7studio.boulderside.domain.feature.user.repository.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCredentialService {
	private final UserCredentialRepository userCredentialRepository;

	@Transactional(readOnly = true)
	public Optional<UserCredential> findByProvider(AuthProviderType providerType, String providerUserId) {
		return userCredentialRepository.findByProviderTypeAndProviderUserId(providerType, providerUserId);
	}

	@Transactional(readOnly = true)
	public Optional<UserCredential> findByUserId(Long userId) {
		return userCredentialRepository.findByUserId(userId);
	}

	@Transactional
	public void createOAuthCredential(User user, AuthProviderType providerType, String providerUserId,
		String refreshToken) {
		UserCredential credential = UserCredential.create(user.getId(), providerType, providerUserId, refreshToken);
		userCredentialRepository.save(credential);
	}

	@Transactional
	public void updateOAuthCredential(UserCredential credential, String refreshToken) {
		credential.updateLoginSession(refreshToken);
		userCredentialRepository.save(credential);
	}
}
