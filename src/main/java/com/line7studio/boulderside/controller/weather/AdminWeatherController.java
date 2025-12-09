package com.line7studio.boulderside.controller.weather;

import com.line7studio.boulderside.application.weather.WeatherUseCase;
import com.line7studio.boulderside.application.weather.dto.DailyWeatherInfoDto;
import com.line7studio.boulderside.common.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/weather")
@RequiredArgsConstructor
public class AdminWeatherController {

	private final WeatherUseCase weatherUseCase;

	@GetMapping
	public ResponseEntity<ApiResponse<List<DailyWeatherInfoDto>>> getWeatherInfo(
		@RequestParam Long boulderId) {
		List<DailyWeatherInfoDto> weatherInfo = weatherUseCase.getWeatherInfo(boulderId);
		return ResponseEntity.ok(ApiResponse.of(weatherInfo));
	}

	@PostMapping("/refresh")
	public ResponseEntity<ApiResponse<Void>> refreshWeatherData() {
		weatherUseCase.fetchAndSaveAllWeatherData();
		return ResponseEntity.ok(ApiResponse.success());
	}
}
