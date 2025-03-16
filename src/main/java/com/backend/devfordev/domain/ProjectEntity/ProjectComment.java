package com.backend.devfordev.domain.ProjectEntity;

import com.backend.devfordev.domain.BaseEntity;
import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.PortfolioEntity.Portfolio;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "project_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProjectComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pro_comment_id")
    private Long id;

    @Column(name = "comment_content", nullable = false)
    private String commentContent;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "pro_id")
    private Project project;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private ProjectComment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProjectComment> children = new ArrayList<>();

}
