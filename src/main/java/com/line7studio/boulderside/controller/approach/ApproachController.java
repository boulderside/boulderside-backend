package com.line7studio.boulderside.controller.approach;

import com.line7studio.boulderside.usecase.approach.ApproachUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.controller.approach.response.ApproachResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApproachController {
    private final ApproachUseCase approachUseCase;

    @GetMapping("/boulders/{boulderId}/approaches")
    public ResponseEntity<ApiResponse<List<ApproachResponse>>> getApproachesByBoulderId(@PathVariable Long boulderId) {
        List<ApproachResponse> approacheList = approachUseCase.getApproachesByBoulderId(boulderId);
        return ResponseEntity.ok(ApiResponse.of(approacheList));
    }
}
