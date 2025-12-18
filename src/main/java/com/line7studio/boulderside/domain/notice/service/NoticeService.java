package com.line7studio.boulderside.domain.notice.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.notice.Notice;
import com.line7studio.boulderside.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Notice getById(Long id) {
        return noticeRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));
    }

    public Page<Notice> getNoticePage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
            Sort.by(Sort.Order.desc("pinned"), Sort.Order.desc("id")));
        return noticeRepository.findAll(pageable);
    }

    public Notice createNotice(String title, String content, boolean pinned) {
        Notice notice = Notice.create(title, content, pinned);
        return noticeRepository.save(notice);
    }

    public Notice updateNotice(Long id, String title, String content, boolean pinned) {
        Notice notice = getById(id);
        notice.update(title, content, pinned);
        return noticeRepository.save(notice);
    }

    public Notice increaseViewCount(Long id) {
        Notice notice = getById(id);
        notice.increaseViewCount();
        return noticeRepository.save(notice);
    }

    public void deleteNotice(Long id) {
        Notice notice = getById(id);
        noticeRepository.delete(notice);
    }
}
