package com.line7studio.boulderside.controller.user.response;

public record NicknameAvailabilityResponse(String nickname, boolean available) {
	public static NicknameAvailabilityResponse of(String nickname, boolean available) {
		return new NicknameAvailabilityResponse(nickname, available);
	}
}
