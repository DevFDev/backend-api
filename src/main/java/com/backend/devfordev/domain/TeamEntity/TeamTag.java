package com.backend.devfordev.domain.TeamEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "team_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TeamTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_tag_id")
    private Long id;

    @Column(name = "team_tag_name", nullable = false)
    private String name;
}
