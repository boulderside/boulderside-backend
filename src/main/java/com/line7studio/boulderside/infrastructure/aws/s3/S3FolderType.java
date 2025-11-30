package com.line7studio.boulderside.infrastructure.aws.s3;

public enum S3FolderType {
	PROFILE("profile");

	private final String path;

	S3FolderType(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}