package com.backend.devfordev.repository.ProjectRepository;

import com.backend.devfordev.domain.ProjectEntity.Project;
import com.backend.devfordev.domain.ProjectEntity.ProjectLink;
import com.backend.devfordev.dto.ProjectDto.ProjectRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectLinkRepository extends JpaRepository<ProjectLink, Long> {
    List<ProjectLink> findByProject(Project project);
}
