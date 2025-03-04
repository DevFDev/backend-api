package com.backend.devfordev.dto.CommunityDto;

import com.backend.devfordev.domain.enums.CommunityCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

public class CommunityResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommunityCreateResponse {
        @Schema(description = "커뮤니티 ID", example = "1")
        Long id;
        @Schema(description = "커뮤니티 카테고리", example = "SKILL")
        CommunityCategory communityCategory;
        @Schema(description = "커뮤니티 제목", example = "커뮤니티 제목 부분입니다.")
        String communityTitle;
        @Schema(description = "커뮤니티 내용", example = "커뮤니티 내용 부분입니다.")
        String communityContent;
        @Schema(description = "작성자 ID", example = "1")
        Long writer;
        @Schema(description = "커뮤니티 답변 동의 여부", example = "true")
        Boolean isComment;
        @Schema(description = "작성시간", example = "2024-11-19T00:52:47.534061")
        LocalDateTime createdAt;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberInfo {

        @Schema(description = "멤버 id", example = "1")
        private Long id;
        @Schema(description = "프로필 사진 url", example = "domain 주소")
        private String imageUrl;
        @Schema(description = "멤버 이름", example = "김민지")
        private String nickname;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommunityListResponse {
        @Schema(description = "커뮤니티 ID", example = "1")
        Long id;
        @Schema(description = "커뮤니티 카테고리", example = "SKILL")
        CommunityCategory communityCategory;
        @Schema(description = "커뮤니티 제목", example = "커뮤니티 제목 부분입니다.")
        String communityTitle;
        @Schema(description = "커뮤니티 내용", example = "커뮤니티 내용 부분입니다.")
        String communityContent;

        MemberInfo writer;
        @Schema(description = "작성시간", example = "2024-11-19T00:52:47.534061")
        LocalDateTime createdAt;
        @Schema(description = "답변수", example = "0")
        Long answers;
        @Schema(description = "조회수", example = "0")
        Long views;
        @Schema(description = "좋아요수", example = "0")
        Long likes;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommunityDetailResponse {
        @Schema(description = "커뮤니티 ID", example = "1")
        Long id;
        @Schema(description = "커뮤니티 카테고리", example = "SKILL")
        CommunityCategory communityCategory;
        @Schema(description = "커뮤니티 제목", example = "커뮤니티 제목 부분입니다.")
        String communityTitle;
        @Schema(description = "커뮤니티 내용", example = "커뮤니티 내용 부분입니다.")
        String communityContent;
        @Schema(description = "커뮤니티 답변 동의 여부", example = "true")
        Boolean isComment;
        MemberInfo writer;
        @Schema(description = "작성시간", example = "2024-11-19T00:52:47.534061")
        LocalDateTime createdAt;
        @Schema(description = "답변수", example = "0")
        Long answers;
        @Schema(description = "조회수", example = "0")
        Long views;
        @Schema(description = "좋아요수", example = "0")
        Long likes;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommunityTop5Response {
        MemberInfo member;
        @Schema(description = "해당 유저가 받은 전체 좋아요수", example = "22")
        Long totalLikes;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommunityUpdateResponse {
        @Schema(description = "커뮤니티 ID", example = "1")
        Long id;
        @Schema(description = "커뮤니티 카테고리", example = "SKILL")
        CommunityCategory communityCategory;
        @Schema(description = "커뮤니티 제목", example = "커뮤니티 제목 부분입니다.")
        String communityTitle;
        @Schema(description = "커뮤니티 내용", example = "커뮤니티 내용 부분입니다.")
        String communityContent;
        @Schema(description = "작성자 ID", example = "1")
        Long writer;
        @Schema(description = "커뮤니티 답변 동의 여부", example = "true")
        Boolean isComment;
        @Schema(description = "작성시간", example = "2024-11-19T00:52:47.534061")
        LocalDateTime createdAt;
        @Schema(description = "수정시간", example = "2024-11-19T00:52:47.534061")
        LocalDateTime updatedAt;
    }

}
