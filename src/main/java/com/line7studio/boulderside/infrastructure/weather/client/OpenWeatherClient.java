package com.line7studio.boulderside.infrastructure.weather.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.exception.ExternalApiException;
import com.line7studio.boulderside.infrastructure.weather.dto.OneCallResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenWeatherClient {
	private final WebClient webClient;

	@Value("${open.weather.baseUrl}")
	private String openWeatherBaseUrl;

	@Value("${open.weather.api.key}")
	private String openWeatherApiKey;

    @Retryable(retryFor = {Exception.class}, maxAttempts = 2, backoff = @Backoff(delay = 1000))
	public OneCallResponse getOneCallWeather(double latitude, double longitude) {
		if (latitude < -90 || latitude > 90) {
			throw new ExternalApiException(ErrorCode.LATITUDE_OUT_OF_RANGE);
		}
		if (longitude < -180 || longitude > 180) {
			throw new ExternalApiException(ErrorCode.LONGITUDE_OUT_OF_RANGE);
		}

		String uri = UriComponentsBuilder
			.fromUriString(openWeatherBaseUrl)
			.queryParam("lat", latitude)
			.queryParam("lon", longitude)
			.queryParam("exclude", "current,minutely,hourly,alerts")
			.queryParam("units", "metric")
			.queryParam("lang", "kr")
			.queryParam("appid", openWeatherApiKey)
			.build()
			.toUriString();

		return webClient
			.get()
			.uri(uri)
			.retrieve()
			.bodyToMono(OneCallResponse.class)
			.block();
	}
}
