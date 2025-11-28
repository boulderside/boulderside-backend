package com.line7studio.boulderside.controller.approach;

import com.line7studio.boulderside.application.approach.ApproachUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.controller.approach.request.CreateApproachRequest;
import com.line7studio.boulderside.controller.approach.response.ApproachResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/approaches")
@RequiredArgsConstructor
public class ApproachController {
    private final ApproachUseCase approachUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<ApproachResponse>> createApproach(@Valid @RequestBody CreateApproachRequest request) {
        ApproachResponse approachResponse = approachUseCase.createApproach(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(approachResponse));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ApproachResponse>>> getApproachesByBoulderId(@RequestParam Long boulderId) {
        List<ApproachResponse> approacheList = approachUseCase.getApproachesByBoulderId(boulderId);
        return ResponseEntity.ok(ApiResponse.of(approacheList));
    }
}