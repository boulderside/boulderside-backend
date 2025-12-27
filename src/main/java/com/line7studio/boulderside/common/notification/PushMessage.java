package com.line7studio.boulderside.common.notification;

import java.util.HashMap;
import java.util.Map;

public record PushMessage(String title, String body, NotificationTarget target) {
	public Map<String, String> toDataMap() {
		Map<String, String> data = new HashMap<>();
		if (target != null) {
			data.putAll(target.toDataMap());
		}
		return data;
	}
}
