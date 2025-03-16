package com.backend.devfordev.service.TeamService;


import com.backend.devfordev.converter.TeamConverter;
import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.MemberEntity.MemberInfo;
import com.backend.devfordev.domain.TeamEntity.Team;
import com.backend.devfordev.domain.TeamEntity.TeamComment;
import com.backend.devfordev.dto.CommunityDto.CommunityCommentResponse;
import com.backend.devfordev.dto.TeamDto.TeamCommentRequest;
import com.backend.devfordev.dto.TeamDto.TeamCommentResponse;
import com.backend.devfordev.repository.MemberRepository.MemberInfoRepository;
import com.backend.devfordev.repository.MemberRepository.MemberRepository;
import com.backend.devfordev.repository.TeamRepository.TeamCommentRepository;
import com.backend.devfordev.repository.TeamRepository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamCommentService {
    private final TeamCommentRepository commentRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;


    @Transactional
    public TeamCommentResponse addComment(Long teamId, Long userId, TeamCommentRequest request) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));


        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));


        TeamComment parentComment = null;
        if (request.getParentId() != null) {
            parentComment = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
        }

        MemberInfo memberInfoEntity = memberInfoRepository.findByMember(member);
        CommunityCommentResponse.MemberInfo memberInfo = new CommunityCommentResponse.MemberInfo(
                userId,
                memberInfoEntity.getImageUrl(), // MemberInfo의 imageUrl 사용
                memberInfoEntity.getNickname()  // MemberInfo의 nickname 사용
        );


        TeamComment comment = TeamComment.builder()
                .team(team)
                .member(member)
                .commentContent(request.getContent())
                .parent(parentComment)
                .build();
        TeamComment savedComment = commentRepository.save(comment);

        return TeamConverter.toTeamCommentResponse(savedComment, memberInfo);
    }


    @Transactional(readOnly = true)
    public List<TeamCommentResponse> getCommentsByTeamId(Long teamId) {
        // ✅ 최상위 댓글만 조회
        List<TeamComment> topLevelComments = commentRepository.findByTeamIdAndParentIsNull(teamId);

        return topLevelComments.stream()
                .map(this::convertWithReplies)  // 대댓글 포함 변환
                .collect(Collectors.toList());
    }

    private TeamCommentResponse convertWithReplies(TeamComment comment) {
        // Member와 연관된 MemberInfo 조회
        MemberInfo memberInfoEntity = memberInfoRepository.findByMember(comment.getMember());

        CommunityCommentResponse.MemberInfo memberInfo = new CommunityCommentResponse.MemberInfo(
                comment.getMember().getId(),
                memberInfoEntity.getImageUrl(), // MemberInfo의 imageUrl 사용
                memberInfoEntity.getNickname()  // MemberInfo의 nickname 사용
        );


        TeamCommentResponse response = TeamConverter.toTeamCommentResponse(comment, memberInfo);


        List<TeamCommentResponse> replies = comment.getChildren().stream()
                .map(this::convertWithReplies) // 재귀 호출
                .collect(Collectors.toList());

        response.setReplies(replies);
        return response;
    }
}
