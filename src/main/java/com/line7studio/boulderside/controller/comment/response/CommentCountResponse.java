package com.line7studio.boulderside.controller.comment.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentCountResponse {
    private Integer commentCount;

    public static CommentCountResponse of(Integer commentCount) {
        return new CommentCountResponse(commentCount);
    }
}
