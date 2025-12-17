package com.line7studio.boulderside.controller.sector;

import com.line7studio.boulderside.usecase.sector.SectorUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.controller.sector.request.CreateAdminSectorRequest;
import com.line7studio.boulderside.controller.sector.request.UpdateAdminSectorRequest;
import com.line7studio.boulderside.controller.sector.response.SectorResponse;
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
@RequestMapping("/admin/sectors")
@RequiredArgsConstructor
public class AdminSectorController {

    private final SectorUseCase sectorUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SectorResponse>>> getSectors() {
        return ResponseEntity.ok(ApiResponse.of(sectorUseCase.getSectors()));
    }

    @GetMapping("/{sectorId}")
    public ResponseEntity<ApiResponse<SectorResponse>> getSector(@PathVariable Long sectorId) {
        return ResponseEntity.ok(ApiResponse.of(sectorUseCase.getSector(sectorId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SectorResponse>> createSector(
        @Valid @RequestBody CreateAdminSectorRequest request) {
        SectorResponse response = sectorUseCase.createSector(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PutMapping("/{sectorId}")
    public ResponseEntity<ApiResponse<SectorResponse>> updateSector(
        @PathVariable Long sectorId,
        @Valid @RequestBody UpdateAdminSectorRequest request) {
        SectorResponse response = sectorUseCase.updateSector(sectorId, request);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @DeleteMapping("/{sectorId}")
    public ResponseEntity<ApiResponse<Void>> deleteSector(@PathVariable Long sectorId) {
        sectorUseCase.deleteSector(sectorId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
