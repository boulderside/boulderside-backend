package com.line7studio.boulderside.infrastructure.fcm;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.line7studio.boulderside.common.notification.PushMessage;

@Service
@Slf4j
public class FcmService {
	private static final int MAX_TOKENS_PER_BATCH = 500;

	public void sendMessage(String targetToken, String title, String body) {
		if (!isFirebaseInitialized()) {
			log.warn("Firebase is not initialized. Skip sending message.");
			return;
		}
		Message message = Message.builder()
			.setToken(targetToken)
			.setNotification(Notification.builder()
				.setTitle(title)
				.setBody(body)
				.build())
			.build();

		try {
			String response = FirebaseMessaging.getInstance().send(message);
			log.info("Successfully sent message: {}", response);
		} catch (FirebaseMessagingException e) {
			log.warn("Failed to send FCM message", e);
		}
	}

	public void sendMessageToAll(List<String> targetTokens, String title, String body) {
		PushMessage message = new PushMessage(title, body, null);
		sendMessageToAll(targetTokens, message);
	}

	public void sendMessageToAll(List<String> targetTokens, PushMessage payload) {
		if (!isFirebaseInitialized()) {
			log.warn("Firebase is not initialized. Skip sending messages.");
			return;
		}
		if (targetTokens == null || targetTokens.isEmpty()) {
			return;
		}

		Map<String, String> data = payload.toDataMap();
		for (int start = 0; start < targetTokens.size(); start += MAX_TOKENS_PER_BATCH) {
			int end = Math.min(start + MAX_TOKENS_PER_BATCH, targetTokens.size());
			List<String> batch = targetTokens.subList(start, end);
			MulticastMessage message = MulticastMessage.builder()
				.addAllTokens(batch)
				.setNotification(Notification.builder()
					.setTitle(payload.title())
					.setBody(payload.body())
					.build())
				.putAllData(data)
				.build();

			try {
				FirebaseMessaging.getInstance().sendEachForMulticast(message);
			} catch (FirebaseMessagingException e) {
				log.warn("Failed to send FCM multicast message", e);
			}
		}
	}

	private boolean isFirebaseInitialized() {
		return !FirebaseApp.getApps().isEmpty();
	}
}
