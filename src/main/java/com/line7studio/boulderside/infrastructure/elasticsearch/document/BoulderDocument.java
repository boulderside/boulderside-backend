package com.line7studio.boulderside.infrastructure.elasticsearch.document;

import com.line7studio.boulderside.infrastructure.elasticsearch.document.enums.DocumentDomainType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "boulders")
public class BoulderDocument {
    @Id
    private String id;

    // 검색 대상
    @Field(type = FieldType.Text, analyzer = "korean_autocomplete", searchAnalyzer = "korean_search")
    private String boulderName;

    // 도메인 타입 (예: BOULDER, ROUTE, POST)
    @Field(type = FieldType.Keyword)
    private DocumentDomainType documentDomainType;

    // 바위 대표 이미지 URL (검색 결과 카드 표시용)
    @Field(type = FieldType.Keyword)
    private String thumbnailUrl;

    // 지역 (예: "서울특별시", "경기도")
    @Field(type = FieldType.Keyword)
    private String province;

    // 시/군/구 (예: "성동구", "안양시")
    @Field(type = FieldType.Keyword)
    private String city;

    // 생성일 (최신순 정렬용)
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime createdAt;
}
