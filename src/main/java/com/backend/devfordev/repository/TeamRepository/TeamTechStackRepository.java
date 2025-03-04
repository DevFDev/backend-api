package com.backend.devfordev.repository.TeamRepository;

import com.backend.devfordev.domain.TeamEntity.TeamTechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamTechStackRepository extends JpaRepository<TeamTechStack, Long> {
    @Modifying
    @Query("DELETE FROM TeamTechStack t WHERE t.team.id = :teamId")
    void deleteAllByTeamId(@Param("teamId") Long teamId);
}
