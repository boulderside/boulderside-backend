package com.line7studio.boulderside.application.weather;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.line7studio.boulderside.application.weather.dto.DailyWeatherInfoDto;
import com.line7studio.boulderside.common.util.TimeUtil;
import com.line7studio.boulderside.external.weather.client.OpenWeatherClient;
import com.line7studio.boulderside.external.weather.dto.OneCallResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeatherUseCase {
	private final OpenWeatherClient openWeatherClient;

	public List<DailyWeatherInfoDto> getWeatherInfo(double latitude, double longitude) {
		OneCallResponse oneCallResponse = openWeatherClient.getOneCallWeather(latitude, longitude);

		if (oneCallResponse.getDaily() == null) {
			return List.of();
		}

		return oneCallResponse.getDaily().stream()
			.map(this::transformToWeatherSummary)
			.collect(Collectors.toList());
	}

	private DailyWeatherInfoDto transformToWeatherSummary(OneCallResponse.DailyWeather daily) {
		String summary = StringUtils.hasText(daily.getSummary())
			? daily.getSummary()
			: (daily.getWeather() != null && !daily.getWeather().isEmpty()
			? daily.getWeather().getFirst().getDescription()
			: "");

		String weatherIcon = daily.getWeather() != null && !daily.getWeather().isEmpty()
			? daily.getWeather().getFirst().getIcon()
			: "";

		String weatherId = daily.getWeather() != null && !daily.getWeather().isEmpty()
			? String.valueOf(daily.getWeather().getFirst().getId())
			: "";

		String weatherMain = daily.getWeather() != null && !daily.getWeather().isEmpty()
			? String.valueOf(daily.getWeather().getFirst().getMain())
			: "";

		String weatherDescription = daily.getWeather() != null && !daily.getWeather().isEmpty()
			? String.valueOf(daily.getWeather().getFirst().getDescription())
			: "";

		OneCallResponse.Temp temp = daily.getTemp();

		double morn = temp != null ? temp.getMorn() : 0.0;
		double day = temp != null ? temp.getDay() : 0.0;
		double eve = temp != null ? temp.getEve() : 0.0;
		double night = temp != null ? temp.getNight() : 0.0;
		double min = temp != null ? temp.getMin() : 0.0;
		double max = temp != null ? temp.getMax() : 0.0;

		return DailyWeatherInfoDto.builder()
			.date(TimeUtil.toSeoulLocalDate(daily.getDt()))
			.summary(summary)
			.tempMorn(morn)
			.tempDay(day)
			.tempEve(eve)
			.tempNight(night)
			.tempMin(min)
			.tempMax(max)
			.humidity(daily.getHumidity())
			.windSpeed(daily.getWindSpeed())
			.rainVolume(daily.getRain() != null ? daily.getRain() : 0.0)
			.rainProbability(daily.getPop())
			.weatherId(weatherId)
			.weatherMain(weatherMain)
			.weatherDescription(weatherDescription)
			.weatherIcon(String.format("https://openweathermap.org/img/wn/%s@2x.png", weatherIcon))
			.build();
	}
}