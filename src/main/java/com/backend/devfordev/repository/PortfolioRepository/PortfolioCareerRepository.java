package com.backend.devfordev.repository.PortfolioRepository;

import com.backend.devfordev.domain.PortfolioEntity.Portfolio;
import com.backend.devfordev.domain.PortfolioEntity.PortfolioCareer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioCareerRepository extends JpaRepository<PortfolioCareer, Long> {
    List<PortfolioCareer> findByPortfolio(Portfolio portfolio);
}