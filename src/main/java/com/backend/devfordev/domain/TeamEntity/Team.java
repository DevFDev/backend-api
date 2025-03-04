package com.backend.devfordev.domain.TeamEntity;

import com.backend.devfordev.domain.BaseEntity;
import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.enums.TeamType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "team")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(name = "team_title", nullable = false)
    private String teamTitle;

    @Column(name = "team_content", columnDefinition = "TEXT", nullable = false)
    private String teamContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "team_type", nullable = false)
    private TeamType teamType;

    @Column(name = "team_position", nullable = false)
    private String teamPosition;

    @Column(name = "team_recruitment_num", nullable = false)
    private String teamRecruitmentNum;


    @Column(name = "team_is_active", nullable = false)
    private Boolean teamIsActive;

    @Column(name = "team_views", nullable = false)
    private Long teamViews = 0L;  // 기본값 설정


    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamTechStack> teamTechStacks = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamTagMap> teamTagMaps = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;



}
