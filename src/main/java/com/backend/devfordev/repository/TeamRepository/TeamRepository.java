package com.backend.devfordev.repository.TeamRepository;

import com.backend.devfordev.domain.TeamEntity.Team;

import com.backend.devfordev.domain.enums.TeamType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("SELECT t, (SELECT COUNT(h) FROM Heart h WHERE h.likeId = t.id AND h.likeType = 'TEAM') as likeCount " +
            "FROM Team t JOIN FETCH t.member " +
            "WHERE t.deletedAt IS NULL")
    List<Object[]> findAllWithLikesAndMember();

    @Query("""
    SELECT t, COUNT(h) 
    FROM Team t
    LEFT JOIN Heart h ON t.id = h.likeId AND h.likeType = 'TEAM'
    LEFT JOIN t.teamTechStacks ts
    WHERE t.deletedAt IS NULL 
    AND (:searchTerm IS NULL OR LOWER(t.teamTitle) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
    AND (:teamType IS NULL OR t.teamType = :teamType)
    AND (:teamIsActive IS NULL OR t.teamIsActive = :teamIsActive)
    AND (:positions IS NULL OR t.teamPosition IN :positions)
    AND (:techStacks IS NULL OR ts.name IN :techStacks)
    GROUP BY t
    ORDER BY t.createdAt DESC
""")
    Page<Object[]> findTeamsWithLikes(
            @Param("searchTerm") String searchTerm,
            @Param("teamType") TeamType teamType,
            @Param("positions") List<String> positions,
            @Param("techStacks") List<String> techStacks,
            @Param("teamIsActive") Boolean teamIsActive,
            Pageable pageable
    );




}
