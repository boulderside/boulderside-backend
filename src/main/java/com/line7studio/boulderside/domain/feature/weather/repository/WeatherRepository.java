package com.line7studio.boulderside.domain.feature.weather.repository;

import com.line7studio.boulderside.domain.feature.weather.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    List<Weather> findAllByBoulderId(Long boulderId);
}
