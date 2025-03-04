package com.backend.devfordev.controller;

import com.backend.devfordev.apiPayload.ApiResponse;
import com.backend.devfordev.dto.LikeDto.LikeRequest;
import com.backend.devfordev.dto.LikeDto.LikeResponse;
import com.backend.devfordev.service.LikeService.LikeService;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "좋아요 API")
@RequiredArgsConstructor
@RestController
@RequestMapping
@Slf4j
public class LikeController {
        private final LikeService likeService;

        // 좋아요 추가
        @Operation(summary = "좋아요", description = "좋아요를 추가하고 삭제할 수 있는 api입니다. 팀, 프로젝트, 서비스, 포트폴리오에 모두 사용할 수 있습니다.")
        @PostMapping(value = "/v1/likes")
        public ResponseEntity<ApiResponse<LikeResponse>> addLike(@Valid @RequestBody LikeRequest request, @AuthenticationPrincipal User user) {

            // 좋아요 생성
            LikeResponse likeResponse = likeService.createLike(request, Long.parseLong(user.getUsername()));  // LikeResponse로 수정

            ApiResponse<LikeResponse> apiResponse = ApiResponse.onSuccess(likeResponse);

            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        }

}
