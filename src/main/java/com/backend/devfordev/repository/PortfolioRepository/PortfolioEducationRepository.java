package com.backend.devfordev.repository.PortfolioRepository;

import com.backend.devfordev.domain.PortfolioEntity.PortfolioEducation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioEducationRepository extends JpaRepository<PortfolioEducation, Long> {
}
