package com.line7studio.boulderside.domain.user.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.user.UserBlock;
import com.line7studio.boulderside.domain.user.repository.UserBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBlockService {

    private final UserBlockRepository userBlockRepository;

    @Transactional
    public UserBlock blockUser(Long blockerId, Long blockedId) {
        if (userBlockRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId)) {
            throw new BusinessException(ErrorCode.USER_BLOCK_ALREADY_EXISTS);
        }

        UserBlock userBlock = UserBlock.create(blockerId, blockedId);
        return userBlockRepository.save(userBlock);
    }

    @Transactional
    public void unblockUser(Long blockerId, Long blockedId) {
        UserBlock userBlock = userBlockRepository.findByBlockerIdAndBlockedId(blockerId, blockedId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_BLOCK_NOT_FOUND));

        userBlockRepository.delete(userBlock);
    }

    @Transactional(readOnly = true)
    public List<UserBlock> getUserBlocks(Long blockerId) {
        return userBlockRepository.findAllByBlockerId(blockerId);
    }

    @Transactional(readOnly = true)
    public List<Long> getBlockedUserIds(Long blockerId) {
        return userBlockRepository.findBlockedIdsByBlockerId(blockerId);
    }

    @Transactional(readOnly = true)
    public List<Long> getBlockedOrBlockingUserIds(Long userId) {
        List<Long> result = new ArrayList<>();
        result.addAll(userBlockRepository.findBlockedIdsByBlockerId(userId));
        result.addAll(userBlockRepository.findBlockerIdsByBlockedId(userId));
        return result.stream().distinct().toList();
    }

    @Transactional(readOnly = true)
    public boolean isBlocked(Long blockerId, Long blockedId) {
        return userBlockRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId);
    }

    @Transactional(readOnly = true)
    public boolean isBlockedBetween(Long userId, Long otherUserId) {
        return userBlockRepository.existsByBlockerIdAndBlockedId(userId, otherUserId)
            || userBlockRepository.existsByBlockerIdAndBlockedId(otherUserId, userId);
    }
}
