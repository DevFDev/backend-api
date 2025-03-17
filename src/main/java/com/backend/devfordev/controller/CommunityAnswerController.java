package com.backend.devfordev.controller;

import com.backend.devfordev.apiPayload.ApiResponse;
import com.backend.devfordev.dto.CommunityDto.CommunityAnswerRequest;
import com.backend.devfordev.dto.CommunityDto.CommunityAnswerResponse;
import com.backend.devfordev.dto.CommunityDto.CommunityResponse;
import com.backend.devfordev.service.CommunityService.CommunityAnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "커뮤니티 답변 API")
@RestController
@Slf4j
@RequestMapping("/v1/community")
@RequiredArgsConstructor
public class CommunityAnswerController {
    private final CommunityAnswerService communityAnswerService;


    @Operation(summary = "커뮤니티 게시글 답변 추가", description = "특정 커뮤니티 게시글에 답변을 추가하는 API입니다.")
    @PostMapping("/{communityId}/answers")
    public ResponseEntity<ApiResponse<CommunityAnswerResponse>> createAnswer(
            @PathVariable Long communityId,
            @RequestBody @Valid CommunityAnswerRequest request,
            @AuthenticationPrincipal User user) {

        CommunityAnswerResponse response = communityAnswerService.addAnswer(communityId, Long.parseLong(user.getUsername()), request);
        ApiResponse<CommunityAnswerResponse> apiResponse = ApiResponse.onSuccess(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    /**
     * ✅ 커뮤니티 게시글의 답변 조회 API
     */
    @Operation(summary = "커뮤니티 게시글 답변 조회", description = "특정 커뮤니티 게시글의 답변을 조회하는 API입니다.")
    @GetMapping("/{communityId}/answers")
    public ResponseEntity<ApiResponse<List<CommunityAnswerResponse>>> getAnswers(
            @PathVariable Long communityId) {

        List<CommunityAnswerResponse> responses = communityAnswerService.getAnswersByCommunityId(communityId);
        return ResponseEntity.ok(ApiResponse.onSuccess(responses));
    }

}
