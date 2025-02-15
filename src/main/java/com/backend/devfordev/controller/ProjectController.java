package com.backend.devfordev.controller;

import com.backend.devfordev.apiPayload.ApiResponse;
import com.backend.devfordev.dto.ProjectDto.ProjectRequest;
import com.backend.devfordev.dto.ProjectDto.ProjectResponse;
import com.backend.devfordev.dto.TeamDto.TeamResponse;
import com.backend.devfordev.service.ProjectService.ProjectService;
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
}
