package com.line7studio.boulderside.controller.post;


import com.line7studio.boulderside.application.post.PostUseCase;
import com.line7studio.boulderside.common.response.ApiResponse;
import com.line7studio.boulderside.common.security.details.CustomUserDetails;
import com.line7studio.boulderside.controller.post.request.CreatePostRequest;
import com.line7studio.boulderside.controller.post.request.UpdatePostRequest;
import com.line7studio.boulderside.controller.post.response.PostPageResponse;
import com.line7studio.boulderside.controller.post.response.PostResponse;
import com.line7studio.boulderside.domain.feature.post.enums.PostSortType;
import com.line7studio.boulderside.domain.feature.post.enums.PostType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {
	private final PostUseCase postUseCase;

	@GetMapping
	public ResponseEntity<ApiResponse<PostPageResponse>> getPostPage(
		@RequestParam(required = false) Long cursor,
        @RequestParam(required = false) String subCursor,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam PostType postType,
		@RequestParam(defaultValue = "LATEST_CREATED") PostSortType postSortType,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
		PostPageResponse postPageResponse = postUseCase.getPostPage(cursor, subCursor, size, postType, postSortType, userDetails.getUserId());
		return ResponseEntity.ok(ApiResponse.of(postPageResponse));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<PostResponse>> getPost(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        PostResponse post = postUseCase.getPostById(id, userDetails.getUserId());
		return ResponseEntity.ok(ApiResponse.of(post));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<PostResponse>> createPost(
		@Valid @RequestBody CreatePostRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
		PostResponse post = postUseCase.createPost(request, userDetails.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(post));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<PostResponse>> updatePost(@PathVariable Long id,
		@Valid @RequestBody UpdatePostRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
		PostResponse post = postUseCase.updatePost(id, request, userDetails.getUserId());
		return ResponseEntity.ok(ApiResponse.of(post));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deletePost(
        @PathVariable Long id,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
		postUseCase.deletePost(id, userDetails.getUserId());
		return ResponseEntity.ok(ApiResponse.success());
	}
}
