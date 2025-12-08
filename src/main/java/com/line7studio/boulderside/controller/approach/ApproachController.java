package com.line7studio.boulderside.controller.approach;

import com.line7studio.boulderside.application.approach.ApproachUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.controller.approach.response.ApproachResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/approaches")
@RequiredArgsConstructor
public class ApproachController {
    private final ApproachUseCase approachUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ApproachResponse>>> getApproachesByBoulderId(@RequestParam Long boulderId) {
        List<ApproachResponse> approacheList = approachUseCase.getApproachesByBoulderId(boulderId);
        return ResponseEntity.ok(ApiResponse.of(approacheList));
    }
}
