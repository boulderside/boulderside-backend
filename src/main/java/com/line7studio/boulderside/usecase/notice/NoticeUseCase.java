package com.line7studio.boulderside.usecase.notice;

import com.line7studio.boulderside.controller.notice.request.CreateNoticeRequest;
import com.line7studio.boulderside.controller.notice.request.UpdateNoticeRequest;
import com.line7studio.boulderside.controller.notice.response.NoticePageResponse;
import com.line7studio.boulderside.controller.notice.response.NoticeResponse;
import com.line7studio.boulderside.domain.notice.Notice;
import com.line7studio.boulderside.domain.notice.service.NoticeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeUseCase {

    private final NoticeService noticeService;

    public NoticePageResponse getNoticePage(int page, int size) {
        int normalizedPage = Math.max(page, 0);
        int normalizedSize = size <= 0 ? 10 : Math.min(size, 50);
        Page<Notice> noticePage = noticeService.getNoticePage(normalizedPage, normalizedSize);
        List<NoticeResponse> content = noticePage.getContent().stream()
            .map(NoticeResponse::from)
            .toList();
        return NoticePageResponse.of(content, normalizedPage, normalizedSize, noticePage.getTotalElements(), noticePage.hasNext());
    }

    @Transactional
    public NoticeResponse getNotice(Long noticeId) {
        Notice notice = noticeService.increaseViewCount(noticeId);
        return NoticeResponse.from(notice);
    }

    @Transactional
    public NoticeResponse createNotice(CreateNoticeRequest request) {
        Notice notice = noticeService.createNotice(request.title(), request.content(), request.pinned());
        return NoticeResponse.from(notice);
    }

    @Transactional
    public NoticeResponse updateNotice(Long noticeId, UpdateNoticeRequest request) {
        Notice notice = noticeService.updateNotice(noticeId, request.title(), request.content(), request.pinned());
        return NoticeResponse.from(notice);
    }

    @Transactional
    public void deleteNotice(Long noticeId) {
        noticeService.deleteNotice(noticeId);
    }
}
