package com.backend.devfordev.service.CommunityService;

import com.backend.devfordev.apiPayload.code.status.ErrorStatus;
import com.backend.devfordev.apiPayload.exception.handler.MemberHandler;
import com.backend.devfordev.converter.CommunityConverter;
import com.backend.devfordev.domain.CommunityEntity.Community;
import com.backend.devfordev.domain.CommunityEntity.CommunityAnswer;
import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.MemberEntity.MemberInfo;
import com.backend.devfordev.dto.CommunityDto.CommunityAnswerRequest;
import com.backend.devfordev.dto.CommunityDto.CommunityAnswerResponse;
import com.backend.devfordev.dto.CommunityDto.CommunityResponse;
import com.backend.devfordev.repository.CommunityRepository.CommunityAnswerRepository;
import com.backend.devfordev.repository.CommunityRepository.CommunityRepository;
import com.backend.devfordev.repository.MemberRepository.MemberInfoRepository;
import com.backend.devfordev.repository.MemberRepository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityAnswerService {
    private final CommunityAnswerRepository answerRepository;
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;

    @Transactional
    public CommunityAnswerResponse addAnswer(Long communityId, Long userId, CommunityAnswerRequest request) {

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("Community not found"));


        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.INVALID_MEMBER));

        MemberInfo memberInfoEntity = memberInfoRepository.findByMember(member);
        CommunityResponse.MemberInfo memberInfo = new CommunityResponse.MemberInfo(
                userId,
                memberInfoEntity.getImageUrl(),
                memberInfoEntity.getNickname()
        );


        CommunityAnswer answer = CommunityAnswer.builder()
                .community(community)
                .member(member)
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        CommunityAnswer savedAnswer = answerRepository.save(answer);

        return CommunityConverter.toCommunityAnswerResponse(savedAnswer, memberInfo);
    }

    @Transactional(readOnly = true)
    public List<CommunityAnswerResponse> getAnswersByCommunityId(Long communityId) {
        // ✅ 커뮤니티 글 확인
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("Community not found"));

        // ✅ 해당 커뮤니티 글의 답변 조회
        List<CommunityAnswer> answers = answerRepository.findByCommunityOrderByCreatedAtDesc(community);

        return answers.stream()
                .map(answer -> {
                    // MemberInfo 조회
                    MemberInfo memberInfoEntity = memberInfoRepository.findByMember(answer.getMember());
                    CommunityResponse.MemberInfo memberInfo = new CommunityResponse.MemberInfo(
                            answer.getMember().getId(),
                            memberInfoEntity.getImageUrl(),
                            memberInfoEntity.getNickname()
                    );
                    return CommunityConverter.toCommunityAnswerResponse(answer, memberInfo);
                })
                .collect(Collectors.toList());
    }

}
