package com.backend.devfordev.controller;

import com.backend.devfordev.apiPayload.ApiResponse;
import com.backend.devfordev.apiPayload.code.status.SuccessStatus;
import com.backend.devfordev.domain.ProjectEntity.Project;
import com.backend.devfordev.domain.enums.ProjectCategory;
import com.backend.devfordev.domain.enums.TeamType;
import com.backend.devfordev.dto.ProjectDto.ProjectRequest;
import com.backend.devfordev.dto.ProjectDto.ProjectResponse;
import com.backend.devfordev.dto.TeamDto.TeamResponse;
import com.backend.devfordev.service.ProjectService.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Tag(name = "프로젝트 API")
@RequiredArgsConstructor
@RestController
@RequestMapping
@Slf4j
public class ProjectController {
    private final ProjectService projectService;
    @Operation(summary = "프로젝트 글 등록", description = "프로젝트 글을 등록하는 api입니다.")
    @PostMapping(value = "/v1/project",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<ProjectResponse.ProjectCreateResponse>> createProject(@Valid @RequestPart("request") ProjectRequest.ProjectCreateRequest request, @AuthenticationPrincipal User user,
                                                                                            @RequestPart(value = "proImage", required = false) MultipartFile proImage) {

        ProjectResponse.ProjectCreateResponse proCreateResponse = projectService.createProject(request, Long.parseLong(user.getUsername()), proImage);
        ApiResponse<ProjectResponse.ProjectCreateResponse> apiResponse = ApiResponse.onSuccess(proCreateResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @Operation(summary = "프로젝트 상세 조회", description = "각 프로젝트 게시글을 상세 조회할 수 있는 api입니다.")
    @GetMapping(value = "/v1/project/{projectId}")
    public ResponseEntity<ApiResponse<ProjectResponse.ProjectDetailResponse>> getProjectDetail(@PathVariable Long projectId) {
        ProjectResponse.ProjectDetailResponse projectDetailResponse = projectService.getProjectDetail(projectId);

        ApiResponse<ProjectResponse.ProjectDetailResponse> apiResponse = ApiResponse.onSuccess(projectDetailResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "프로젝트 수정", description = "기존 프로젝트의 정보를 업데이트합니다. 작성자만 해당 기능을 사용할 수 있습니다.")
    @PatchMapping(value = "/v1/projects/{projectId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<ProjectResponse.ProjectUpdateResponse>> updateProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal User user,
            @Valid @RequestPart("request") ProjectRequest.ProjectUpdateRequest request,
            @RequestPart(value = "projectImage", required = false) MultipartFile projectImage) {
        ProjectResponse.ProjectUpdateResponse projectUpdateResponse = projectService.updateProject(projectId, Long.parseLong(user.getUsername()), request, projectImage);
        ApiResponse<ProjectResponse.ProjectUpdateResponse> apiResponse = ApiResponse.onSuccess(projectUpdateResponse);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "프로젝트 게시글 삭제", description = "프로젝트 모집글을 삭제하는 api입니다. 작성자만 해당 기능을 사용할 수 있습니다.")
    @DeleteMapping(value = "/v1/project/{projectId}")
    public ResponseEntity<ApiResponse> deleteProject(@PathVariable Long projectId, @AuthenticationPrincipal User user) {
        projectService.deleteProject(projectId, Long.parseLong(user.getUsername()));
        ApiResponse apiResponse = ApiResponse.builder()
                .isSuccess(SuccessStatus._OK.getReason().getIsSuccess())
                .code(SuccessStatus._OK.getCode())
                .message("프로젝트 게시글이 성공적으로 삭제되었습니다.")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @Operation(summary = "프로젝트 게시글 전체 조회", description = "프로젝트 게시글 조회 api입니다. 검색, 필터링, 정렬 적용.")
    @GetMapping(value = "/v1/project")
    public ResponseEntity<ApiResponse<List<ProjectResponse.ProjectListResponse>>> getProjectList(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) ProjectCategory projectCategory,
            @RequestParam(defaultValue = "recent") String sortBy, // "sortBy"만 사용
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) { // "sort"는 제거됨

        Pageable sortedPageable = PageRequest.of(
                page - 1, // Page는 0부터 시작하므로 -1 해줌
                size
        );

        List<ProjectResponse.ProjectListResponse> projectList = projectService.getProjectList(
                Optional.ofNullable(projectCategory),
                Optional.ofNullable(searchTerm),
                sortBy
        );

        ApiResponse<List<ProjectResponse.ProjectListResponse>> apiResponse = ApiResponse.onSuccess(projectList);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }



}
