package com.backend.devfordev.controller;

import com.backend.devfordev.apiPayload.ApiResponse;
import com.backend.devfordev.apiPayload.code.status.SuccessStatus;
import com.backend.devfordev.domain.enums.CommunityCategory;
import com.backend.devfordev.dto.CommunityDto.CommunityCommentRequest;
import com.backend.devfordev.dto.CommunityDto.CommunityCommentResponse;
import com.backend.devfordev.dto.CommunityDto.CommunityRequest;
import com.backend.devfordev.dto.CommunityDto.CommunityResponse;
import com.backend.devfordev.service.CommunityService.CommunityCommentService;
import com.backend.devfordev.service.CommunityService.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;

@Tag(name = "커뮤니티 API")
@RequiredArgsConstructor
@RestController
@RequestMapping
@Slf4j
public class CommunityController {
    private final CommunityService communityService;
    private final CommunityCommentService communityCommentService;
    @Operation(summary = "커뮤니티 글 등록",  description = "커뮤니티 글 등록 api입니다.")
    @PostMapping(value = "/v1/community")
    public ResponseEntity<ApiResponse<CommunityResponse.CommunityCreateResponse>> createCommunity(@Valid @RequestBody CommunityRequest.CommunityCreateRequest request, @AuthenticationPrincipal User user){

        CommunityResponse.CommunityCreateResponse communityCreateResponse = communityService.createCommunity(request, Long.parseLong(user.getUsername()));
        ApiResponse<CommunityResponse.CommunityCreateResponse> apiResponse = ApiResponse.onSuccess(communityCreateResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }


    @Operation(summary = "커뮤니티 글 전체 조회", description = "커뮤니티 전체 글 조회 api입니다. 검색, 필터링, 정렬 적용.")
    @GetMapping(value = "/v1/community")
    public ResponseEntity<ApiResponse<List<CommunityResponse.CommunityListResponse>>> getCommunityList(
            @RequestParam(required = false) CommunityCategory category,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false, defaultValue = "recent") String sortBy
            ) {
        // 카테고리가 있을 경우 서비스에 Optional로 전달
        List<CommunityResponse.CommunityListResponse> communityList = communityService.getCommunityList(
                Optional.ofNullable(category),
                Optional.ofNullable(searchTerm),
                sortBy);

        ApiResponse<List<CommunityResponse.CommunityListResponse>> apiResponse = ApiResponse.onSuccess(communityList);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

    }


    @Operation(summary = "커뮤니티 글 상세 조회", description = "커뮤니티의 각 글을 상세 조회하는 api입니다.")
    @GetMapping(value = "/v1/community/{id}")
    public ResponseEntity<ApiResponse<CommunityResponse.CommunityDetailResponse>> getCommunityDetail(@PathVariable Long id) {
        CommunityResponse.CommunityDetailResponse communityDetail = communityService.getCommunityDetail(id);
        ApiResponse<CommunityResponse.CommunityDetailResponse> apiResponse = ApiResponse.onSuccess(communityDetail);


        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @Operation(summary = "인기 커뮤니티 Top 5 유저 조회 (좋아요 수 기준)", description = "커뮤니티 전체 글 중 누적 좋아요 수가 가장 많은 유저 5명에 대한 조회 api입니다.")
    @GetMapping(value = "/v1/community/top5")
    public ResponseEntity<ApiResponse<List<CommunityResponse.CommunityTop5Response>>> getTop5UsersByTotalLikes() {
        // 인기 Top 5 유저 조회
        List<CommunityResponse.CommunityTop5Response> top5Users = communityService.getTop5UsersByTotalLikes();


        ApiResponse<List<CommunityResponse.CommunityTop5Response>> apiResponse = ApiResponse.onSuccess(top5Users);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "커뮤니티 글 수정", description = "커뮤니티 글을 수정하는 api입니다. 작성자만 해당 기능을 사용할 수 있습니다.")
    @PatchMapping(value = "/v1/community/{id}")
    public ResponseEntity<ApiResponse<CommunityResponse.CommunityUpdateResponse>> updateCommunity(@Valid @RequestBody CommunityRequest.CommunityUpdateRequest request, @PathVariable Long id, @AuthenticationPrincipal User user) {

        CommunityResponse.CommunityUpdateResponse communityUpdateResponse = communityService.updateCommunity(id, request, Long.parseLong(user.getUsername()));

        ApiResponse<CommunityResponse.CommunityUpdateResponse> apiResponse = ApiResponse.onSuccess(communityUpdateResponse);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "커뮤니티 글 삭제", description = "커뮤니티 글을 삭제하는 api입니다. 작성자만 해당 기능을 사용할 수 있습니다.")
    @DeleteMapping(value = "/v1/community/{id}")
    public ResponseEntity<ApiResponse> deleteCommunity(@PathVariable Long id, @AuthenticationPrincipal User user) {
        communityService.deleteCommunity(id, Long.parseLong(user.getUsername()));
        ApiResponse apiResponse = ApiResponse.builder()
                .isSuccess(SuccessStatus._OK.getReason().getIsSuccess())
                .code(SuccessStatus._OK.getCode())
                .message("팀 모집글이 성공적으로 삭제되었습니다.")
                .build();
        // HTTP 상태 코드 204로 응답
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "커뮤니티 댓글 등록", description = "커뮤니티에 댓글 작성하는 api입니다. 작성자만 해당 기능을 사용할 수 있습니다. 최상위 댓글의 경우 parentId를 null로 보내주세요!")
    @PostMapping("/community/{communityId}/comments")
    public ResponseEntity<ApiResponse<CommunityCommentResponse>> createComment(
            @PathVariable Long communityId,
            @RequestBody @Valid CommunityCommentRequest request,
            @AuthenticationPrincipal User user) {
        CommunityCommentResponse response = communityCommentService.addComment(communityId, Long.parseLong(user.getUsername()), request);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "커뮤니티 댓글 조회", description = "특정 커뮤니티의 댓글을 조회합니다. 댓글은 계층 구조로 반환됩니다.")
    @GetMapping("/community/{communityId}/comments")
    public ResponseEntity<ApiResponse<List<CommunityCommentResponse>>> getComments(
            @PathVariable Long communityId) {
        List<CommunityCommentResponse> responses = communityCommentService.getCommentsByCommunityId(communityId);
        return ResponseEntity.ok(ApiResponse.onSuccess(responses));
    }
}
