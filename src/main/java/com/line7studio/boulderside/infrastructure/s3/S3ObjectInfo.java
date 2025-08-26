package com.line7studio.boulderside.infrastructure.s3;

public record S3ObjectInfo(
	String url,
	String key
) {
	public static S3ObjectInfo of(String url, String key) {
		return new S3ObjectInfo(url, key);
	}
}
