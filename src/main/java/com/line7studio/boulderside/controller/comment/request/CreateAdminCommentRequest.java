package com.line7studio.boulderside.controller.comment.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateAdminCommentRequest {
    @NotBlank
    private String domainType;

    @NotNull
    private Long domainId;

    @NotBlank
    private String content;

    private Long userId;
}
