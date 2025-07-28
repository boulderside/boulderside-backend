package com.example.boulderside.application.weather;

import org.springframework.stereotype.Service;

import com.example.boulderside.application.weather.dto.response.WeatherInfoResponse;
import com.example.boulderside.external.weather.WeatherApiClient;
import com.example.boulderside.external.weather.dto.WeatherResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeatherUseCase {
	private final WeatherApiClient weatherApiClient;

	public WeatherInfoResponse getWeatherByCode(String code) {
		WeatherResponse weatherResponse = weatherApiClient.getWeatherByCode(code);
		return WeatherInfoResponse.from(weatherResponse);
	}
}