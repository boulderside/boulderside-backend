package com.line7studio.boulderside.domain.feature.weather.service;

import com.line7studio.boulderside.domain.feature.weather.Weather;
import com.line7studio.boulderside.domain.feature.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherRepository weatherRepository;

    public List<Weather> getAllWeatherByBoulderId(Long boulderId) {
        return weatherRepository.findAllByBoulderId(boulderId);
    }

    @Transactional
    public void deleteAll() {
        weatherRepository.deleteAll();
    }

    @Transactional
    public void saveAll(List<Weather> weatherList) {
        weatherRepository.saveAll(weatherList);
    }
}
