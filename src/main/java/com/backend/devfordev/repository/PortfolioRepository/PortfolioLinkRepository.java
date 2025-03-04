package com.backend.devfordev.repository.PortfolioRepository;


import com.backend.devfordev.domain.PortfolioEntity.PortfolioLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioLinkRepository extends JpaRepository<PortfolioLink, Long> {
}
