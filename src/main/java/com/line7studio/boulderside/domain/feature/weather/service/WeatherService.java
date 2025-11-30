package com.line7studio.boulderside.domain.feature.weather.service;

import com.line7studio.boulderside.domain.feature.weather.entity.Weather;

import java.util.List;

public interface WeatherService {
    List<Weather> getAllWeatherByBoulderId(Long boulderId);

    void deleteAll();

    void saveAll(List<Weather> allWeatherData);
}
