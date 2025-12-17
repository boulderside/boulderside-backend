package com.line7studio.boulderside.infrastructure.weather.mapper;

import com.line7studio.boulderside.usecase.weather.dto.DailyWeatherInfoDto;
import com.line7studio.boulderside.common.util.TimeUtil;
import com.line7studio.boulderside.infrastructure.weather.dto.OneCallResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WeatherApiMapper {

    public DailyWeatherInfoDto toDto(OneCallResponse.DailyWeather daily) {
        OneCallResponse.Temp tempDetails = daily.getTemp();
        OneCallResponse.Weather weatherDetails = daily.getWeather().getFirst();
        String summary = daily.getSummary();

        return DailyWeatherInfoDto.builder()
                .date(TimeUtil.toSeoulLocalDate(daily.getDt()))
                .summary(summary)
                .tempMorn(tempDetails.getMorn())
                .tempDay(tempDetails.getDay())
                .tempEve(tempDetails.getEve())
                .tempNight(tempDetails.getNight())
                .tempMin(tempDetails.getMin())
                .tempMax(tempDetails.getMax())
                .humidity(daily.getHumidity())
                .windSpeed(daily.getWindSpeed())
                .rainVolume(daily.getRain() != null ? daily.getRain() : 0)
                .rainProbability(daily.getPop())
                .weatherId(weatherDetails.getId())
                .weatherMain(weatherDetails.getMain())
                .weatherDescription(weatherDetails.getDescription())
                .weatherIcon(formatWeatherIconUrl(weatherDetails.getIcon()))
                .build();
    }

    private String formatWeatherIconUrl(String weatherIcon) {
        return weatherIcon.isEmpty() ? "" :
                String.format("https://openweathermap.org/img/wn/%s@2x.png", weatherIcon);
    }
}
