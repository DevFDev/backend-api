package com.backend.devfordev.service.CommunityService;

import com.backend.devfordev.apiPayload.code.status.ErrorStatus;
import com.backend.devfordev.apiPayload.exception.handler.CommunityHandler;
import com.backend.devfordev.apiPayload.exception.handler.MemberHandler;
import com.backend.devfordev.converter.CommunityConverter;
import com.backend.devfordev.domain.CommunityEntity.Community;
import com.backend.devfordev.domain.CommunityEntity.CommunityComment;
import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.MemberEntity.MemberInfo;
import com.backend.devfordev.domain.enums.CommunityCategory;
import com.backend.devfordev.dto.CommunityDto.CommunityCommentRequest;
import com.backend.devfordev.dto.CommunityDto.CommunityCommentResponse;
import com.backend.devfordev.dto.CommunityDto.CommunityRequest;
import com.backend.devfordev.dto.CommunityDto.CommunityResponse;
import com.backend.devfordev.repository.*;
import com.backend.devfordev.repository.CommunityRepository.CommunityCommentRepository;
import com.backend.devfordev.repository.CommunityRepository.CommunityRepository;
import com.backend.devfordev.repository.MemberRepository.MemberInfoRepository;
import com.backend.devfordev.repository.MemberRepository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommunityServiceImpl implements CommunityService{
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;

    private final CommunityCommentRepository communityCommentRepository;
    private final MemberInfoRepository memberInfoRepository;
    @Override
    @Transactional
    public CommunityResponse.CommunityCreateResponse createCommunity(CommunityRequest.CommunityCreateRequest request, Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.INVALID_MEMBER));


        // communityCategory 예외 처리
        CommunityCategory category;
        try {
            category = request.getCommunityCategory();
        } catch (IllegalArgumentException e) {
            throw new CommunityHandler(ErrorStatus.INVALID_CATEGORY);
        }

        // Community 생성 (category를 직접 전달)
        Community community = CommunityConverter.toCommunity(request, member, category);


        communityRepository.save(community);
        // communityAI 필드가 1인 경우 OpenAI API로 댓글 생성
//        if (Boolean.TRUE.equals(request.getCommunityAI())) {
//            try {
//                // OpenAI API 호출
//                String aiCommentContent = openAIService.generateAIComment(community.getCommunityTitle(), community.getCommunityContent());
//
//                // 생성된 AI 댓글을 community_comment 테이블에 저장
//                CommunityComment aiComment = CommunityComment.builder()
//                        .community(community)
//                        .commentContent(aiCommentContent)
//                        .isAiComment(true)  // AI 댓글임을 표시
//                        .build();
//
//                communityCommentRepository.save(aiComment);
//
//            } catch (HttpClientErrorException e) {
//                // OpenAI API에서 잘못된 요청이나 401 Unauthorized 등의 에러 처리
//                throw new CommunityHandler(ErrorStatus.OPENAI_API_ERROR);
//            } catch (RestClientException e) {
//                // API 요청 중 네트워크 오류 등의 일반적인 예외 처리
//                throw new CommunityHandler(ErrorStatus.OPENAI_API_ERROR);
//            }
//        }

        return CommunityConverter.toCommunityResponse(community);
     }



    @Override
    @Transactional
    public List<CommunityResponse.CommunityListResponse> getCommunityList(Optional<CommunityCategory> categoryOpt, Optional<String> searchTermOpt, String sortBy) {
        // 커뮤니티와 좋아요 수를 한 번의 쿼리로 가져옴
        List<Object[]> results = communityRepository.findAllWithLikesAndMember();

        // 필터링 및 DTO 변환
        List<CommunityResponse.CommunityListResponse> communityList = results.stream()
                .map(result -> {
                    Community community = (Community) result[0];  // 첫 번째는 Community 객체
                    Long likeCount = (Long) result[1];            // 두 번째는 좋아요 수

                    // 카테고리 필터링
                    if (categoryOpt.isPresent() && !community.getCommunityCategory().equals(categoryOpt.get())) {
                        return null;
                    }

                    // 검색 필터링: 제목, 작성자 이름, 내용 중 하나라도 일치하면 true 반환
                    if (searchTermOpt.isPresent()) {
                        String searchTerm = searchTermOpt.get().toLowerCase();
                        boolean matches = community.getCommunityTitle().toLowerCase().contains(searchTerm) ||
                                community.getMember().getName().toLowerCase().contains(searchTerm) ||
                                community.getCommunityContent().toLowerCase().contains(searchTerm);
                        if (!matches) {
                            return null;
                        }
                    }
                    MemberInfo memberInfoEntity = memberInfoRepository.findByMember(community.getMember());

                    // MemberInfo 생성
                    CommunityResponse.MemberInfo memberInfo = new CommunityResponse.MemberInfo(
                            community.getMember().getId(),
                            memberInfoEntity.getImageUrl(),
                            memberInfoEntity.getNickname()
                    );

                    // communityContent를 80자까지만 잘라서 보여줌
                    String shortenedContent = community.getCommunityContent();
                    if (shortenedContent.length() > 80) {
                        shortenedContent = shortenedContent.substring(0, 80) + "...";  // 80자까지만 자르고 "..." 추가
                    }

                    // DTO 변환
                    return CommunityConverter.toCommunityListResponse(community, memberInfo, likeCount, shortenedContent);
                })
                .filter(Objects::nonNull)  // 필터링에서 null이 반환된 경우 제거
                .collect(Collectors.toList());

        // 정렬 기준에 따른 정렬
        switch (sortBy.toLowerCase()) {
            case "likes":   // 좋아요 순 정렬
                communityList.sort(Comparator.comparingLong(CommunityResponse.CommunityListResponse::getLikes).reversed());
                break;
            case "views":   // 조회수 순 정렬
                communityList.sort(Comparator.comparingLong(CommunityResponse.CommunityListResponse::getViews).reversed());
                break;
            case "recent":  // 최신순 정렬 (기본)
            default:
                communityList.sort(Comparator.comparing(CommunityResponse.CommunityListResponse::getCreatedAt).reversed());
                break;
        }

        return communityList;
    }

    @Override
    @Transactional
    public CommunityResponse.CommunityDetailResponse getCommunityDetail(Long id) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new CommunityHandler(ErrorStatus.COMMUNITY_NOT_FOUND));

        if (community.getDeletedAt() != null) {
            throw new CommunityHandler(ErrorStatus.COMMUNITY_DELETED);
        }

        Long Likecount = likeRepository.countByCommunityId(id);

        // Member와 연관된 MemberInfo 조회
        MemberInfo memberInfoEntity = memberInfoRepository.findByMember(community.getMember());

        CommunityResponse.MemberInfo memberInfo = new CommunityResponse.MemberInfo(
                community.getMember().getId(),
                memberInfoEntity.getImageUrl(), // MemberInfo의 imageUrl 사용
                memberInfoEntity.getNickname()  // MemberInfo의 nickname 사용
        );

        return CommunityConverter.toCommunityLDetailResponse(community, memberInfo, Likecount);
    }



    @Override
    @Transactional
    public List<CommunityResponse.CommunityTop5Response> getTop5UsersByTotalLikes() {
        // 유저별 총 좋아요 수 계산
        List<Object[]> totalLikesForUsers = communityRepository.findTop5UsersByTotalLikes();

        // 로그로 각 유저의 총 좋아요 수 출력
        totalLikesForUsers.forEach(result -> {
            Member member = (Member) result[0];
            Long totalLikes = (Long) result[1];
            System.out.println(member.getId());
            //log.info("Member ID: {}, Name: {}, Total Likes: {}", member.getId(), member.getName(), totalLikes);
        });

        // 좋아요 수 기준으로 상위 5명의 유저를 가져옴
        return totalLikesForUsers.stream()
                .limit(5)  // 상위 5명 가져오기
                .map(result -> {
                    Member member = (Member) result[0];  // 유저 객체
                    Long totalLikes = (Long) result[1];  // 총 좋아요 수

                    // Member와 연관된 MemberInfo 조회
                    MemberInfo memberInfoEntity = memberInfoRepository.findByMember(member);
                    // MemberInfo 생성
                    CommunityResponse.MemberInfo memberInfo = new CommunityResponse.MemberInfo(
                            member.getId(),
                            memberInfoEntity.getImageUrl(),
                            memberInfoEntity.getNickname()
                    );

                    // CommunityTop5Response로 변환
                    return CommunityConverter.toCommunityTop5Response(memberInfo, totalLikes);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCommunity(Long id, Long userId) {


        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new CommunityHandler(ErrorStatus.COMMUNITY_NOT_FOUND));


        if (community.getDeletedAt() != null) {
            throw new CommunityHandler(ErrorStatus.COMMUNITY_DELETED);
        }

        if (!community.getMember().getId().equals(userId)) {
            throw new CommunityHandler(ErrorStatus.UNAUTHORIZED_USER);
        }

        community.deleteSoftly();

        communityRepository.save(community);
    }

    @Override
    @Transactional
    public CommunityResponse.CommunityUpdateResponse updateCommunity(Long id, CommunityRequest.CommunityUpdateRequest request, Long userId) {

        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new CommunityHandler(ErrorStatus.COMMUNITY_NOT_FOUND));

        // communityCategory 예외 처리
        CommunityCategory category;
        try {
            category = request.getCommunityCategory();
        } catch (IllegalArgumentException e) {
            throw new CommunityHandler(ErrorStatus.INVALID_CATEGORY);
        }

        // 삭제된 커뮤니티는 수정 불가
        if (community.getDeletedAt() != null) {
            throw new CommunityHandler(ErrorStatus.COMMUNITY_DELETED);  // 삭제된 커뮤니티 예외 처리
        }

        // 글 작성자와 로그인한 유저가 동일한지 확인
        if (!community.getMember().getId().equals(userId)) {
            throw new CommunityHandler(ErrorStatus.UNAUTHORIZED_USER); // 예외 처리 (권한 없음)
        }

        // 컨버터를 사용하여 업데이트할 데이터 변환
        Community updatedCommunity = CommunityConverter.toUpdateCommunity(request);

        // 기존 데이터 수정
        community.setCommunityCategory(updatedCommunity.getCommunityCategory());
        community.setCommunityTitle(updatedCommunity.getCommunityTitle());
        community.setCommunityContent(updatedCommunity.getCommunityContent());

        //communityRepository.save(community);
        return CommunityConverter.toCommunityUpdateResponse(community);
    }

//    @Transactional
//    @Override
//    public CommunityCommentResponse createComment(Long communityId, Long memberId, CommunityCommentRequest request) {
//        // 게시글 조회
//        Community community = communityRepository.findById(communityId)
//                .orElseThrow(() -> new IllegalArgumentException("Community post not found"));
//
//        // 작성자 조회
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
//
//        // 부모 댓글 조회 (답글인 경우)
//        CommunityComment parentComment = null;
//        if (request.getParentId() != null) {
//            parentComment = communityCommentRepository.findById(request.getParentId())
//                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
//        }
//
//        // 댓글 엔티티 생성
//        CommunityComment comment = CommunityConverter.toCommunityComment(request, community, member, parentComment);
//
//        // 댓글 저장
//        CommunityComment savedComment = communityCommentRepository.save(comment);
//
//        // 저장된 댓글을 응답 객체로 변환하여 반환
//        return CommunityConverter.toCommunityCommentResponse(savedComment);
//    }


}
