package com.line7studio.boulderside.usecase.user;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.controller.user.request.BlockUserRequest;
import com.line7studio.boulderside.controller.user.response.BlockedUserResponse;
import com.line7studio.boulderside.domain.user.User;
import com.line7studio.boulderside.domain.user.service.UserService;
import com.line7studio.boulderside.domain.user.UserBlock;
import com.line7studio.boulderside.domain.user.service.UserBlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserBlockUseCase {

    private final UserBlockService userBlockService;
    private final UserService userService;

    @Transactional
    public void blockUser(Long blockerId, BlockUserRequest request) {
        Long blockedId = request.targetUserId();
        if (blockerId.equals(blockedId)) {
            throw new BusinessException(ErrorCode.CANNOT_BLOCK_SELF);
        }

        userService.getUserById(blockedId);
        userBlockService.blockUser(blockerId, blockedId);
    }

    @Transactional
    public void unblockUser(Long blockerId, Long blockedId) {
        userBlockService.unblockUser(blockerId, blockedId);
    }

    @Transactional(readOnly = true)
    public List<BlockedUserResponse> getBlockedUsers(Long blockerId) {
        List<UserBlock> blocks = userBlockService.getUserBlocks(blockerId);
        if (blocks.isEmpty()) {
            return List.of();
        }

        List<Long> blockedIds = blocks.stream()
            .map(UserBlock::getBlockedId)
            .toList();

        Map<Long, User> userMap = userService.findAllById(blockedIds).stream()
            .collect(Collectors.toMap(User::getId, u -> u));

        return blocks.stream()
            .map(block -> {
                User user = userMap.get(block.getBlockedId());
                return BlockedUserResponse.of(block, user);
            })
            .toList();
    }

    @Transactional(readOnly = true)
    public List<Long> getBlockedUserIds(Long blockerId) {
        return userBlockService.getBlockedUserIds(blockerId);
    }

    @Transactional(readOnly = true)
    public boolean isBlocked(Long blockerId, Long targetUserId) {
        return userBlockService.isBlocked(blockerId, targetUserId);
    }
}
