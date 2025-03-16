package com.backend.devfordev.repository.ProjectRepository;

import com.backend.devfordev.domain.PortfolioEntity.PortfolioComment;
import com.backend.devfordev.domain.ProjectEntity.ProjectComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProjectCommentRepository extends JpaRepository<ProjectComment, Long> {
    List<ProjectComment> findByProjectIdAndParentIsNull(Long proId);
}
