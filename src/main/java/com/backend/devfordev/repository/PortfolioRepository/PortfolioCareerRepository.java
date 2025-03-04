package com.backend.devfordev.repository.PortfolioRepository;

import com.backend.devfordev.domain.PortfolioEntity.PortfolioCareer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioCareerRepository extends JpaRepository<PortfolioCareer, Long> {
}