package com.line7studio.boulderside.domain.aggregate.weather.service;

import com.line7studio.boulderside.domain.aggregate.weather.entity.Weather;
import com.line7studio.boulderside.domain.aggregate.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final WeatherRepository weatherRepository;

    @Override
    public List<Weather> getAllWeatherByBoulderId(Long boulderId) {
        return weatherRepository.findAllByBoulderId(boulderId);
    }

    @Override
    @Transactional
    public void deleteAll() {
        weatherRepository.deleteAll();
    }

    @Override
    @Transactional
    public void saveAll(List<Weather> weatherList) {
        weatherRepository.saveAll(weatherList);
    }
}
