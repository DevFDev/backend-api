package com.backend.devfordev.domain.PortfolioEntity;

import com.backend.devfordev.domain.PortfolioEntity.Portfolio;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "portfolio_education")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PortfolioEducation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "education_id")
    private Long id;

    @Column(name = "education_level", nullable = false)
    private String level;  // 예: "대학(2,3년)", "대학(4년)", "대학원" 등

    @Column(name = "institution_name", nullable = false)
    private String institutionName;  // 학교 이름

    @Column(name = "major", nullable = false)
    private String major;  // 전공

    @Column(name = "admission_date", nullable = false)
    private LocalDate admissionDate;  // 입학 연월

    @Column(name = "graduation_date", nullable = false)
    private LocalDate graduationDate;  // 졸업 연월

    @Column(name = "graduation_status", nullable = false)
    private String graduationStatus;  // 졸업 여부 ("졸업", "재학", "휴학" 등)

    @Column(name = "is_transfer", nullable = false)
    private Boolean isTransfer;  // 졸업 여부 ("졸업", "재학", "휴학" 등)

    @Column(name = "grade", nullable = false)
    private Double grade;  // 학점

    @Column(name = "grade_scale", nullable = false)
    private Double gradeScale;  // 기준 학점 (예: 4.5, 5.0 등)

    @Column(name = "orderIndex", nullable = false)
    private Integer orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "port_id")
    private Portfolio portfolio;  // 포트폴리오와의 연관 관계
}
