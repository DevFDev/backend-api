package com.backend.devfordev.repository.PortfolioRepository;

import com.backend.devfordev.domain.PortfolioEntity.Portfolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {


        @Query("""
        SELECT p, COUNT(h), m
        FROM Portfolio p
        LEFT JOIN Heart h ON p.id = h.likeId AND h.likeType = 'PORTFOLIO'
        JOIN MemberInfo m ON p.member.id = m.member.id
        WHERE (COALESCE(:searchTerm, '') = '' OR LOWER(p.portTitle) LIKE LOWER(CONCAT('%', :searchTerm, '%')) 
               OR LOWER(p.portContent) LIKE LOWER(CONCAT('%', :searchTerm, '%')) 
               OR LOWER(m.nickname) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
        AND (:position IS NULL OR LOWER(p.portPosition) = LOWER(:position))
        AND p.deletedAt IS NULL 
        GROUP BY p, m
        """)
        Page<Object[]> findAllWithLikesAndMember(
                @Param("searchTerm") Optional<String> searchTerm,
                @Param("position") String position,
                Pageable pageable
        );



    @Query("SELECT p FROM Portfolio p WHERE p.id = :portfolioId")
    Optional<Portfolio> findPortfolioById(@Param("portfolioId") Long portfolioId);


}
