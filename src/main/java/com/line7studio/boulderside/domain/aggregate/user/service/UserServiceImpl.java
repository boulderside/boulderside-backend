package com.line7studio.boulderside.domain.aggregate.user.service;

import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.domain.aggregate.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.line7studio.boulderside.domain.aggregate.user.entity.User;
import com.line7studio.boulderside.domain.aggregate.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public User findByPhone(String phoneNumber) {
		return userRepository.findByPhone(phoneNumber).orElse(null);
	}

	@Override
	public boolean isUserIdDuplicate(String email) {
		return userRepository.existsByEmail(email);
	}
}


	@Override
	public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
	}

  @Override
  public List<User> findAllById(List<Long> userIdList) {
      return userRepository.findAllByIdIn(userIdList);
  }

