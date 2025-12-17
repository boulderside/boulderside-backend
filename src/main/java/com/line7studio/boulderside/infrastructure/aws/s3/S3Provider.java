package com.line7studio.boulderside.infrastructure.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.line7studio.boulderside.common.exception.BusinessException;
import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.exception.ExternalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static com.line7studio.boulderside.infrastructure.aws.s3.S3FolderType.PROFILE;

@RequiredArgsConstructor
@Component
@Slf4j
public class S3Provider {
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final AmazonS3 amazonS3;

	// S3 이미지 업로드
	public S3ObjectInfo imageUpload(MultipartFile file, S3FolderType folder, Long userId) {
		if (file == null || file.isEmpty()) {
			// 기본 이미지 제공
			if (folder == PROFILE) {
				return S3ObjectInfo.of(
					"https://lhj-s3-1.s3.ap-northeast-2.amazonaws.com/profile/53ca0dcc-95db-4460-afcf-c352af4f89e7_logo.png",
					null);
			}

			return null;
		}

		String fileName = file.getOriginalFilename();

		// 확장자 검증(스푸핑 가능)
		String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		if (!List.of("png", "jpg", "jpeg").contains(ext)) {
			throw new ExternalException(ErrorCode.S3_INVALID_FILE_TYPE);
		}

		// MIME 타입 검증
		String contentType = file.getContentType();
		List<String> allowedMimeTypes = List.of("image/png", "image/jpg", "image/jpeg");

		if (contentType == null || !allowedMimeTypes.contains(contentType.toLowerCase())) {
			throw new ExternalException(ErrorCode.S3_INVALID_FILE_TYPE);
		}

		String uuidFileName = UUID.randomUUID() + "_" + fileName;
		String s3FilePathName = resolveFolderPath(folder, userId) + "/" + uuidFileName;

		try (InputStream inputStream = file.getInputStream()) {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(file.getSize());
			metadata.setContentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream");

			amazonS3.putObject(
				new PutObjectRequest(bucket, s3FilePathName, inputStream, metadata)
			);
		} catch (Exception e) {
			log.error("[S3 UPLOAD FAILED] bucket={}, key={}, file={}, message={}", bucket, s3FilePathName, fileName,
				e.getMessage(), e);
			throw new BusinessException(ErrorCode.S3_UPLOAD_FAILED);
		}

		String s3Url = amazonS3.getUrl(bucket, s3FilePathName).toString();
		return S3ObjectInfo.of(s3Url, fileName);
	}

	// S3 이미지 삭제
	public void deleteImageByUrl(String s3Url) {
		String s3Key = extractKeyFromUrl(s3Url);
		try {
			log.info("[S3 DELETE] bucket={}, key={}, url={}", bucket, s3Key, s3Url);
			amazonS3.deleteObject(bucket, s3Key);
		} catch (Exception e) {
			throw new ExternalException(ErrorCode.S3_DELETE_FAILED);
		}
	}

	private String extractKeyFromUrl(String url) {
		try {
			if (url.contains("amazonaws.com")) {
				AmazonS3URI s3URI = new AmazonS3URI(url);
				return s3URI.getKey();
			} else {
				// CloudFront 등 커스텀 도메인
				URI uri = new URI(url);
				String path = uri.getPath();
				return path.startsWith("/") ? path.substring(1) : path;
			}
		} catch (Exception e) {
			throw new ExternalException(ErrorCode.S3_DELETE_FAILED);
		}
	}

	private String resolveFolderPath(S3FolderType folder, Long userId) {
		if (folder == PROFILE && userId != null) {
			return "users/" + userId + "/profile";
		}
		return folder.getPath();
	}
}
