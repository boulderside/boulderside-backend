package com.line7studio.boulderside.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.line7studio.boulderside.domain.user.User;
import com.line7studio.boulderside.domain.user.enums.AuthProviderType;

public interface UserRepository extends JpaRepository<User, Long> {
	List<User> findAllByIdIn(List<Long> userIdList);

	boolean existsByNickname(String nickname);

	Optional<User> findByProviderTypeAndProviderUserId(AuthProviderType providerType, String providerUserId);

	@Query("""
		select u.fcmToken
		from User u
		where u.fcmToken is not null
			and u.fcmToken <> ''
			and u.pushEnabled = true
		""")
	List<String> findAllFcmTokens();

	@Query("""
		select u.fcmToken
		from User u
		where u.id = :userId
			and u.fcmToken is not null
			and u.fcmToken <> ''
			and u.pushEnabled = true
		""")
	Optional<String> findFcmTokenByUserIdAndPushEnabled(@Param("userId") Long userId);
}
