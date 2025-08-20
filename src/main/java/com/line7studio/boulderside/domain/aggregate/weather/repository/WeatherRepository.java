package com.line7studio.boulderside.domain.aggregate.weather.repository;

import com.line7studio.boulderside.domain.aggregate.weather.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    List<Weather> findAllByBoulderId(Long boulderId);
}
