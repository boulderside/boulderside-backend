package com.example.boulderside.external.weather.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherResponse {
	private String city;
	private String code;
	private String country;
	private Current current;
	private List<Daily> daily;

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Current {
		@JsonProperty("TimeLocal")
		private String timeLocal;

		@JsonProperty("TimeUtc")
		private String timeUtc;

		private double dewpt;
		private int feeltemp;
		private String pm10;
		private String pm25;
		private String prec;
		private double press;
		private String rhum;
		private int temp;
		private int tmax;
		private int tmin;
		private String uv;
		private String visi;
		private String wdir;
		private String wspd;
		private String wx;
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Daily {
		@JsonProperty("TimeLocal")
		private String timeLocal;

		@JsonProperty("TimeUtc")
		private String timeUtc;

		private int day;
		private int mon;
		private String moonrise;
		private String moonset;
		private int pop;

		@JsonProperty("pop_am")
		private int popAm;

		@JsonProperty("pop_pm")
		private int popPm;

		private String prec;
		private double press;
		private int rhum;
		private String sunrise;
		private String sunset;
		private int tmax;
		private int tmin;
		private String uv;
		private int wday;
		private int wdir;
		private double wspd;
		private String wx;

		@JsonProperty("wx_am")
		private String wxAm;

		@JsonProperty("wx_pm")
		private String wxPm;
	}
}