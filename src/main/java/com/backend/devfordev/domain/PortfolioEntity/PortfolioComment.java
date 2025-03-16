package com.backend.devfordev.domain.PortfolioEntity;

import com.backend.devfordev.domain.BaseEntity;
import com.backend.devfordev.domain.CommunityEntity.Community;
import com.backend.devfordev.domain.MemberEntity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "portfolio_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PortfolioComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "port_comment_id")
    private Long id;

    @Column(name = "comment_content", nullable = false)
    private String commentContent;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "port_id")
    private Portfolio portfolio;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private PortfolioComment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PortfolioComment> children = new ArrayList<>();

}
