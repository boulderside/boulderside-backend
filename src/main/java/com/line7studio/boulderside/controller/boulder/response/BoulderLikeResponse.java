package com.line7studio.boulderside.controller.boulder.response;

public record BoulderLikeResponse(
    Long boulderId,
    boolean liked,
    long likeCount
) {
    public static BoulderLikeResponse of(Long boulderId, boolean liked, long likeCount) {
        return new BoulderLikeResponse(boulderId, liked, likeCount);
    }
}