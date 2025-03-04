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
            "WHERE (:category IS NULL OR p.projectCategory = :category) " +
            "AND (:searchTerm IS NULL OR LOWER(p.projectTitle) LIKE %:searchTerm% OR LOWER(p.projectContent) LIKE %:searchTerm%) " +
            "GROUP BY p")
    Page<Object[]> findProjectsWithLikes(
            @Param("category") ProjectCategory category,
            @Param("searchTerm") String searchTerm,
            @Param("likeType") LikeType likeType,
            Pageable pageable
    );


    @Query("SELECT p, COUNT(h), mi FROM Project p " +
            "LEFT JOIN p.member m " +
            "LEFT JOIN MemberInfo mi ON mi.member = m " +
            "LEFT JOIN Heart h ON p.id = h.likeId AND h.likeType = 'PROJECT' " +
            "WHERE (:category IS NULL OR p.projectCategory = :category) " +
            "AND (:searchTerm IS NULL OR LOWER(p.projectTitle) LIKE %:searchTerm%) " +
            "GROUP BY p, mi")
    Page<Object[]> findProjectsWithLikes(
            @Param("category") ProjectCategory category,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );



    @Query("SELECT p, COUNT(h), mi FROM Project p " +
            "LEFT JOIN p.member m " +
            "LEFT JOIN MemberInfo mi ON mi.member = m " +
            "LEFT JOIN Heart h ON p.id = h.likeId AND h.likeType = 'PROJECT' " +
            "WHERE (:category IS NULL OR p.projectCategory = :category) " +
            "AND (:searchTerm IS NULL OR LOWER(p.projectTitle) LIKE %:searchTerm%) " +
            "GROUP BY p, mi " +
            "ORDER BY " +
            "   CASE WHEN :sortBy = 'views' THEN p.projectViews END DESC, " +
            "   CASE WHEN :sortBy = 'likes' THEN COUNT(h) END DESC, " +
            "   p.createdAt DESC")
    Page<Object[]> findProjectsWithLikes(
            @Param("category") ProjectCategory category,
            @Param("searchTerm") String searchTerm,
            @Param("sortBy") String sortBy,
            Pageable pageable
    );




    // 특정 작성자의 다른 프로젝트 조회 (현재 프로젝트 제외)
    @Query("SELECT p FROM Project p WHERE p.member.id = :memberId AND p.id <> :currentProjectId ORDER BY p.createdAt DESC")
    List<Project> findOtherProjectsByMember(@Param("memberId") Long memberId, @Param("currentProjectId") Long currentProjectId);

}
