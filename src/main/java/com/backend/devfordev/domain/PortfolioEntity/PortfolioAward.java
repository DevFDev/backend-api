package com.backend.devfordev.domain.PortfolioEntity;

import com.backend.devfordev.domain.PortfolioEntity.Portfolio;
import com.backend.devfordev.domain.enums.AwardType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "award_type")
@Table(name = "portfolio_award")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PortfolioAward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "port_award_id")
    private Long id;

    @Column(name = "orderIndex", nullable = false)
    private Integer orderIndex;

    @Enumerated(EnumType.STRING)
    @Column(name = "award_type", insertable = false, updatable = false)
    private AwardType awardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "port_id")
    private Portfolio portfolio;  // 포트폴리오와의 연관 관계
}


