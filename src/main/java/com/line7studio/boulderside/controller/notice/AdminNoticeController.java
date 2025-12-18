package com.line7studio.boulderside.controller.notice;

import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.controller.notice.request.CreateNoticeRequest;
import com.line7studio.boulderside.controller.notice.request.UpdateNoticeRequest;
import com.line7studio.boulderside.controller.notice.response.NoticeResponse;
import com.line7studio.boulderside.usecase.notice.NoticeUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/notices")
@RequiredArgsConstructor
public class AdminNoticeController {

    private final NoticeUseCase noticeUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<NoticeResponse>> createNotice(
        @Valid @RequestBody CreateNoticeRequest request
    ) {
        NoticeResponse response = noticeUseCase.createNotice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PutMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<NoticeResponse>> updateNotice(
        @PathVariable Long noticeId,
        @Valid @RequestBody UpdateNoticeRequest request
    ) {
        NoticeResponse response = noticeUseCase.updateNotice(noticeId, request);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<Void>> deleteNotice(@PathVariable Long noticeId) {
        noticeUseCase.deleteNotice(noticeId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
