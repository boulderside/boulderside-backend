package com.line7studio.boulderside.usecase.user;

import com.line7studio.boulderside.controller.user.response.ProfileImageResponse;
import com.line7studio.boulderside.domain.user.service.UserService;
import com.line7studio.boulderside.infrastructure.aws.s3.S3FolderType;
import com.line7studio.boulderside.infrastructure.aws.s3.S3ObjectInfo;
import com.line7studio.boulderside.infrastructure.aws.s3.S3Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserProfileUseCase {

    private final UserService userService;
    private final S3Provider s3Provider;

    @Transactional
    public ProfileImageResponse updateProfileImage(Long userId, MultipartFile profileImage) {
        S3ObjectInfo uploadedImage = s3Provider.imageUpload(profileImage, S3FolderType.PROFILE, userId);
        String uploadedImageUrl = uploadedImage.url();

        userService.updateUserProfileImage(userId, uploadedImageUrl);

        return ProfileImageResponse.of(uploadedImageUrl);
    }
}
