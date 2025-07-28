package com.example.boulderside.external.weather;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.boulderside.common.exception.ErrorCode;
import com.example.boulderside.common.exception.ExternalApiException;
import com.example.boulderside.external.weather.dto.WeatherResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherApiClient {
	private final WebClient webClient;

	@Value("${weather.api.url}")
	private String apiUrl;

	public WeatherResponse getWeatherByCode(String code) {
		try {
			return webClient.get()
				.uri(apiUrl + "&loc={code}", code)
				.retrieve()
				.bodyToMono(WeatherResponse.class)
				.doOnError(ExternalApiException.class, ex ->
					log.error("Failed to fetch weather for code {}: {}", code, ex.getMessage())
				)
				.block();
		} catch (Exception e) {
			throw new ExternalApiException(ErrorCode.WEATHER_API_FAILED);
		}
	}
}
