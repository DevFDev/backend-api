package com.backend.devfordev.repository.ProjectRepository;

import com.backend.devfordev.domain.ProjectEntity.Project;
import com.backend.devfordev.domain.ProjectEntity.ProjectLink;
import com.backend.devfordev.dto.ProjectDto.ProjectRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProjectLinkRepository extends JpaRepository<ProjectLink, Long> {
    List<ProjectLink> findByProject(Project project);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProjectLink pl WHERE pl.project = :project")
    void deleteByProject(@Param("project") Project project);
}
