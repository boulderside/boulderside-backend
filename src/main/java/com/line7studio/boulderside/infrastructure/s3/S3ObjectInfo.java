package com.line7studio.boulderside.infrastructure.s3;

import lombok.Builder;

@Builder
public record S3ObjectInfo(
	String url, String originalFileName
) {
	public static S3ObjectInfo of(String url, String originalFileName) {
		return S3ObjectInfo.builder()
			.url(url)
			.originalFileName(originalFileName)
			.build();
	}
}
