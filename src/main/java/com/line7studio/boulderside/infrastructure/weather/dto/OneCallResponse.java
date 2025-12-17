package com.line7studio.boulderside.infrastructure.weather.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OneCallResponse {

	private double lat;
	private double lon;
	private String timezone;

	@JsonProperty("timezone_offset")
	private int timezoneOffset;

	private CurrentWeather current;
	private List<HourlyWeather> hourly;
	private List<DailyWeather> daily;
	private List<Alert> alerts;

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CurrentWeather {
		private long dt;
		private long sunrise;
		private long sunset;
		private double temp;

		@JsonProperty("feels_like")
		private double feelsLike;

		private int pressure;
		private int humidity;

		@JsonProperty("dew_point")
		private double dewPoint;

		private double uvi;
		private int clouds;
		private int visibility;

		@JsonProperty("wind_speed")
		private double windSpeed;

		@JsonProperty("wind_deg")
		private int windDeg;

		@JsonProperty("wind_gust")
		private Double windGust;

		private List<Weather> weather;
	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class HourlyWeather {
		private long dt;
		private double temp;

		@JsonProperty("feels_like")
		private double feelsLike;

		private int pressure;
		private int humidity;

		@JsonProperty("dew_point")
		private double dewPoint;

		private double uvi;
		private int clouds;
		private int visibility;

		@JsonProperty("wind_speed")
		private double windSpeed;

		@JsonProperty("wind_deg")
		private int windDeg;

		@JsonProperty("wind_gust")
		private Double windGust;

		private List<Weather> weather;
		private double pop;
	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DailyWeather {
		private long dt;
		private long sunrise;
		private long sunset;
		private long moonrise;
		private long moonset;

		@JsonProperty("moon_phase")
		private double moonPhase;

		private String summary;
		private Temp temp;

		@JsonProperty("feels_like")
		private FeelsLike feelsLike;

		private int pressure;
		private int humidity;

		@JsonProperty("dew_point")
		private double dewPoint;

		@JsonProperty("wind_speed")
		private double windSpeed;

		@JsonProperty("wind_deg")
		private int windDeg;

		@JsonProperty("wind_gust")
		private Double windGust;

		private List<Weather> weather;
		private int clouds;
		private double pop;
		private Double rain;
		private Double snow;
		private double uvi;
	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Temp {
		private double day;
		private double min;
		private double max;
		private double night;
		private double eve;
		private double morn;
	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class FeelsLike {
		private double day;
		private double night;
		private double eve;
		private double morn;
	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Weather {
		private int id;
		private String main;
		private String description;
		private String icon;
	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Alert {
		@JsonProperty("sender_name")
		private String senderName;

		private String event;
		private long start;
		private long end;
		private String description;
		private List<String> tags;
	}
}