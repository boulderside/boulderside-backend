package com.line7studio.boulderside.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FirebaseConfig {

	private final ResourceLoader resourceLoader;

	@Value("${firebase.credentials.path:}")
	private String credentialsPath;

	@PostConstruct
	public void initializeFirebase() throws IOException {
		if (credentialsPath == null || credentialsPath.isBlank()) {
			log.warn("Firebase credentials path is empty. FCM will be disabled.");
			return;
		}

		if (!FirebaseApp.getApps().isEmpty()) {
			return;
		}

		Resource resource = resourceLoader.getResource(credentialsPath);
		try (InputStream credentialsStream = resource.getInputStream()) {
			FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(credentialsStream))
				.build();
			FirebaseApp.initializeApp(options);
		}
	}
}
