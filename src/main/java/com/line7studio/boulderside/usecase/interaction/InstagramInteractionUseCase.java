package com.line7studio.boulderside.usecase.interaction;

import com.line7studio.boulderside.common.notification.NotificationDomainType;
import com.line7studio.boulderside.common.notification.NotificationTarget;
import com.line7studio.boulderside.common.notification.PushMessage;
import com.line7studio.boulderside.controller.instagram.response.InstagramLikeResponse;
import com.line7studio.boulderside.controller.instagram.response.LikedInstagramItemResponse;
import com.line7studio.boulderside.controller.instagram.response.LikedInstagramPageResponse;
import com.line7studio.boulderside.domain.boulder.Boulder;
import com.line7studio.boulderside.domain.boulder.service.BoulderService;
import com.line7studio.boulderside.domain.instagram.Instagram;
import com.line7studio.boulderside.domain.instagram.RouteInstagram;
import com.line7studio.boulderside.domain.instagram.interaction.like.UserInstagramLike;
import com.line7studio.boulderside.domain.instagram.interaction.like.service.UserInstagramLikeService;
import com.line7studio.boulderside.domain.instagram.service.InstagramService;
import com.line7studio.boulderside.domain.instagram.service.RouteInstagramService;
import com.line7studio.boulderside.domain.route.Route;
import com.line7studio.boulderside.domain.route.service.RouteService;
import com.line7studio.boulderside.domain.user.User;
import com.line7studio.boulderside.domain.user.service.UserService;
import com.line7studio.boulderside.infrastructure.fcm.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InstagramInteractionUseCase {
	private static final int MAX_PAGE_SIZE = 50;

	private final InstagramService instagramService;
	private final UserInstagramLikeService userInstagramLikeService;
	private final RouteInstagramService routeInstagramService;
	private final RouteService routeService;
	private final BoulderService boulderService;
	private final UserService userService;
	private final FcmService fcmService;

	public InstagramLikeResponse toggleLike(Long userId, Long instagramId) {
		Instagram instagram = instagramService.getById(instagramId);

		UserInstagramLike userInstagramLike = UserInstagramLike.builder()
			.userId(userId)
			.instagramId(instagram.getId())
			.build();

		boolean liked = userInstagramLikeService.toggle(userInstagramLike);

		if (liked) {
			instagram.incrementLikeCount();
			publishInstagramLikePushAfterCommit(instagram, userId);
		} else {
			instagram.decrementLikeCount();
		}
		long likeCount = instagram.getLikeCount() != null ? instagram.getLikeCount() : 0L;

		return InstagramLikeResponse.of(instagramId, liked, likeCount);
	}

	@Transactional(readOnly = true)
	public LikedInstagramPageResponse getLikedInstagrams(Long userId, Long cursor, int size) {
		int pageSize = normalizeSize(size);
		List<UserInstagramLike> likes = userInstagramLikeService.getLikesByUser(userId, cursor, pageSize + 1);

		boolean hasNext = likes.size() > pageSize;
		if (hasNext) {
			likes = likes.subList(0, pageSize);
		}
		Long nextCursor = hasNext && !likes.isEmpty() ? likes.getLast().getId() : null;

		if (likes.isEmpty()) {
			return LikedInstagramPageResponse.of(Collections.emptyList(), nextCursor, hasNext, 0);
		}

		// Instagram 정보 조회
		List<Long> instagramIds = likes.stream()
			.map(UserInstagramLike::getInstagramId)
			.distinct()
			.toList();

		Map<Long, Instagram> instagramMap = instagramIds.stream()
			.map(instagramService::getById)
			.collect(Collectors.toMap(Instagram::getId, Function.identity()));

		// RouteInfo 조회
		Map<Long, List<LikedInstagramItemResponse.RouteInfo>> routeInfosMap = instagramIds.stream()
			.collect(Collectors.toMap(
				instagramId -> instagramId,
				this::getRouteInfosByInstagramId
			));

		// Response 생성
		List<LikedInstagramItemResponse> content = likes.stream()
			.map(like -> {
				Instagram instagram = instagramMap.get(like.getInstagramId());
				if (instagram == null) {
					return null;
				}

				List<LikedInstagramItemResponse.RouteInfo> routes = routeInfosMap.getOrDefault(
					like.getInstagramId(),
					Collections.emptyList()
				);
				return LikedInstagramItemResponse.of(like, instagram, routes);
			})
			.filter(Objects::nonNull)
			.toList();

		return LikedInstagramPageResponse.of(content, nextCursor, hasNext, content.size());
	}

	private int normalizeSize(int size) {
		if (size <= 0) {
			return 10;
		}
		return Math.min(size, MAX_PAGE_SIZE);
	}

	private List<Long> getRouteIdsByInstagramId(Long instagramId) {
		return routeInstagramService.findByInstagramId(instagramId).stream()
			.map(RouteInstagram::getRouteId)
			.collect(Collectors.toList());
	}

	private List<LikedInstagramItemResponse.RouteInfo> getRouteInfosByInstagramId(Long instagramId) {
		return routeInstagramService.findByInstagramId(instagramId).stream()
			.map(ri -> {
				Route route = routeService.getById(ri.getRouteId());
				Boulder boulder = boulderService.getById(route.getBoulderId());
				return LikedInstagramItemResponse.RouteInfo.of(
					route.getId(),
					route.getName(),
					boulder.getName()
				);
			})
			.toList();
	}

	private void publishInstagramLikePushAfterCommit(Instagram instagram, Long likerId) {
		if (!TransactionSynchronizationManager.isSynchronizationActive()) {
			sendInstagramLikePush(instagram, likerId);
			return;
		}
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
			@Override
			public void afterCommit() {
				sendInstagramLikePush(instagram, likerId);
			}
		});
	}

	private void sendInstagramLikePush(Instagram instagram, Long likerId) {
		// 좋아요를 누른 사람과 Instagram 작성자가 같으면 알림 발송하지 않음
		if (instagram.getUserId() == null || instagram.getUserId().equals(likerId)) {
			return;
		}

		// Instagram 작성자의 FCM 토큰 조회
		Optional<String> token = userService.getFcmTokenForPush(instagram.getUserId());
		if (token.isEmpty()) {
			return;
		}

		// 좋아요 누른 사용자 정보 조회
		User liker = userService.getUserById(likerId);
		String likerNickname = liker.getNickname();

		// 알림 메시지 생성 및 발송
		NotificationTarget target = new NotificationTarget(NotificationDomainType.INSTAGRAM, String.valueOf(instagram.getId()));
		String body = likerNickname + "님이 회원님의 게시글을 좋아합니다";
		PushMessage message = new PushMessage("내 인스타그램 좋아요", body, target);
		fcmService.sendMessageToAll(List.of(token.get()), message);
	}
}