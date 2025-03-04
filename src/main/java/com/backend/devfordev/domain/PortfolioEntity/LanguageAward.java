package com.backend.devfordev.domain.PortfolioEntity;

import com.backend.devfordev.domain.enums.AwardType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
@DiscriminatorValue("LANGUAGE")
public class LanguageAward extends PortfolioAward {
    private String language;

    private String testName;
    private String score;
    private LocalDate obtainedDate;
    protected LanguageAward() {
        super();
    }

    public LanguageAward(Long id, Integer orderIndex, AwardType awardType, Portfolio portfolio,
                         String language, String testName, String score, LocalDate obtainedDate) {
        super(id, orderIndex, awardType, portfolio);
        this.language = language;

        this.testName = testName;
        this.score = score;
        this.obtainedDate = obtainedDate;
    }
}
