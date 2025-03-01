package com.backend.devfordev.repository.ProjectRepository;


import com.backend.devfordev.domain.ProjectEntity.Project;
import com.backend.devfordev.domain.enums.LikeType;
import com.backend.devfordev.domain.enums.ProjectCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p, COUNT(h) FROM Project p " +
            "LEFT JOIN Heart h ON p.id = h.likeId AND h.likeType = :likeType " +
            "JOIN p.member m " +
            "WHERE (:category IS NULL OR p.projectCategory = :category) " +
            "AND (:searchTerm IS NULL OR " +
            "LOWER(p.projectTitle) LIKE %:searchTerm% OR " +
            "LOWER(p.projectContent) LIKE %:searchTerm% OR " +
            "LOWER(m.name) LIKE %:searchTerm%) " +
            "GROUP BY p")
    List<Object[]> findProjectsWithLikes(
            @Param("category") ProjectCategory category,
            @Param("searchTerm") String searchTerm,
            @Param("likeType") LikeType likeType);

}
