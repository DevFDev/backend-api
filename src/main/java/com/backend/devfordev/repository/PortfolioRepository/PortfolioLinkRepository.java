package com.backend.devfordev.repository.PortfolioRepository;


import com.backend.devfordev.domain.PortfolioEntity.Portfolio;
import com.backend.devfordev.domain.PortfolioEntity.PortfolioLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioLinkRepository extends JpaRepository<PortfolioLink, Long> {
    List<PortfolioLink> findByPortfolio(Portfolio portfolio);
}
