package com.line7studio.boulderside.domain.instagram.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.instagram.RouteInstagram;
import com.line7studio.boulderside.domain.instagram.repository.RouteInstagramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteInstagramService {
	private final RouteInstagramRepository routeInstagramRepository;

	public RouteInstagram save(RouteInstagram routeInstagram) {
		return routeInstagramRepository.save(routeInstagram);
	}

	public RouteInstagram getById(Long id) {
		return routeInstagramRepository.findById(id)
			.orElseThrow(() -> new BusinessException(ErrorCode.ROUTE_INSTAGRAM_NOT_FOUND));
	}

	public List<RouteInstagram> findByRouteId(Long routeId) {
		return routeInstagramRepository.findByRouteId(routeId);
	}

	public List<RouteInstagram> findByRouteIdWithCursor(Long routeId, Long cursor, int size) {
		return routeInstagramRepository.findByRouteIdWithCursor(routeId, cursor, PageRequest.of(0, size));
	}

	public List<RouteInstagram> findByInstagramId(Long instagramId) {
		return routeInstagramRepository.findByInstagramId(instagramId);
	}

	public boolean existsByRouteIdAndInstagramId(Long routeId, Long instagramId) {
		return routeInstagramRepository.existsByRouteIdAndInstagramId(routeId, instagramId);
	}

	public void deleteById(Long id) {
		if (!routeInstagramRepository.existsById(id)) {
			throw new BusinessException(ErrorCode.ROUTE_INSTAGRAM_NOT_FOUND);
		}
		routeInstagramRepository.deleteById(id);
	}

	public void deleteByRouteId(Long routeId) {
		routeInstagramRepository.deleteByRouteId(routeId);
	}

	public void deleteByInstagramId(Long instagramId) {
		routeInstagramRepository.deleteByInstagramId(instagramId);
	}

	public long countByRouteId(Long routeId) {
		return routeInstagramRepository.countByRouteId(routeId);
	}
}