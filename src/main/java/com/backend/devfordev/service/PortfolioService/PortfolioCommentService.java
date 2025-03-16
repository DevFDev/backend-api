package com.backend.devfordev.service.PortfolioService;

import com.backend.devfordev.converter.CommunityConverter;
import com.backend.devfordev.converter.PortfolioConverter;
import com.backend.devfordev.domain.CommunityEntity.Community;
import com.backend.devfordev.domain.CommunityEntity.CommunityComment;
import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.MemberEntity.MemberInfo;
import com.backend.devfordev.domain.PortfolioEntity.Portfolio;
import com.backend.devfordev.domain.PortfolioEntity.PortfolioComment;
import com.backend.devfordev.dto.CommunityDto.CommunityCommentRequest;
import com.backend.devfordev.dto.CommunityDto.CommunityCommentResponse;
import com.backend.devfordev.dto.PortfolioDto.PortfolioCommentRequest;
import com.backend.devfordev.dto.PortfolioDto.PortfolioCommentResponse;
import com.backend.devfordev.repository.CommunityRepository.CommunityCommentRepository;
import com.backend.devfordev.repository.CommunityRepository.CommunityRepository;
import com.backend.devfordev.repository.MemberRepository.MemberInfoRepository;
import com.backend.devfordev.repository.MemberRepository.MemberRepository;
import com.backend.devfordev.repository.PortfolioRepository.PortfolioCommentRepository;
import com.backend.devfordev.repository.PortfolioRepository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioCommentService {
    private final PortfolioCommentRepository commentRepository;
    private final PortfolioRepository portfolioRepository;
    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    /**
     * ✅ 포트폴리오 댓글 추가
     */
    @Transactional
    public PortfolioCommentResponse addComment(Long portfolioId, Long userId, PortfolioCommentRequest request) {
        // ✅ 포트폴리오 존재 여부 확인
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found"));

        // ✅ 사용자 존재 여부 확인
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // ✅ 부모 댓글이 존재하는 경우 확인 (대댓글 기능)
        PortfolioComment parentComment = null;
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

        // ✅ 댓글 엔티티 생성
        PortfolioComment comment = PortfolioComment.builder()
                .portfolio(portfolio)
                .member(member)
                .commentContent(request.getContent())
                .parent(parentComment)
                .build();

        // ✅ 댓글 저장
        PortfolioComment savedComment = commentRepository.save(comment);

        return PortfolioConverter.toPortfolioCommentResponse(savedComment, memberInfo);
    }

    /**
     * ✅ 포트폴리오 댓글 조회 (대댓글 포함)
     */
    @Transactional(readOnly = true)
    public List<PortfolioCommentResponse> getCommentsByPortfolioId(Long portfolioId) {
        // ✅ 최상위 댓글만 조회
        List<PortfolioComment> topLevelComments = commentRepository.findByPortfolioIdAndParentIsNull(portfolioId);

        return topLevelComments.stream()
                .map(this::convertWithReplies)  // 대댓글 포함 변환
                .collect(Collectors.toList());
    }

    /**
     * ✅ 대댓글 변환
     */
    private PortfolioCommentResponse convertWithReplies(PortfolioComment comment) {
        // Member와 연관된 MemberInfo 조회
        MemberInfo memberInfoEntity = memberInfoRepository.findByMember(comment.getMember());

        CommunityCommentResponse.MemberInfo memberInfo = new CommunityCommentResponse.MemberInfo(
                comment.getMember().getId(),
                memberInfoEntity.getImageUrl(), // MemberInfo의 imageUrl 사용
                memberInfoEntity.getNickname()  // MemberInfo의 nickname 사용
        );
        // ✅ 최상위 댓글 변환
        PortfolioCommentResponse response = PortfolioConverter.toPortfolioCommentResponse(comment, memberInfo);

        // ✅ 대댓글(Children) 처리
        List<PortfolioCommentResponse> replies = comment.getChildren().stream()
                .map(this::convertWithReplies) // 재귀 호출
                .collect(Collectors.toList());

        response.setReplies(replies);
        return response;
    }
}
