package com.line7studio.boulderside.infrastructure.elasticsearch.document;

import com.line7studio.boulderside.common.enums.Level;
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
@Document(indexName = "routes")
public class RouteDocument {
    @Id
    private String id;

    // 검색 대상
    @Field(type = FieldType.Text, analyzer = "korean_autocomplete", searchAnalyzer = "korean_search")
    private String routeName;

    // 도메인 타입 (예: BOULDER, ROUTE, POST)
    @Field(type = FieldType.Keyword)
    private DocumentDomainType documentDomainType;

    // 난이도 (예: V3, V5, 5.11a 등)
    @Field(type = FieldType.Keyword)
    private Level level;

    // 좋아요 수
    @Field(type = FieldType.Integer)
    private Integer likeCount;

    // 등반자 수 (이 루트를 완등한 사용자 수)
    @Field(type = FieldType.Integer)
    private Integer climberCount;

    // 생성일 (최신순 정렬용)
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime createdAt;
}
