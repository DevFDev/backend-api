package com.backend.devfordev.repository.ProjectRepository;


import com.backend.devfordev.domain.ProjectEntity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    // 특정 작성자의 다른 프로젝트 조회 (현재 프로젝트 제외)
    @Query("SELECT p FROM Project p WHERE p.member.id = :memberId AND p.id <> :currentProjectId ORDER BY p.createdAt DESC")
    List<Project> findOtherProjectsByMember(@Param("memberId") Long memberId, @Param("currentProjectId") Long currentProjectId);
}
