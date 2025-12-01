package com.line7studio.boulderside.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDetail {
    private String field;
    private String value;
    private String reason;

    public static ErrorDetail of(String field, String value, String reason) {
        return ErrorDetail.builder()
            .field(field)
            .value(value)
            .reason(reason)
            .build();
    }
}
