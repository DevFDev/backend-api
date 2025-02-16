package com.backend.devfordev.service.ProjectService;

import com.backend.devfordev.dto.ProjectDto.ProjectRequest;
import com.backend.devfordev.dto.ProjectDto.ProjectResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProjectService {
    public ProjectResponse.ProjectCreateResponse createProject(ProjectRequest.ProjectCreateRequest request, Long userId, MultipartFile portImage);
    public ProjectResponse.ProjectDetailResponse getProjectDetail(Long id);
    public ProjectResponse.ProjectUpdateResponse updateProject(Long projectId, Long userId, ProjectRequest.ProjectUpdateRequest request, MultipartFile projectImage);
}
