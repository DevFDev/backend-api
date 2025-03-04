package com.backend.devfordev.domain.PortfolioEntity;

import com.backend.devfordev.domain.enums.AwardType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
@DiscriminatorValue("ACTIVITY")
public class ActivityAward extends PortfolioAward {
    private String activityName;
    private LocalDate startDate;
    private LocalDate endDate;


    protected ActivityAward() {
        super();
    }

    public ActivityAward(Long id, Integer orderIndex, AwardType awardType, Portfolio portfolio,
                         String activityName, LocalDate startDate, LocalDate endDate) {
        super(id, orderIndex, awardType, portfolio);
        this.activityName = activityName;
        this.startDate = startDate;
        this.endDate = endDate;

    }
}
