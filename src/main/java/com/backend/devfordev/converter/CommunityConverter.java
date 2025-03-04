package com.backend.devfordev.converter;

import com.backend.devfordev.domain.CommunityEntity.Community;
import com.backend.devfordev.domain.CommunityEntity.CommunityComment;
import com.backend.devfordev.domain.MemberEntity.Member;
import com.backend.devfordev.domain.enums.CommunityCategory;
import com.backend.devfordev.dto.CommunityDto.CommunityCommentRequest;
import com.backend.devfordev.dto.CommunityDto.CommunityCommentResponse;
import com.backend.devfordev.dto.CommunityDto.CommunityRequest;
import com.backend.devfordev.dto.CommunityDto.CommunityResponse;

import java.util.stream.Collectors;

public class CommunityConverter {
    public static Community toCommunity(CommunityRequest.CommunityCreateRequest request, Member member, CommunityCategory category) {

        return Community.builder()
                .communityCategory(category)
                .communityTitle(request.getCommunityTitle())
                .communityContent(request.getCommunityContent())
                .isComment(request.getIsComment())
                .member(member)
                .communityViews(0L)
                .build();
    }


    public static CommunityResponse.CommunityCreateResponse toCommunityResponse(Community community) {
        return new CommunityResponse.CommunityCreateResponse(
                community.getId(),
                community.getCommunityCategory(),
                community.getCommunityTitle(),
                community.getCommunityContent(),
                community.getMember().getId(),
                community.getIsComment(),
                community.getCreatedAt()
        );
    }

    public static CommunityResponse.CommunityListResponse toCommunityListResponse(Community community, CommunityResponse.MemberInfo member, Long likeCount, String shortenedContent) {
        return new CommunityResponse.CommunityListResponse(
                community.getId(),
                community.getCommunityCategory(),
                community.getCommunityTitle(),
                shortenedContent,
                member,
                community.getCreatedAt(),
                0L,
                community.getCommunityViews(),
                likeCount
        );

    }

    public static CommunityResponse.CommunityDetailResponse toCommunityLDetailResponse(Community community, CommunityResponse.MemberInfo member, Long likeCount) {
        return new CommunityResponse.CommunityDetailResponse(
                community.getId(),
                community.getCommunityCategory(),
                community.getCommunityTitle(),
                community.getCommunityContent(),
                community.getIsComment(),
                member,
                community.getCreatedAt(),
                0L,
                community.getCommunityViews(),
                likeCount
        );

    }

    public static CommunityResponse.CommunityTop5Response toCommunityTop5Response(CommunityResponse.MemberInfo member, Long totalLikes) {
        return new CommunityResponse.CommunityTop5Response(
                member,
                totalLikes
        );
    }

    public static Community toUpdateCommunity(CommunityRequest.CommunityUpdateRequest request) {
        return Community.builder()
                .communityCategory(CommunityCategory.valueOf(request.getCommunityCategory().name()))
                .communityTitle(request.getCommunityTitle())
                .communityContent(request.getCommunityContent())
                .build();
    }


    public static CommunityResponse.CommunityUpdateResponse toCommunityUpdateResponse(Community community) {
        return new CommunityResponse.CommunityUpdateResponse(
                community.getId(),
                community.getCommunityCategory(),
                community.getCommunityTitle(),
                community.getCommunityContent(),
                community.getMember().getId(),
                community.getIsComment(),
                community.getCreatedAt(),
                community.getModifiedAt()
        );
    }

    public static CommunityComment toCommunityComment(CommunityCommentRequest request, Community community, Member member, CommunityComment parentComment) {
        return CommunityComment.builder()
                .commentContent(request.getContent())
                .community(community) // 게시글 정보 설정
                .member(member) // 작성자 정보 설정
                .parent(parentComment) // 부모 댓글 정보 설정
                .build();
    }


    public static CommunityCommentResponse toCommunityCommentResponse(CommunityComment comment) {
        return CommunityCommentResponse.builder()
                .commentId(comment.getId())
                .parentCommentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .content(comment.getCommentContent())
                .writer(comment.getMember().getId())
                .createdAt(comment.getCreatedAt())
//                .replies(comment.getChildren().stream()
//                        .map(CommunityConverter::toCommunityCommentResponse) // 답글도 재귀적으로 변환
//                        .collect(Collectors.toList()))
                .build();
    }

}
