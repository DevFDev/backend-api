package com.backend.devfordev.domain.CommunityEntity;

import com.backend.devfordev.domain.BaseEntity;
import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.enums.CommunityCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "community")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Community extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "com_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "com_category", nullable = false)
    private CommunityCategory communityCategory;


    @Column(name = "com_title", nullable = false)
    private String communityTitle;

    @Column(name = "com_content", nullable = false)
    private String communityContent;

    @Column(name = "com_ai")
    private Boolean isComment;

    @Column(name = "com_views", nullable = false)
    private Long communityViews;  // 기본값 설정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


}
