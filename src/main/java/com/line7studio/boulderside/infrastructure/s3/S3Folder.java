package com.line7studio.boulderside.infrastructure.s3;

public enum S3Folder {
	PROFILE("profile");
	
	private final String path;

	S3Folder(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}