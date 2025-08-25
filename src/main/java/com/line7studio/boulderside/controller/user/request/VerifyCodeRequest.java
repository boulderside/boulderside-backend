package com.line7studio.boulderside.controller.user.request;

public record VerifyCodeRequest(String phoneNumber, String code) {
}
