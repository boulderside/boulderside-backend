package com.line7studio.boulderside.usecase.instagram;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.controller.instagram.request.CreateInstagramRequest;
import com.line7studio.boulderside.controller.instagram.request.UpdateInstagramRequest;
import com.line7studio.boulderside.controller.instagram.response.InstagramPageResponse;
import com.line7studio.boulderside.controller.instagram.response.InstagramResponse;
import com.line7studio.boulderside.controller.instagram.response.RouteInstagramPageResponse;
import com.line7studio.boulderside.controller.instagram.response.RouteInstagramResponse;
import com.line7studio.boulderside.domain.instagram.Instagram;
import com.line7studio.boulderside.domain.instagram.RouteInstagram;
import com.line7studio.boulderside.domain.instagram.service.InstagramService;
import com.line7studio.boulderside.domain.instagram.service.RouteInstagramService;
import com.line7studio.boulderside.domain.route.service.RouteService;
import com.line7studio.boulderside.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

	@Transactional
	public InstagramResponse createInstagram(Long userId, CreateInstagramRequest request) {
		// Route 존재 확인
		if (request.routeIds() != null && !request.routeIds().isEmpty()) {
			request.routeIds().forEach(routeService::getById);
		}

		Instagram instagram = Instagram.create(request.url(), userId);
		Instagram savedInstagram = instagramService.save(instagram);

		// Route 연결
		List<Long> routeIds = linkRoutes(savedInstagram.getId(), request.routeIds());

		return InstagramResponse.of(savedInstagram, routeIds);
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
		List<Long> routeIds = linkRoutes(instagramId, request.routeIds());

		return InstagramResponse.of(instagram, routeIds);
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
		List<Long> linkedRouteIds = linkRoutes(savedInstagram.getId(), routeIds);

		return InstagramResponse.of(savedInstagram, linkedRouteIds);
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
		List<Long> linkedRouteIds = linkRoutes(instagramId, routeIds);

		return InstagramResponse.of(instagram, linkedRouteIds);
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
		List<Long> routeIds = getRouteIdsByInstagramId(instagramId);
		return InstagramResponse.of(instagram, routeIds);
	}

	private List<Long> getRouteIdsByInstagramId(Long instagramId) {
		return routeInstagramService.findByInstagramId(instagramId).stream()
			.map(RouteInstagram::getRouteId)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<InstagramResponse> getAllInstagrams() {
		List<Instagram> instagrams = instagramService.findAll();
		return instagrams.stream()
			.map(instagram -> {
				List<Long> routeIds = getRouteIdsByInstagramId(instagram.getId());
				return InstagramResponse.of(instagram, routeIds);
			})
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public InstagramPageResponse getInstagramsPage(Long cursor, int size) {
		int pageSize = normalizeSize(size);
		List<Instagram> instagrams = instagramService.findAllWithCursor(cursor, pageSize + 1);

		boolean hasNext = instagrams.size() > pageSize;
		if (hasNext) {
			instagrams = instagrams.subList(0, pageSize);
		}
		Long nextCursor = hasNext && !instagrams.isEmpty() ? instagrams.get(instagrams.size() - 1).getId() : null;

		List<InstagramResponse> content = instagrams.stream()
			.map(instagram -> {
				List<Long> routeIds = getRouteIdsByInstagramId(instagram.getId());
				return InstagramResponse.of(instagram, routeIds);
			})
			.collect(Collectors.toList());

		return InstagramPageResponse.of(content, nextCursor, hasNext, pageSize);
	}

	@Transactional(readOnly = true)
	public List<InstagramResponse> getInstagramsByUserId(Long userId) {
		List<Instagram> instagrams = instagramService.findByUserId(userId);
		return instagrams.stream()
			.map(instagram -> {
				List<Long> routeIds = getRouteIdsByInstagramId(instagram.getId());
				return InstagramResponse.of(instagram, routeIds);
			})
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public InstagramPageResponse getInstagramsByUserIdPage(Long userId, Long cursor, int size) {
		int pageSize = normalizeSize(size);
		List<Instagram> instagrams = instagramService.findByUserIdWithCursor(userId, cursor, pageSize + 1);

		boolean hasNext = instagrams.size() > pageSize;
		if (hasNext) {
			instagrams = instagrams.subList(0, pageSize);
		}
		Long nextCursor = hasNext && !instagrams.isEmpty() ? instagrams.get(instagrams.size() - 1).getId() : null;

		List<InstagramResponse> content = instagrams.stream()
			.map(instagram -> {
				List<Long> routeIds = getRouteIdsByInstagramId(instagram.getId());
				return InstagramResponse.of(instagram, routeIds);
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
				List<Long> routeIds = getRouteIdsByInstagramId(ri.getInstagramId());
				return RouteInstagramResponse.of(ri, instagram, routeIds);
			})
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public RouteInstagramPageResponse getInstagramsByRouteIdPage(Long routeId, Long cursor, int size) {
		int pageSize = normalizeSize(size);
		List<RouteInstagram> routeInstagrams = routeInstagramService.findByRouteIdWithCursor(routeId, cursor, pageSize + 1);

		boolean hasNext = routeInstagrams.size() > pageSize;
		if (hasNext) {
			routeInstagrams = routeInstagrams.subList(0, pageSize);
		}
		Long nextCursor = hasNext && !routeInstagrams.isEmpty() ? routeInstagrams.get(routeInstagrams.size() - 1).getId() : null;

		List<RouteInstagramResponse> content = routeInstagrams.stream()
			.map(ri -> {
				Instagram instagram = instagramService.getById(ri.getInstagramId());
				List<Long> routeIds = getRouteIdsByInstagramId(ri.getInstagramId());
				return RouteInstagramResponse.of(ri, instagram, routeIds);
			})
			.collect(Collectors.toList());

		return RouteInstagramPageResponse.of(content, nextCursor, hasNext, pageSize);
	}

	@Transactional(readOnly = true)
	public List<RouteInstagramResponse> getRoutesByInstagramId(Long instagramId) {
		Instagram instagram = instagramService.getById(instagramId);
		List<RouteInstagram> routeInstagrams = routeInstagramService.findByInstagramId(instagramId);
		List<Long> routeIds = getRouteIdsByInstagramId(instagramId);

		return routeInstagrams.stream()
			.map(ri -> RouteInstagramResponse.of(ri, instagram, routeIds))
			.collect(Collectors.toList());
	}

	private int normalizeSize(int size) {
		if (size <= 0) {
			return DEFAULT_PAGE_SIZE;
		}
		return Math.min(size, MAX_PAGE_SIZE);
	}
}