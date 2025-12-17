package com.line7studio.boulderside.domain.feature.weather.mapper;

import com.line7studio.boulderside.usecase.weather.dto.DailyWeatherInfoDto;
import com.line7studio.boulderside.domain.feature.weather.entity.Weather;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class WeatherMapper {

    public static DailyWeatherInfoDto toDto(Weather entity) {
        return DailyWeatherInfoDto.builder()
                .date(entity.getDate())
                .summary(entity.getSummary())
                .tempMorn(entity.getTempMorn())
                .tempDay(entity.getTempDay())
                .tempEve(entity.getTempEve())
                .tempNight(entity.getTempNight())
                .tempMin(entity.getTempMin())
                .tempMax(entity.getTempMax())
                .humidity(entity.getHumidity())
                .windSpeed(entity.getWindSpeed())
                .rainVolume(entity.getRainVolume())
                .rainProbability(entity.getRainProbability())
                .weatherIcon(entity.getWeatherIcon())
                .weatherId(entity.getWeatherId())
                .weatherMain(entity.getWeatherMain())
                .weatherDescription(entity.getWeatherDescription())
                .build();
    }

    public static Weather toEntity(DailyWeatherInfoDto dto, Long boulderId) {
        return Weather.builder()
                .boulderId(boulderId)
                .date(dto.getDate())
                .summary(dto.getSummary())
                .tempMorn(dto.getTempMorn())
                .tempDay(dto.getTempDay())
                .tempEve(dto.getTempEve())
                .tempNight(dto.getTempNight())
                .tempMin(dto.getTempMin())
                .tempMax(dto.getTempMax())
                .humidity(dto.getHumidity())
                .windSpeed(dto.getWindSpeed())
                .rainVolume(dto.getRainVolume())
                .rainProbability(dto.getRainProbability())
                .weatherIcon(dto.getWeatherIcon())
                .weatherId(dto.getWeatherId())
                .weatherMain(dto.getWeatherMain())
                .weatherDescription(dto.getWeatherDescription())
                .build();
    }
}