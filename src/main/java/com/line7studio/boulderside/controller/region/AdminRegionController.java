package com.line7studio.boulderside.controller.region;

import com.line7studio.boulderside.usecase.region.RegionUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.controller.region.request.CreateAdminRegionRequest;
import com.line7studio.boulderside.controller.region.request.UpdateAdminRegionRequest;
import com.line7studio.boulderside.controller.region.response.RegionResponse;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping("/admin/regions")
@RequiredArgsConstructor
public class AdminRegionController {

    private final RegionUseCase regionUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RegionResponse>>> getRegions() {
        return ResponseEntity.ok(ApiResponse.of(regionUseCase.getRegions()));
    }

    @GetMapping("/{regionId}")
    public ResponseEntity<ApiResponse<RegionResponse>> getRegion(@PathVariable Long regionId) {
        return ResponseEntity.ok(ApiResponse.of(regionUseCase.getRegion(regionId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RegionResponse>> createRegion(
        @Valid @RequestBody CreateAdminRegionRequest request) {
        RegionResponse response = regionUseCase.createRegion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PutMapping("/{regionId}")
    public ResponseEntity<ApiResponse<RegionResponse>> updateRegion(
        @PathVariable Long regionId,
        @Valid @RequestBody UpdateAdminRegionRequest request) {
        RegionResponse response = regionUseCase.updateRegion(regionId, request);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @DeleteMapping("/{regionId}")
    public ResponseEntity<ApiResponse<Void>> deleteRegion(@PathVariable Long regionId) {
        regionUseCase.deleteRegion(regionId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
