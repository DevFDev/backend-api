package com.backend.devfordev.controller;


import com.backend.devfordev.apiPayload.ApiResponse;
import com.backend.devfordev.apiPayload.code.status.SuccessStatus;
import com.backend.devfordev.dto.CustomPageResponse;
import com.backend.devfordev.dto.PortfolioDto.PortfolioCommentRequest;
import com.backend.devfordev.dto.PortfolioDto.PortfolioCommentResponse;
import com.backend.devfordev.dto.PortfolioDto.PortfolioRequest;
import com.backend.devfordev.dto.PortfolioDto.PortfolioResponse;
import com.backend.devfordev.dto.ProjectDto.ProjectResponse;
import com.backend.devfordev.service.PortfolioService.PortfolioCommentService;
import com.backend.devfordev.service.PortfolioService.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Tag(name = "포트폴리오 API")
@RequiredArgsConstructor
@RestController
@RequestMapping
@Slf4j
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final PortfolioCommentService portfolioCommentService;
    @Operation(summary = "포트폴리오 글 등록", description = "포트폴리오를 등록하는 api입니다.")
    @PostMapping(value = "/v1/portfolio",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<PortfolioResponse.PortCreateResponse>> createPortfolio(@Valid @RequestPart("request") PortfolioRequest.PortfolioCreateRequest request, @AuthenticationPrincipal User user,
                                                                                             @RequestPart(value = "portImage", required = false) MultipartFile portImage) {

        PortfolioResponse.PortCreateResponse portCreateResponse = portfolioService.createPortfolio(request, Long.parseLong(user.getUsername()), portImage);
        ApiResponse<PortfolioResponse.PortCreateResponse> apiResponse = ApiResponse.onSuccess(portCreateResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }


    @Operation(summary = "포트폴리오 전체 리스트 조회", description = "포트폴리오 전체 글 조회 API입니다. 검색, 필터링, 정렬, 페이징 적용.")
    @GetMapping(value = "/v1/portfolio")
    public ResponseEntity<ApiResponse<CustomPageResponse<PortfolioResponse.PortfolioListResponse>>> getPortfolioList(
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false, defaultValue = "recent") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);  // 정렬은 서비스에서 처리
        CustomPageResponse<PortfolioResponse.PortfolioListResponse> portfolioList = portfolioService.getPortList(
                position,
                Optional.ofNullable(searchTerm),
                sortBy,
                pageable
        );

        return ResponseEntity.ok(ApiResponse.onSuccess(portfolioList));
    }

    @Operation(summary = "포트폴리오 삭제", description = "포트폴리오를 삭제하는 api입니다. 작성자만 해당 기능을 사용할 수 있습니다.")
    @DeleteMapping(value = "/v1/portfolio/{portId}")
    public ResponseEntity<ApiResponse> deletePortfolio(@PathVariable Long portId, @AuthenticationPrincipal User user) {
        portfolioService.deletePortfolio(portId, Long.parseLong(user.getUsername()));
        ApiResponse apiResponse = ApiResponse.builder()
                .isSuccess(SuccessStatus._OK.getReason().getIsSuccess())
                .code(SuccessStatus._OK.getCode())
                .message("포트폴리오가 성공적으로 삭제되었습니다.")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @Operation(summary = "포트폴리오 상세 조회", description = "포트폴리오 ID를 입력받아 상세 정보를 조회합니다.")
    @GetMapping("/v1/portfolio/{portId}")
    public ResponseEntity<ApiResponse<PortfolioResponse.PortDetailResponse>> getPortfolioDetail(
            @PathVariable Long portId) {

        PortfolioResponse.PortDetailResponse portfolioDetail = portfolioService.getPortfolioDetail(portId);
        ApiResponse<PortfolioResponse.PortDetailResponse> apiResponse = ApiResponse.onSuccess(portfolioDetail);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "포트폴리오 수정", description = "포트폴리오를 수정하는 API입니다. (이미지 포함 가능)")
    @PatchMapping(value = "v1/portfolio/{portfolioId}", consumes = "multipart/form-data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<PortfolioResponse.PortCreateResponse>> updatePortfolio(
            @PathVariable Long portfolioId,
            @Valid @RequestPart("request") PortfolioRequest.PortfolioCreateRequest request,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
            @RequestPart(value = "portImage", required = false) MultipartFile portImage
    ) {

        PortfolioResponse.PortCreateResponse updatedPortfolio = portfolioService.updatePortfolio(
                portfolioId,
                request,
                Long.parseLong(user.getUsername()), // 현재 로그인한 사용자 ID
                portImage
        );


        ApiResponse<PortfolioResponse.PortCreateResponse> apiResponse = ApiResponse.onSuccess(updatedPortfolio);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }




    @Operation(summary = "포트폴리오 댓글 등록",
            description = "포트폴리오에 댓글 작성하는 API입니다. 작성자만 해당 기능을 사용할 수 있습니다. " +
                    "최상위 댓글의 경우 parentId를 null로 보내주세요!")
    @PostMapping("v1/portfolio/{portfolioId}/comments")
    public ResponseEntity<ApiResponse<PortfolioCommentResponse>> createComment(
            @PathVariable Long portfolioId,
            @RequestBody @Valid PortfolioCommentRequest request,
            @AuthenticationPrincipal User user) {

        PortfolioCommentResponse response = portfolioCommentService.addComment(portfolioId, Long.parseLong(user.getUsername()), request);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }


    @Operation(summary = "포트폴리오 댓글 조회",
            description = "특정 포트폴리오의 댓글을 조회합니다. 댓글은 계층 구조로 반환됩니다.")
    @GetMapping("v1/portfolio/{portfolioId}/comments")
    public ResponseEntity<ApiResponse<List<PortfolioCommentResponse>>> getComments(
            @PathVariable Long portfolioId) {

        List<PortfolioCommentResponse> responses = portfolioCommentService.getCommentsByPortfolioId(portfolioId);
        return ResponseEntity.ok(ApiResponse.onSuccess(responses));
    }
}
