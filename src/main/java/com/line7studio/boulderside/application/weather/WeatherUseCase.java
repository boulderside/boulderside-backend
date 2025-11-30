package com.line7studio.boulderside.application.weather;

import com.line7studio.boulderside.application.weather.dto.DailyWeatherInfoDto;
import com.line7studio.boulderside.domain.feature.boulder.entity.Boulder;
import com.line7studio.boulderside.domain.feature.boulder.service.BoulderService;
import com.line7studio.boulderside.domain.feature.weather.entity.Weather;
import com.line7studio.boulderside.domain.feature.weather.mapper.WeatherMapper;
import com.line7studio.boulderside.domain.feature.weather.service.WeatherService;
import com.line7studio.boulderside.domain.feature.weather.external.client.OpenWeatherClient;
import com.line7studio.boulderside.domain.feature.weather.external.dto.OneCallResponse;
import com.line7studio.boulderside.domain.feature.weather.external.mapper.WeatherApiMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherUseCase {
	private final OpenWeatherClient openWeatherClient;

	private final BoulderService boulderService;
    private final WeatherService weatherService;
	
	private final Executor weatherExecutor = Executors.newFixedThreadPool(5);

    /**
     * 바위에 대한 날씨 정보를 db에서 조회
     */
    public List<DailyWeatherInfoDto> getWeatherInfo(Long boulderId) {
        List<Weather> weatherList = weatherService.getAllWeatherByBoulderId(boulderId);

        return weatherList.stream()
                .map(WeatherMapper::toDto)
                .toList();
    }
	
	/**
	 * 모든 바위 위치에 대한 날씨 정보를 외부 api를 통해 호출한 후 db에 저장
	 */
	public void fetchAndSaveAllWeatherData() {
		long startTime = System.currentTimeMillis();

        // 모든 바위 조회
        List<Boulder> boulderList = boulderService.getAllBoulders();

        // 날씨 정보 조회 (API - 병렬처리)
        List<CompletableFuture<List<Weather>>> futureList = new ArrayList<>();

        for (Boulder boulder : boulderList) {
            CompletableFuture<List<Weather>> future = CompletableFuture
                .supplyAsync(() -> fetchWeatherForBoulder(boulder), weatherExecutor)
                    .orTimeout(10, TimeUnit.SECONDS)
                .exceptionally(ex -> List.of());
            futureList.add(future);
        }

        // 날씨 정보 조회 API 호출이 완료되면 적재 (비동기)
        List<Weather> allWeatherList = futureList.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .toList();

        // 기존 날씨 정보 전체 삭제
        weatherService.deleteAll();

        // 새로운 날씨 정보 전체 저장
        weatherService.saveAll(allWeatherList);

        // 작업 소요 시간 로그
        long endTime = System.currentTimeMillis();
        log.info("날씨 정보 조회 스케쥴러 소요시간 : {} ms", endTime - startTime);
	}

	private List<Weather> fetchWeatherForBoulder(Boulder boulder) {
        OneCallResponse oneCallResponse = openWeatherClient.getOneCallWeather(
                boulder.getLatitude(), boulder.getLongitude());

        List<DailyWeatherInfoDto> dailyWeatherInfoDtoList = oneCallResponse.getDaily().stream()
                .map(WeatherApiMapper::toDto)
                .toList();

        return dailyWeatherInfoDtoList.stream()
            .map(dto -> WeatherMapper.toEntity(dto, boulder.getId()))
            .toList();
	}
}