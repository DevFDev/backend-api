package com.backend.devfordev.domain.PortfolioEntity;

import com.backend.devfordev.domain.PortfolioEntity.Portfolio;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "portfolio_link")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PortfolioLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String url;
    private Integer orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "port_id")
    private Portfolio portfolio;
}