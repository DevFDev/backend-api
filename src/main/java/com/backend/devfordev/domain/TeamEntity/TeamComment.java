package com.backend.devfordev.domain.TeamEntity;

import com.backend.devfordev.domain.BaseEntity;
import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.ProjectEntity.Project;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "team_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TeamComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_comment_id")
    private Long id;

    @Column(name = "comment_content", nullable = false)
    private String commentContent;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private TeamComment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TeamComment> children = new ArrayList<>();

}
