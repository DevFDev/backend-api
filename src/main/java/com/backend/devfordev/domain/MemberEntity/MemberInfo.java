package com.backend.devfordev.domain.MemberEntity;

import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.enums.Affiliation;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder

/*
이름
이메일

(프로필)
프로필 사진
이름
닉네임
소개
깃허브

(커리어)
포지션
기술스택
소속
프로필 완성률
 */
public class MemberInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_info_id")
    private Long id;

    @Column(name="nickname", nullable = false)
    private String nickname;

    @Column(name="image_url", nullable = false)
    private String imageUrl;

    @Column(name="Introduction")
    private String introduction;

    @Column(name="position")
    private String position;

    @Column(name="tech_stacks")
    private String techStacks;

    @Column(name="affiliation")
    private Affiliation affiliation;

    @Column(name="completion_rate")
    private Long completionRate = 0L;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
