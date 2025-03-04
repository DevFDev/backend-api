package com.backend.devfordev.repository.PortfolioRepository;

import com.backend.devfordev.domain.PortfolioEntity.Portfolio;
import com.backend.devfordev.domain.PortfolioEntity.PortfolioAward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioAwardRepository extends JpaRepository<PortfolioAward, Long> {
    List<PortfolioAward> findByPortfolio(Portfolio portfolio);
}
