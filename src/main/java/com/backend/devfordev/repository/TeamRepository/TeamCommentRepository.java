package com.backend.devfordev.repository.TeamRepository;

import com.backend.devfordev.domain.ProjectEntity.ProjectComment;
import com.backend.devfordev.domain.TeamEntity.TeamComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TeamCommentRepository extends JpaRepository<TeamComment, Long> {
    List<TeamComment> findByTeamIdAndParentIsNull(Long proId);
}
