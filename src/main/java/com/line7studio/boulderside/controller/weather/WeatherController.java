package com.line7studio.boulderside.controller.weather;

import com.line7studio.boulderside.application.weather.WeatherUseCase;
import com.line7studio.boulderside.application.weather.dto.DailyWeatherInfoDto;
import com.line7studio.boulderside.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {
	private final WeatherUseCase weatherUseCase;

	@GetMapping
	public ResponseEntity<ApiResponse<List<DailyWeatherInfoDto>>> getWeatherInfo(
		@RequestParam Long boulderId) {
		List<DailyWeatherInfoDto> dailyWeatherInfoDtoList = weatherUseCase.getWeatherInfo(boulderId);
		return ResponseEntity.ok(ApiResponse.of(dailyWeatherInfoDtoList));
	}
}