package com.backend.devfordev.domain.PortfolioEntity;

import com.backend.devfordev.domain.enums.AwardType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@DiscriminatorValue("COMPETITION")
public class CompetitionAward extends PortfolioAward {

    private String competitionName;      // 수상 및 공모전명
    private String hostingInstitution;   // 주최기관
    private LocalDate competitionDate;   // 공모일

    protected CompetitionAward() {
        super();
    }
    public CompetitionAward(Long id, Integer orderIndex, AwardType awardType, Portfolio portfolio,
                            String competitionName, String hostingInstitution, LocalDate competitionDate) {
        super(id, orderIndex, awardType, portfolio);
        this.competitionName = competitionName;
        this.hostingInstitution = hostingInstitution;
        this.competitionDate = competitionDate;
    }
}