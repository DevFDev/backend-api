package com.backend.devfordev.repository.PortfolioRepository;

import com.backend.devfordev.domain.PortfolioEntity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    @Query("SELECT c, (SELECT COUNT(h) FROM Heart h WHERE h.likeId = c.id AND h.likeType = 'PORTFOLIO') as likeCount " +
            "FROM Portfolio c JOIN FETCH c.member " +
            "WHERE c.deletedAt IS NULL")
    List<Object[]> findAllWithLikesAndMember();



    @Query("SELECT p FROM Portfolio p WHERE p.id = :portfolioId")
    Optional<Portfolio> findPortfolioById(@Param("portfolioId") Long portfolioId);


}
