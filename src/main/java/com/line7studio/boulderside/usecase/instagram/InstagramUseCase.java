package com.line7studio.boulderside.usecase.instagram;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.dto.UserInfo;
import com.line7studio.boulderside.controller.instagram.request.CreateInstagramRequest;
import com.line7studio.boulderside.controller.instagram.request.UpdateInstagramRequest;
import com.line7studio.boulderside.controller.instagram.response.InstagramDetailResponse;
import com.line7studio.boulderside.controller.instagram.response.InstagramPageResponse;
import com.line7studio.boulderside.controller.instagram.response.InstagramResponse;
import com.line7studio.boulderside.controller.instagram.response.RouteInstagramPageResponse;
import com.line7studio.boulderside.controller.instagram.response.RouteInstagramResponse;
import com.line7studio.boulderside.domain.boulder.Boulder;
import com.line7studio.boulderside.domain.boulder.service.BoulderService;
import com.line7studio.boulderside.domain.instagram.Instagram;
import com.line7studio.boulderside.domain.instagram.RouteInstagram;
import com.line7studio.boulderside.domain.instagram.interaction.like.service.UserInstagramLikeService;
import com.line7studio.boulderside.domain.instagram.service.InstagramService;
import com.line7studio.boulderside.domain.instagram.service.RouteInstagramService;
import com.line7studio.boulderside.domain.route.Route;
import com.line7studio.boulderside.domain.route.service.RouteService;
import com.line7studio.boulderside.domain.user.User;
import com.line7studio.boulderside.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstagramUseCase {
	private static final int MAX_PAGE_SIZE = 50;
	private static final int DEFAULT_PAGE_SIZE = 10;

	private final InstagramService instagramService;
	private final RouteInstagramService routeInstagramService;
	private final RouteService routeService;
	private final UserService userService;
	private final UserInstagramLikeService userInstagramLikeService;
	private final BoulderService boulderService;

	@Transactional
	public InstagramResponse createInstagram(Long userId, CreateInstagramRequest request) {
		// Route 존재 확인
		if (request.routeIds() != null && !request.routeIds().isEmpty()) {
			request.routeIds().forEach(routeService::getById);
		}

		Instagram instagram = Instagram.create(request.url(), userId);
		Instagram savedInstagram = instagramService.save(instagram);

		// Route 연결
		linkRoutes(savedInstagram.getId(), request.routeIds());
		List<InstagramResponse.RouteInfo> routes = getRouteInfosByInstagramId(savedInstagram.getId());

		// User 정보 조회
		User user = userService.getUserById(userId);
		UserInfo userInfo = UserInfo.from(user);

		return InstagramResponse.of(savedInstagram, userInfo, routes);
	}

	@Transactional
	public InstagramResponse updateInstagram(Long userId, Long instagramId, UpdateInstagramRequest request) {
		Instagram instagram = instagramService.getById(instagramId);

		// 본인의 Instagram인지 확인
		if (!instagram.getUserId().equals(userId)) {
			throw new BusinessException(ErrorCode.NO_PERMISSION);
		}

		// Route 존재 확인
		if (request.routeIds() != null && !request.routeIds().isEmpty()) {
			request.routeIds().forEach(routeService::getById);
		}

		instagram.update(request.url(), userId);

		// 기존 Route 연결 삭제 후 새로 연결
		routeInstagramService.deleteByInstagramId(instagramId);
		linkRoutes(instagramId, request.routeIds());
		List<InstagramResponse.RouteInfo> routes = getRouteInfosByInstagramId(instagramId);

		// User 정보 조회
		User user = userService.getUserById(userId);
		UserInfo userInfo = UserInfo.from(user);

		return InstagramResponse.of(instagram, userInfo, routes);
	}

	private List<Long> linkRoutes(Long instagramId, List<Long> routeIds) {
		if (routeIds == null || routeIds.isEmpty()) {
			return List.of();
		}

		routeIds.forEach(routeId -> {
			if (!routeInstagramService.existsByRouteIdAndInstagramId(routeId, instagramId)) {
				RouteInstagram routeInstagram = RouteInstagram.create(routeId, instagramId);
				routeInstagramService.save(routeInstagram);
			}
		});

		return routeIds;
	}

	@Transactional
	public void deleteInstagram(Long userId, Long instagramId) {
		Instagram instagram = instagramService.getById(instagramId);

		// 본인의 Instagram인지 확인
		if (!instagram.getUserId().equals(userId)) {
			throw new BusinessException(ErrorCode.NO_PERMISSION);
		}

		// Instagram 삭제 시 연결된 RouteInstagram도 함께 삭제
		routeInstagramService.deleteByInstagramId(instagramId);
		instagramService.deleteById(instagramId);
	}

	// Admin 전용 메서드

	@Transactional
	public InstagramResponse createInstagramByAdmin(Long userId, String url, List<Long> routeIds) {
		// Route 존재 확인
		if (routeIds != null && !routeIds.isEmpty()) {
			routeIds.forEach(routeService::getById);
		}

		Instagram instagram = Instagram.create(url, userId);
		Instagram savedInstagram = instagramService.save(instagram);

		// Route 연결
		linkRoutes(savedInstagram.getId(), routeIds);
		List<InstagramResponse.RouteInfo> routes = getRouteInfosByInstagramId(savedInstagram.getId());

		// User 정보 조회
		User user = userService.getUserById(userId);
		UserInfo userInfo = UserInfo.from(user);

		return InstagramResponse.of(savedInstagram, userInfo, routes);
	}

	@Transactional
	public InstagramResponse updateInstagramByAdmin(Long instagramId, Long userId, String url, List<Long> routeIds) {
		Instagram instagram = instagramService.getById(instagramId);

		// Route 존재 확인
		if (routeIds != null && !routeIds.isEmpty()) {
			routeIds.forEach(routeService::getById);
		}

		instagram.update(url, userId);

		// 기존 Route 연결 삭제 후 새로 연결
		routeInstagramService.deleteByInstagramId(instagramId);
		linkRoutes(instagramId, routeIds);
		List<InstagramResponse.RouteInfo> routes = getRouteInfosByInstagramId(instagramId);

		// User 정보 조회
		User user = userService.getUserById(userId);
		UserInfo userInfo = UserInfo.from(user);

		return InstagramResponse.of(instagram, userInfo, routes);
	}

	@Transactional
	public void deleteInstagramByAdmin(Long instagramId) {
		// Admin은 권한 체크 없이 삭제 가능
		routeInstagramService.deleteByInstagramId(instagramId);
		instagramService.deleteById(instagramId);
	}

	@Transactional(readOnly = true)
	public InstagramResponse getInstagram(Long instagramId) {
		Instagram instagram = instagramService.getById(instagramId);
		List<InstagramResponse.RouteInfo> routes = getRouteInfosByInstagramId(instagramId);

		// User 정보 조회
		User user = userService.getUserById(instagram.getUserId());
		UserInfo userInfo = UserInfo.from(user);

		return InstagramResponse.of(instagram, userInfo, routes);
	}

	@Transactional(readOnly = true)
	public InstagramDetailResponse getInstagramDetail(Long instagramId, Long requestUserId) {
		// Instagram 정보 조회
		Instagram instagram = instagramService.getById(instagramId);

		// User 정보 조회
		User user = userService.getUserById(instagram.getUserId());
		UserInfo userInfo = UserInfo.from(user);

		// 연결된 Route 정보 조회
		List<RouteInstagram> routeInstagrams = routeInstagramService.findByInstagramId(instagramId);
		List<InstagramDetailResponse.RouteInfo> routes = routeInstagrams.stream()
			.map(ri -> {
				Route route = routeService.getById(ri.getRouteId());
				Boulder boulder = boulderService.getById(route.getBoulderId());
				return InstagramDetailResponse.RouteInfo.of(
					route.getId(),
					route.getName(),
					boulder.getName()
				);
			})
			.toList();

		// 좋아요 여부 조회
		Boolean isLiked = requestUserId != null
			? userInstagramLikeService.existsIsLikedByUserId(instagramId, requestUserId)
			: null;

		return InstagramDetailResponse.of(instagram, userInfo, routes, isLiked);
	}

	private List<Long> getRouteIdsByInstagramId(Long instagramId) {
		return routeInstagramService.findByInstagramId(instagramId).stream()
			.map(RouteInstagram::getRouteId)
			.collect(Collectors.toList());
	}

	private List<InstagramResponse.RouteInfo> getRouteInfosByInstagramId(Long instagramId) {
		return routeInstagramService.findByInstagramId(instagramId).stream()
			.map(ri -> {
				Route route = routeService.getById(ri.getRouteId());
				Boulder boulder = boulderService.getById(route.getBoulderId());
				return InstagramResponse.RouteInfo.of(
					route.getId(),
					route.getName(),
					boulder.getName()
				);
			})
			.toList();
	}

	@Transactional(readOnly = true)
	public List<InstagramResponse> getAllInstagrams() {
		List<Instagram> instagrams = instagramService.findAll();
		return instagrams.stream()
			.map(instagram -> {
				List<InstagramResponse.RouteInfo> routes = getRouteInfosByInstagramId(instagram.getId());
				User user = userService.getUserById(instagram.getUserId());
				UserInfo userInfo = UserInfo.from(user);
				return InstagramResponse.of(instagram, userInfo, routes);
			})
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public InstagramPageResponse getInstagramsPage(Long cursor, int size) {
		return getInstagramsPage(null, cursor, size);
	}

	@Transactional(readOnly = true)
	public InstagramPageResponse getInstagramsPage(Long userId, Long cursor, int size) {
		int pageSize = normalizeSize(size);
		List<Instagram> instagrams = instagramService.findAllWithCursor(cursor, pageSize + 1);

		boolean hasNext = instagrams.size() > pageSize;
		if (hasNext) {
			instagrams = instagrams.subList(0, pageSize);
		}
		Long nextCursor = hasNext && !instagrams.isEmpty() ? instagrams.get(instagrams.size() - 1).getId() : null;

		// 좋아요 여부 조회
		Map<Long, Boolean> likedMap = Map.of();
		if (userId != null && !instagrams.isEmpty()) {
			List<Long> instagramIds = instagrams.stream()
				.map(Instagram::getId)
				.toList();
			likedMap = userInstagramLikeService.getIsLikedByUserIdForInstagramList(instagramIds, userId);
		}

		Map<Long, Boolean> finalLikedMap = likedMap;
		List<InstagramResponse> content = instagrams.stream()
			.map(instagram -> {
				List<InstagramResponse.RouteInfo> routes = getRouteInfosByInstagramId(instagram.getId());
				User user = userService.getUserById(instagram.getUserId());
				UserInfo userInfo = UserInfo.from(user);
				Boolean isLiked = userId != null ? finalLikedMap.getOrDefault(instagram.getId(), false) : null;
				return InstagramResponse.of(instagram, userInfo, routes, isLiked);
			})
			.collect(Collectors.toList());

		return InstagramPageResponse.of(content, nextCursor, hasNext, pageSize);
	}

	@Transactional(readOnly = true)
	public List<InstagramResponse> getInstagramsByUserId(Long userId) {
		List<Instagram> instagrams = instagramService.findByUserId(userId);
		return instagrams.stream()
			.map(instagram -> {
				List<InstagramResponse.RouteInfo> routes = getRouteInfosByInstagramId(instagram.getId());
				User user = userService.getUserById(instagram.getUserId());
				UserInfo userInfo = UserInfo.from(user);
				return InstagramResponse.of(instagram, userInfo, routes);
			})
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public InstagramPageResponse getInstagramsByUserIdPage(Long userId, Long cursor, int size) {
		return getInstagramsByUserIdPage(userId, null, cursor, size);
	}

	@Transactional(readOnly = true)
	public InstagramPageResponse getInstagramsByUserIdPage(Long userId, Long requestUserId, Long cursor, int size) {
		int pageSize = normalizeSize(size);
		List<Instagram> instagrams = instagramService.findByUserIdWithCursor(userId, cursor, pageSize + 1);

		boolean hasNext = instagrams.size() > pageSize;
		if (hasNext) {
			instagrams = instagrams.subList(0, pageSize);
		}
		Long nextCursor = hasNext && !instagrams.isEmpty() ? instagrams.get(instagrams.size() - 1).getId() : null;

		// 좋아요 여부 조회
		Map<Long, Boolean> likedMap = Map.of();
		if (requestUserId != null && !instagrams.isEmpty()) {
			List<Long> instagramIds = instagrams.stream()
				.map(Instagram::getId)
				.toList();
			likedMap = userInstagramLikeService.getIsLikedByUserIdForInstagramList(instagramIds, requestUserId);
		}

		Map<Long, Boolean> finalLikedMap = likedMap;
		List<InstagramResponse> content = instagrams.stream()
			.map(instagram -> {
				List<InstagramResponse.RouteInfo> routes = getRouteInfosByInstagramId(instagram.getId());
				User user = userService.getUserById(instagram.getUserId());
				UserInfo userInfo = UserInfo.from(user);
				Boolean isLiked = requestUserId != null ? finalLikedMap.getOrDefault(instagram.getId(), false) : null;
				return InstagramResponse.of(instagram, userInfo, routes, isLiked);
			})
			.collect(Collectors.toList());

		return InstagramPageResponse.of(content, nextCursor, hasNext, pageSize);
	}

	// Route별 Instagram 조회

	@Transactional(readOnly = true)
	public List<RouteInstagramResponse> getInstagramsByRouteId(Long routeId) {
		List<RouteInstagram> routeInstagrams = routeInstagramService.findByRouteId(routeId);

		return routeInstagrams.stream()
			.map(ri -> {
				Instagram instagram = instagramService.getById(ri.getInstagramId());
				List<InstagramResponse.RouteInfo> routes = getRouteInfosByInstagramId(ri.getInstagramId());
				User user = userService.getUserById(instagram.getUserId());
				UserInfo userInfo = UserInfo.from(user);
				return RouteInstagramResponse.of(ri, instagram, userInfo, routes);
			})
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public RouteInstagramPageResponse getInstagramsByRouteIdPage(Long routeId, Long cursor, int size) {
		return getInstagramsByRouteIdPage(routeId, null, cursor, size);
	}

	@Transactional(readOnly = true)
	public RouteInstagramPageResponse getInstagramsByRouteIdPage(Long routeId, Long userId, Long cursor, int size) {
		int pageSize = normalizeSize(size);
		List<RouteInstagram> routeInstagrams = routeInstagramService.findByRouteIdWithCursor(routeId, cursor, pageSize + 1);

		boolean hasNext = routeInstagrams.size() > pageSize;
		if (hasNext) {
			routeInstagrams = routeInstagrams.subList(0, pageSize);
		}
		Long nextCursor = hasNext && !routeInstagrams.isEmpty() ? routeInstagrams.get(routeInstagrams.size() - 1).getId() : null;

		// 좋아요 여부 조회
		Map<Long, Boolean> likedMap = Map.of();
		if (userId != null && !routeInstagrams.isEmpty()) {
			List<Long> instagramIds = routeInstagrams.stream()
				.map(RouteInstagram::getInstagramId)
				.distinct()
				.toList();
			likedMap = userInstagramLikeService.getIsLikedByUserIdForInstagramList(instagramIds, userId);
		}

		Map<Long, Boolean> finalLikedMap = likedMap;
		List<RouteInstagramResponse> content = routeInstagrams.stream()
			.map(ri -> {
				Instagram instagram = instagramService.getById(ri.getInstagramId());
				List<InstagramResponse.RouteInfo> routes = getRouteInfosByInstagramId(ri.getInstagramId());
				User user = userService.getUserById(instagram.getUserId());
				UserInfo userInfo = UserInfo.from(user);
				Boolean isLiked = userId != null ? finalLikedMap.getOrDefault(ri.getInstagramId(), false) : null;
				return RouteInstagramResponse.of(ri, instagram, userInfo, routes, isLiked);
			})
			.collect(Collectors.toList());

		return RouteInstagramPageResponse.of(content, nextCursor, hasNext, pageSize);
	}

	@Transactional(readOnly = true)
	public List<RouteInstagramResponse> getRoutesByInstagramId(Long instagramId) {
		Instagram instagram = instagramService.getById(instagramId);
		List<RouteInstagram> routeInstagrams = routeInstagramService.findByInstagramId(instagramId);
		List<InstagramResponse.RouteInfo> routes = getRouteInfosByInstagramId(instagramId);

		User user = userService.getUserById(instagram.getUserId());
		UserInfo userInfo = UserInfo.from(user);

		return routeInstagrams.stream()
			.map(ri -> RouteInstagramResponse.of(ri, instagram, userInfo, routes))
			.collect(Collectors.toList());
	}

	private int normalizeSize(int size) {
		if (size <= 0) {
			return DEFAULT_PAGE_SIZE;
		}
		return Math.min(size, MAX_PAGE_SIZE);
	}
}