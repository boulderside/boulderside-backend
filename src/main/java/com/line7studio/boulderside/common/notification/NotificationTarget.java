package com.line7studio.boulderside.common.notification;

import java.util.Map;

public record NotificationTarget(NotificationDomainType domainType, String domainId) {
	public Map<String, String> toDataMap() {
		return Map.of(
			"domainType", domainType.name(),
			"domainId", domainId
		);
	}
}
