package com.line7studio.boulderside.application.weather.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

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
	private String weatherId;
	private String weatherMain;
	private String weatherDescription;
}