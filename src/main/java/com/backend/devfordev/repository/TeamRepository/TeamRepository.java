package com.backend.devfordev.repository.TeamRepository;

import com.backend.devfordev.domain.TeamEntity.Team;
import com.backend.devfordev.domain.enums.LikeType;
import com.backend.devfordev.domain.enums.TeamType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface TeamRepository extends JpaRepository<Team, Long> {


    @Query("SELECT DISTINCT t FROM Team t " +
            "JOIN FETCH t.member m " +
            "LEFT JOIN FETCH t.teamTechStacks ts " +
            "LEFT JOIN FETCH t.teamTagMaps tm " +
            "WHERE (:teamType IS NULL OR t.teamType = :teamType) " +
            "AND (:searchTerm IS NULL OR " +
            "LOWER(t.teamTitle) LIKE %:searchTerm% OR " +
            "LOWER(t.teamContent) LIKE %:searchTerm% OR " +
            "LOWER(m.name) LIKE %:searchTerm%) " +
            "AND (:teamIsActive IS NULL OR t.teamIsActive = :teamIsActive) " +
            "AND (:positions IS NULL OR t.teamPosition IN :positions) " +
            "AND (:techStacks IS NULL OR ts.name IN :techStacks)")
    Page<Team> findByFilters(
            @Param("teamType") TeamType teamType,
            @Param("searchTerm") String searchTerm,
            @Param("teamIsActive") Boolean teamIsActive,
            @Param("positions") List<String> positions,
            @Param("techStacks") List<String> techStacks,
            Pageable pageable);



    @Query("SELECT t, (SELECT COUNT(h) FROM Heart h WHERE h.likeId = t.id AND h.likeType = 'TEAM') as likeCount " +
            "FROM Team t JOIN FETCH t.member " +
            "WHERE t.deletedAt IS NULL")
    List<Object[]> findAllWithLikesAndMember();
}
