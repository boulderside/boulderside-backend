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

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "posts")
public class PostDocument {
    @Id
    private String id;

    // 검색 대상
    @Field(type = FieldType.Text, analyzer = "korean_autocomplete", searchAnalyzer = "korean_search")
    private String title;

    // 도메인 타입 (예: BOULDER, ROUTE, POST)
    @Field(type = FieldType.Keyword)
    private DocumentDomainType documentDomainType;

    // 동행 게시글일 경우 만남 날짜
    @Field(type = FieldType.Date, format = DateFormat.date)
    private LocalDate meetingDate;

    // 작성자 이름
    @Field(type = FieldType.Keyword)
    private String authorName;

    // 조회수
    @Field(type = FieldType.Long)
    private Long viewCount;

    // 댓글 수
    @Field(type = FieldType.Long)
    private Long commentCount;

    // 작성 시간
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime createdAt;
}
