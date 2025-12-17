package com.line7studio.boulderside.controller.approach;

import com.line7studio.boulderside.usecase.approach.ApproachUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.controller.approach.request.CreateApproachRequest;
import com.line7studio.boulderside.controller.approach.request.UpdateApproachRequest;
import com.line7studio.boulderside.controller.approach.response.ApproachResponse;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/approaches")
@RequiredArgsConstructor
public class AdminApproachController {
    private final ApproachUseCase approachUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ApproachResponse>>> getAllApproaches() {
        List<ApproachResponse> responses = approachUseCase.getAllApproaches();
        return ResponseEntity.ok(ApiResponse.of(responses));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ApproachResponse>> createApproach(
        @Valid @RequestBody CreateApproachRequest request) {
        ApproachResponse response = approachUseCase.createApproach(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PutMapping("/{approachId}")
    public ResponseEntity<ApiResponse<ApproachResponse>> updateApproach(@PathVariable Long approachId,
        @Valid @RequestBody UpdateApproachRequest request) {
        ApproachResponse response = approachUseCase.updateApproach(approachId, request);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @DeleteMapping("/{approachId}")
    public ResponseEntity<ApiResponse<Void>> deleteApproach(@PathVariable Long approachId) {
        approachUseCase.deleteApproach(approachId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
