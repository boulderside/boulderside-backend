package com.line7studio.boulderside.domain.weather;

import com.line7studio.boulderside.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "weathers")
public class Weather extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 연관 바위 ID (FK) */
    @Column(name = "boulder_id", nullable = false)
    private Long boulderId;

    /** 날짜 */
    @Column(name = "date")
    private LocalDate date;

    /** 요약 */
    @Column(name = "summary")
    private String summary;

    /** 아침 기온 */
    @Column(name = "temp_morn")
    private double tempMorn;

    /** 낮 기온 */
    @Column(name = "temp_day")
    private double tempDay;

    /** 저녁 기온 */
    @Column(name = "temp_eve")
    private double tempEve;

    /** 밤 기온 */
    @Column(name = "temp_night")
    private double tempNight;

    /** 최저 기온 */
    @Column(name = "temp_min")
    private double tempMin;

    /** 최고 기온 */
    @Column(name = "temp_max")
    private double tempMax;

    /** 습도 (%) */
    @Column(name = "humidity")
    private int humidity;

    /** 풍속 (m/s) */
    @Column(name = "wind_speed")
    private double windSpeed;

    /** 강수량 (mm) */
    @Column(name = "rain_volume")
    private Double rainVolume;

    /** 강수 확률 (%) */
    @Column(name = "rain_probability")
    private Double rainProbability;

    /** 날씨 아이콘 코드 */
    @Column(name = "weather_icon")
    private String weatherIcon;

    /** 날씨 식별자(ID) */
    @Column(name = "weather_id")
    private int weatherId;

    /** 날씨 주요 분류 (예: Clear, Clouds) */
    @Column(name = "weather_main")
    private String weatherMain;

    /** 날씨 상세 설명 */
    @Column(name = "weather_description")
    private String weatherDescription;
}
