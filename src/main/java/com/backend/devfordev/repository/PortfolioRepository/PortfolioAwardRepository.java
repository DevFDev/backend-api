package com.backend.devfordev.repository.PortfolioRepository;

import com.backend.devfordev.domain.PortfolioEntity.PortfolioAward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioAwardRepository extends JpaRepository<PortfolioAward, Long> {
}
