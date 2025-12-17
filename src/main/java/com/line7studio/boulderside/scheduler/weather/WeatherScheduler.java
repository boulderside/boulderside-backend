package com.line7studio.boulderside.scheduler.weather;

import com.line7studio.boulderside.usecase.weather.WeatherUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "scheduler.weather.enabled", havingValue = "true", matchIfMissing = true)
public class WeatherScheduler {
    private final WeatherUseCase weatherUseCase;

    /**
     * 매일 3시간마다 모든 바위 위치의 날씨 데이터를 가져와서 저장
     */
    @Scheduled(cron = "0 0 */3 * * *", zone = "Asia/Seoul")
    @SchedulerLock(name = "weatherDataFetch", lockAtMostFor = "10m", lockAtLeastFor = "1m")
    public void fetchWeatherData() {
        log.info("날씨 정보 업데이트 스케쥴러 실행");
        weatherUseCase.fetchAndSaveAllWeatherData();
        log.info("날씨 정보 업데이트 스케쥴러 실행 완료");
    }
}