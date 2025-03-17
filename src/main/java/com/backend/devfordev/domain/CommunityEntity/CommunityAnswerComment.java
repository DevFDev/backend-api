package com.backend.devfordev.domain.CommunityEntity;

import com.backend.devfordev.domain.BaseEntity;
import com.backend.devfordev.domain.MemberEntity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "community_answer_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommunityAnswerComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private CommunityAnswer answer;  // ✅ 어떤 답변에 대한 댓글인지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  // ✅ 댓글 작성자

    @Column(nullable = false)
    private String content;  // ✅ 댓글 내용

}
