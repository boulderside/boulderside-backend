package com.line7studio.boulderside.domain.feature.post.service;

import com.line7studio.boulderside.domain.feature.post.entity.MatePost;
import com.line7studio.boulderside.domain.feature.post.enums.MatePostSortType;

import java.time.LocalDate;
import java.util.List;

public interface MatePostService {
    MatePost getMatePostById(Long postId);

    List<MatePost> getMatePostsWithCursor(Long cursor, String subCursor, int size, MatePostSortType postSortType);

    List<MatePost> getMatePostsByUser(Long userId, Long cursor, int size);

    MatePost createMatePost(Long userId, String title, String content, LocalDate meetingDate);

    MatePost updateMatePost(Long postId, String title, String content, LocalDate meetingDate, Long userId);

    void deleteMatePost(Long postId, Long userId);
}
