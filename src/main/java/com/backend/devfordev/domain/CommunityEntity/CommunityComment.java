package com.backend.devfordev.domain.CommunityEntity;

import com.backend.devfordev.domain.BaseEntity;
import com.backend.devfordev.domain.MemberEntity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "community_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommunityComment extends BaseEntity {
    //댓글 id, 커뮤니티 id, 유저 id, 댓글 내용, 댓글 길이, 부모 댓글 id, 인공지능 여부
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "com_comment_id")
    private Long id;

    @Column(name = "comment_content", nullable = false)
    private String commentContent;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "com_id")
    private Community community;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private CommunityComment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CommunityComment> children = new ArrayList<>();

}
