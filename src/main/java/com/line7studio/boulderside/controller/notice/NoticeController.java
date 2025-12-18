package com.line7studio.boulderside.controller.notice;

import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.controller.notice.response.NoticePageResponse;
import com.line7studio.boulderside.controller.notice.response.NoticeResponse;
import com.line7studio.boulderside.usecase.notice.NoticeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeUseCase noticeUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<NoticePageResponse>> getNotices(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        NoticePageResponse response = noticeUseCase.getNoticePage(page, size);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<NoticeResponse>> getNotice(@PathVariable Long noticeId) {
        NoticeResponse response = noticeUseCase.getNotice(noticeId);
        return ResponseEntity.ok(ApiResponse.of(response));
    }
}
