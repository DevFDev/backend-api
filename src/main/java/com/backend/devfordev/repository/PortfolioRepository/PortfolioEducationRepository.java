package com.backend.devfordev.repository.PortfolioRepository;

import com.backend.devfordev.domain.PortfolioEntity.Portfolio;
import com.backend.devfordev.domain.PortfolioEntity.PortfolioEducation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioEducationRepository extends JpaRepository<PortfolioEducation, Long> {
    List<PortfolioEducation> findByPortfolio(Portfolio portfolio);

    void deleteByPortfolio(Portfolio portfolio);

}
