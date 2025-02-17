package com.backend.devfordev.repository.ProjectRepository;


import com.backend.devfordev.domain.ProjectEntity.Project;
import com.backend.devfordev.domain.enums.ProjectCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p " +
            "JOIN p.member m " +
            "WHERE (:category IS NULL OR p.projectCategory = :category) " +
            "AND (:searchTerm IS NULL OR " +
            "LOWER(p.projectTitle) LIKE %:searchTerm% OR " +
            "LOWER(p.projectContent) LIKE %:searchTerm% OR " +
            "LOWER(m.name) LIKE %:searchTerm%)")
    Page<Project> findByFilters(
            @Param("category") ProjectCategory category,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );
}
