package com.example.boulderside.application.weather.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.example.boulderside.external.weather.dto.WeatherResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherInfoResponse {
	private String city;
	private String country;
	private CurrentWeatherInfo current;
	private List<DailyWeatherInfo> daily;

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class CurrentWeatherInfo {
		private String timeLocal;
		private int temperature;
		private int feelTemperature;
		private String humidity;
		private String pm10;
		private String pm25;
		private String precipitation;
		private String windDirection;
		private String windSpeed;
		private String weatherCondition;
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class DailyWeatherInfo {
		private String date;
		private int maxTemperature;
		private int minTemperature;
		private int precipitationProbability;
		private String sunrise;
		private String sunset;
		private String weatherCondition;
		private String morningCondition;
		private String eveningCondition;
	}

	public static WeatherInfoResponse from(WeatherResponse weatherResponse) {
		CurrentWeatherInfo currentInfo = CurrentWeatherInfo.builder()
			.timeLocal(weatherResponse.getCurrent().getTimeLocal())
			.temperature(weatherResponse.getCurrent().getTemp())
			.feelTemperature(weatherResponse.getCurrent().getFeeltemp())
			.humidity(weatherResponse.getCurrent().getRhum())
			.pm10(weatherResponse.getCurrent().getPm10())
			.pm25(weatherResponse.getCurrent().getPm25())
			.precipitation(weatherResponse.getCurrent().getPrec())
			.windDirection(weatherResponse.getCurrent().getWdir())
			.windSpeed(weatherResponse.getCurrent().getWspd())
			.weatherCondition(weatherResponse.getCurrent().getWx())
			.build();

		List<DailyWeatherInfo> dailyInfos = weatherResponse.getDaily().stream()
			.map(daily -> DailyWeatherInfo.builder()
				.date(daily.getTimeLocal())
				.maxTemperature(daily.getTmax())
				.minTemperature(daily.getTmin())
				.precipitationProbability(daily.getPop())
				.sunrise(daily.getSunrise())
				.sunset(daily.getSunset())
				.weatherCondition(daily.getWx())
				.morningCondition(daily.getWxAm())
				.eveningCondition(daily.getWxPm())
				.build())
			.collect(Collectors.toList());

		return WeatherInfoResponse.builder()
			.city(weatherResponse.getCity())
			.country(weatherResponse.getCountry())
			.current(currentInfo)
			.daily(dailyInfos)
			.build();
	}
}