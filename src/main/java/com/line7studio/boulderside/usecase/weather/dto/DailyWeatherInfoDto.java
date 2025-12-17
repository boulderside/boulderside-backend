package com.line7studio.boulderside.usecase.weather.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DailyWeatherInfoDto {
	private LocalDate date;
	private String summary;
	private double tempMorn;
	private double tempDay;
	private double tempEve;
	private double tempNight;
	private double tempMin;
	private double tempMax;
	private int humidity;
	private double windSpeed;
	private Double rainVolume;
	private Double rainProbability;
	private String weatherIcon;
	private int weatherId;
	private String weatherMain;
	private String weatherDescription;
}