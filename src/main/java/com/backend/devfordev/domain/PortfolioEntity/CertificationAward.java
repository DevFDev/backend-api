package com.backend.devfordev.domain.PortfolioEntity;

import com.backend.devfordev.domain.enums.AwardType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
@DiscriminatorValue("CERTIFICATION")
public class CertificationAward extends PortfolioAward {
    private String certificationName;
    private String issuingInstitution;
    private LocalDate passingDate;


    protected CertificationAward() {
        super();
    }
    public CertificationAward(Long id, Integer orderIndex, AwardType awardType, Portfolio portfolio,
                              String certificateName, String issuer, LocalDate passingDate) {
        super(id, orderIndex, awardType, portfolio);
        this.certificationName = certificateName;
        this.issuingInstitution = issuer;
        this.passingDate = passingDate;
    }
}
