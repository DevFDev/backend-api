package com.backend.devfordev.service.ProjectService;

import com.backend.devfordev.domain.enums.ProjectCategory;
import com.backend.devfordev.dto.CustomPageResponse;
import com.backend.devfordev.dto.ProjectDto.ProjectRequest;
import com.backend.devfordev.dto.ProjectDto.ProjectResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    public ProjectResponse.ProjectCreateResponse createProject(ProjectRequest.ProjectCreateRequest request, Long userId, MultipartFile portImage);
    public ProjectResponse.ProjectDetailResponse getProjectDetail(Long 글id);
    public ProjectResponse.ProjectUpdateResponse updateProject(Long projectId, Long userId, ProjectRequest.ProjectUpdateRequest request, MultipartFile projectImage);
    public void deleteProject(Long projectId, Long userId);


    public CustomPageResponse<ProjectResponse.ProjectListResponse> getProjectList(
            Optional<ProjectCategory> categoryOpt,
            Optional<String> searchTermOpt,
            String sortBy,
            Pageable pageable
    );
}
