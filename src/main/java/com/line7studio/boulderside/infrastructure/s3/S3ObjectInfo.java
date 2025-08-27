package com.line7studio.boulderside.infrastructure.s3;

import lombok.Builder;

@Builder
public record S3ObjectInfo(
	String url
) {
	public static S3ObjectInfo of(String url) {
		return S3ObjectInfo.builder()
			.url(url)
			.build();
	}
}
