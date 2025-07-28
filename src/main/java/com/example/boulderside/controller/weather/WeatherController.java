package com.example.boulderside.controller.weather;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.boulderside.application.weather.WeatherUseCase;
import com.example.boulderside.application.weather.dto.response.WeatherInfoResponse;
import com.example.boulderside.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@Tag(name = "Weather", description = "날씨 정보 API")
public class WeatherController {
	private final WeatherUseCase weatherUseCase;

	@GetMapping
	@Operation(summary = "날씨 정보 조회", description = "지역 코드로 현재 날씨와 14일 예보를 조회합니다.")
	public ResponseEntity<ApiResponse<WeatherInfoResponse>> getWeather(@RequestParam String code) {
		WeatherInfoResponse weatherInfo = weatherUseCase.getWeatherByCode(code);
		return ResponseEntity.ok(ApiResponse.of(weatherInfo));
	}
}