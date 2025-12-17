package com.line7studio.boulderside.scheduler.weather;

import com.line7studio.boulderside.usecase.weather.WeatherUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "scheduler.weather.enabled", havingValue = "true", matchIfMissing = true)
public class WeatherBootstrapRunner implements ApplicationRunner {

	private final WeatherUseCase weatherUseCase;

	@Override
	public void run(ApplicationArguments args) {
		log.info("애플리케이션 기동 시 초기 날씨 데이터 동기화 시작");
		try {
			weatherUseCase.fetchAndSaveAllWeatherData();
			log.info("초기 날씨 데이터 동기화 완료");
		} catch (Exception e) {
			log.error("초기 날씨 데이터 동기화 중 오류 발생", e);
		}
	}
}
