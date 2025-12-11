package com.line7studio.boulderside.controller.user.response;

public record ProfileImageResponse(String profileImageUrl) {

    public static ProfileImageResponse of(String profileImageUrl) {
        return new ProfileImageResponse(profileImageUrl);
    }
}
