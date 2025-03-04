package com.backend.devfordev.repository.TeamRepository;

import com.backend.devfordev.domain.TeamEntity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("SELECT t, (SELECT COUNT(h) FROM Heart h WHERE h.likeId = t.id AND h.likeType = 'TEAM') as likeCount " +
            "FROM Team t JOIN FETCH t.member " +
            "WHERE t.deletedAt IS NULL")
    List<Object[]> findAllWithLikesAndMember();
}
