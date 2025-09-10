package com.line7studio.boulderside.controller.user.response;

import com.line7studio.boulderside.domain.aggregate.user.entity.User;

import lombok.Builder;

@Builder
public record FindIdByPhoneResponse(String email) {

	public static FindIdByPhoneResponse from(User user) {
		return FindIdByPhoneResponse.builder()
			.email(user.getEmail())
			.build();
	}
}
