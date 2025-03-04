package com.backend.devfordev.domain.TeamEntity;

import com.backend.devfordev.domain.TeamEntity.Team;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "team_tech_stack")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TeamTechStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_tech_stack_id")
    private Long id;

    @Column(name = "team_tech_name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
}