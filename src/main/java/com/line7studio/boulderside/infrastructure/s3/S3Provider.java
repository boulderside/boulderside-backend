package com.line7studio.boulderside.infrastructure.s3;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.exception.ExternalApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class S3Provider {
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final AmazonS3 amazonS3;

	// S3 이미지 업로드
	public S3ObjectInfo imageUpload(MultipartFile file, S3Folder folder) throws IOException {
		String fileName = file.getOriginalFilename();

		// 확장자 검증
		String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		if (!List.of("png", "jpg", "jpeg").contains(ext)) {
			throw new ExternalApiException(ErrorCode.S3_INVALID_FILE_TYPE);
		}

		String uuidFileName = UUID.randomUUID() + "_" + fileName;
		String s3FilePathName = folder.getPath() + "/" + uuidFileName;

		try (InputStream inputStream = file.getInputStream()) {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(file.getSize());
			metadata.setContentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream");

			amazonS3.putObject(
				new PutObjectRequest(bucket, s3FilePathName, inputStream, metadata)
					.withCannedAcl(CannedAccessControlList.PublicRead)
			);
		}

		String s3Url = amazonS3.getUrl(bucket, s3FilePathName).toString();
		return S3ObjectInfo.of(s3Url, s3FilePathName);
	}

	// S3 이미지 삭제
	public void deleteImageByUrl(String s3Url) {
		try {
			String s3Key = extractKeyFromUrl(s3Url);
			if (!amazonS3.doesObjectExist(bucket, s3Key)) {
				throw new ExternalApiException(ErrorCode.S3_DELETE_FAILED);
			}
			amazonS3.deleteObject(bucket, s3Key);
		} catch (Exception e) {
			throw new ExternalApiException(ErrorCode.S3_DELETE_FAILED);
		}
	}

	private String extractKeyFromUrl(String s3Url) {
		try {
			URI uri = new URI(s3Url);
			String path = uri.getPath();

			if (path.startsWith("/" + bucket + "/")) {
				return path.substring(bucket.length() + 2);
			}

			if (path.startsWith("/")) {
				return path.substring(1);
			}

			return path;
		} catch (Exception e) {
			throw new ExternalApiException(ErrorCode.S3_DELETE_FAILED);
		}
	}
}

