package com.line7studio.boulderside.controller.weather;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.line7studio.boulderside.application.weather.WeatherUseCase;
import com.line7studio.boulderside.application.weather.dto.DailyWeatherInfoDto;
import com.line7studio.boulderside.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {
	private final WeatherUseCase weatherUseCase;

	@GetMapping
	public ResponseEntity<ApiResponse<List<DailyWeatherInfoDto>>> getWeatherInfo(
		@RequestParam double latitude,
		@RequestParam double longitude) {
		List<DailyWeatherInfoDto> dailyWeatherInfoDtoList = weatherUseCase.getWeatherInfo(latitude, longitude);
		return ResponseEntity.ok(ApiResponse.of(dailyWeatherInfoDtoList));
	}
}