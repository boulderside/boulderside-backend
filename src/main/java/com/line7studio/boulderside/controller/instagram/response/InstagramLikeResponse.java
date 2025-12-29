package com.line7studio.boulderside.controller.instagram.response;

public record InstagramLikeResponse(
    Long instagramId,
    boolean liked,
    long likeCount
) {
    public static InstagramLikeResponse of(Long instagramId, boolean liked, long likeCount) {
        return new InstagramLikeResponse(instagramId, liked, likeCount);
    }
}