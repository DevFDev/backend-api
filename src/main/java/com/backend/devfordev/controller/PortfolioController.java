package com.backend.devfordev.controller;


import com.backend.devfordev.apiPayload.ApiResponse;
import com.backend.devfordev.apiPayload.code.status.SuccessStatus;
import com.backend.devfordev.dto.PortfolioDto.PortfolioRequest;
import com.backend.devfordev.dto.PortfolioDto.PortfolioResponse;
import com.backend.devfordev.service.PortfolioService.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Operation(summary = "포트폴리오 글 등록", description = "포트폴리오를 등록하는 api입니다.")
    @PostMapping(value = "/v1/portfolio",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<PortfolioResponse.PortCreateResponse>> createPortfolio(@Valid @RequestPart("request") PortfolioRequest.PortfolioCreateRequest request, @AuthenticationPrincipal User user,
                                                                                             @RequestPart(value = "portImage", required = false) MultipartFile portImage) {

        PortfolioResponse.PortCreateResponse portCreateResponse = portfolioService.createPortfolio(request, Long.parseLong(user.getUsername()), portImage);
        ApiResponse<PortfolioResponse.PortCreateResponse> apiResponse = ApiResponse.onSuccess(portCreateResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }


    @Operation(summary = "포트폴리오 전체 리스트 조회", description = "포트폴리오 전체 글 조회 api입니다. 검색, 필터링, 정렬 적용.")
    @GetMapping(value="/v1/portfolio")
    public ResponseEntity<ApiResponse<List<PortfolioResponse.PortfolioListResponse>>> getPortfolioList(
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false, defaultValue = "recent") String sortBy
    ) {
        // 서비스 호출
        List<PortfolioResponse.PortfolioListResponse> portfolioList = portfolioService.getPortList(
                position,
                Optional.ofNullable(searchTerm),
                sortBy
        );

        // 응답 생성
        ApiResponse<List<PortfolioResponse.PortfolioListResponse>> apiResponse = ApiResponse.onSuccess(portfolioList);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
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

}
