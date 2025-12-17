package com.line7studio.boulderside.common.util;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * 커서 기반 페이징 결과를 담는 유틸리티 클래스.
 *
 * @param <T> 페이징 대상 엔티티 타입
 */
public record CursorPageUtil<T>(
    List<T> content,
    Long nextCursor,
    boolean hasNext,
    int size
) {
    /**
     * 리스트와 요청 사이즈로 CursorPage를 생성합니다.
     * 리스트는 size + 1 개를 조회한 결과여야 합니다.
     *
     * @param list 조회된 리스트 (size + 1 개)
     * @param requestedSize 요청된 페이지 사이즈
     * @param idExtractor ID 추출 함수 (nextCursor 계산용)
     */
    public static <T> CursorPageUtil<T> of(List<T> list, int requestedSize, Function<T, Long> idExtractor) {
        if (list == null || list.isEmpty()) {
            return new CursorPageUtil<>(Collections.emptyList(), null, false, 0);
        }

        boolean hasNext = list.size() > requestedSize;
        List<T> content = hasNext ? list.subList(0, requestedSize) : list;
        Long nextCursor = hasNext && !content.isEmpty()
            ? idExtractor.apply(content.getLast())
            : null;

        return new CursorPageUtil<>(content, nextCursor, hasNext, content.size());
    }

    /**
     * 리스트와 요청 사이즈로 CursorPage를 생성합니다. (subCursor 포함)
     *
     * @param list 조회된 리스트 (size + 1 개)
     * @param requestedSize 요청된 페이지 사이즈
     * @param idExtractor ID 추출 함수
     * @param subCursorExtractor subCursor 추출 함수
     */
    public static <T> CursorPageWithSubCursor<T> ofWithSubCursor(
            List<T> list,
            int requestedSize,
            Function<T, Long> idExtractor,
            Function<T, String> subCursorExtractor) {

        if (list == null || list.isEmpty()) {
            return new CursorPageWithSubCursor<>(Collections.emptyList(), null, null, false, 0);
        }

        boolean hasNext = list.size() > requestedSize;
        List<T> content = hasNext ? list.subList(0, requestedSize) : list;

        Long nextCursor = null;
        String nextSubCursor = null;
        if (hasNext && !content.isEmpty()) {
            T last = content.getLast();
            nextCursor = idExtractor.apply(last);
            nextSubCursor = subCursorExtractor.apply(last);
        }

        return new CursorPageWithSubCursor<>(content, nextCursor, nextSubCursor, hasNext, content.size());
    }

    /**
     * subCursor를 포함하는 커서 페이지 결과.
     */
    public record CursorPageWithSubCursor<T>(
        List<T> content,
        Long nextCursor,
        String nextSubCursor,
        boolean hasNext,
        int size
    ) {}
}