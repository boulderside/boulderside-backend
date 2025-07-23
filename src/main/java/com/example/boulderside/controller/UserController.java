package com.example.boulderside.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.boulderside.controller.dto.response.ApiResponse;

@RestController
public class UserController {

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> getUser(@PathVariable Long id) {
		return ResponseEntity.ok(ApiResponse.of("hi"));
	}

}
