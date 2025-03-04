package com.backend.devfordev.domain.PortfolioEntity;


import com.backend.devfordev.domain.BaseEntity;
import com.backend.devfordev.domain.MemberEntity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "portfolio")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Portfolio extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "port_id")
    private Long id;

    @Column(name = "port_title", nullable = false)
    private String portTitle;

    @Column(name = "port_content", nullable = false)
    private String portContent;

    @Column(name = "port_position", nullable = false)
    private String portPosition;

    @Column(name = "tech_stacks")
    private String techStacks;

    @Column(name = "port_tags")
    private String tags;

    @Column(name = "port_image")
    private String portImageUrl;

    @Column(name = "port_views", nullable = false)
    private Long portViews;  // 기본값 설정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    // techStacks 설정 메서드 - 저장 시 쉼표로 구분된 문자열로 변환
    public void setTechStacks(List<String> techStacks) {
        this.techStacks = String.join(",", techStacks);
    }
    public void setTags(List<String> tags) {
        this.tags = String.join(",", tags);
    }

    // techStacks 반환 메서드 - 응답 시 리스트 형식으로 변환
    public List<String> getTechStacks() {
        return techStacks != null ? Arrays.asList(techStacks.split(",")) : new ArrayList<>();
    }

    public List<String> getTags() {
        return tags != null ? Arrays.asList(tags.split(",")) : new ArrayList<>();
    }
}
