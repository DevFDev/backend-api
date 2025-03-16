package com.backend.devfordev.dto.TeamDto;

import com.backend.devfordev.domain.enums.TeamType;
import com.backend.devfordev.dto.CommunityDto.CommunityResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class TeamResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamCreateResponse {
        @Schema(description = "팀 모집 id", example = "1")
        Long id;
        @Schema(description = "팀 모집 제목", example = "팀 모집 제목입니다.")
        String teamTitle;
        @Schema(description = "팀 모집 내용", example = "팀 모집 내용입니다.")
        String teamContent;
        @Schema(description = "팀 모집 타입", example = "STUDY")
        TeamType teamType;
        @Schema(description = "팀 모집 포지션", example = "BACKEND")
        String teamPosition;
        @Schema(description = "팀 모집 인원", example = "1")
        Long teamRecruitmentNum;
        @Schema(description = "기술 스택", example = "[\"Java\", \"Spring\", \"AWS\"]")
        List<String> teamTechStack;
        @Schema(description = "태그", example = "[\"태그1\", \"태그2\", \"태그3\"]")
        List<String> teamTags;
        @Schema(description = "작성시간", example = "2024-11-19T00:52:47.534061")
        LocalDateTime createdAt;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamListResponse {

        @Schema(description = "팀 모집 id", example = "1")
        Long id;
        CommunityResponse.MemberInfo writer;
        @Schema(description = "팀 모집 제목", example = "팀 모집 제목입니다.")
        String teamTitle;
        @Schema(description = "팀 모집 내용", example = "팀 모집 내용입니다.")
        String teamContent;
        @Schema(description = "팀 모집 타입", example = "STUDY")
        TeamType teamType;
        @Schema(description = "팀 모집 포지션", example = "BACKEND")
        String teamPosition;
        @Schema(description = "팀 모집 인원", example = "1")
        Long teamRecruitmentNum;
        @Schema(description = "팀 멤버 수", example = "1")
        Long currentTeamNum;
        @Schema(description = "기술 스택", example = "[\"Java\", \"Spring\", \"AWS\"]")
        List<String> teamTechStack;
        @Schema(description = "태그", example = "[\"태그1\", \"태그2\", \"태그3\"]")
        List<String> teamTags;
        @Schema(description = "작성시간", example = "2024-11-19T00:52:47.534061")
        LocalDateTime createdAt;
        @Schema(description = "모집중 여부", example = "true")
        Boolean teamIsActive;

        @Schema(description = "조회수", example = "0")
        Long views;
        @Schema(description = "답변수", example = "0")
        Long answers;
        @Schema(description = "좋아요수", example = "0")
        Long likes;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamDetailResponse {
        @Schema(description = "팀 모집 id", example = "1")
        Long id;
        CommunityResponse.MemberInfo writer;
        @Schema(description = "팀 모집 제목", example = "팀 모집 제목입니다.")
        String teamTitle;
        @Schema(description = "팀 모집 내용", example = "팀 모집 내용입니다.")
        String teamContent;
        @Schema(description = "팀 모집 타입", example = "STUDY")
        TeamType teamType;
        @Schema(description = "팀 모집 포지션", example = "BACKEND")
        String teamPosition;
        @Schema(description = "팀 모집 인원", example = "1")
        Long teamRecruitmentNum;
        @Schema(description = "기술 스택", example = "[\"Java\", \"Spring\", \"AWS\"]")
        List<String> teamTechStack;
        @Schema(description = "태그", example = "[\"태그1\", \"태그2\", \"태그3\"]")
        List<String> teamTags;
        @Schema(description = "작성시간", example = "2024-11-19T00:52:47.534061")
        LocalDateTime createdAt;
        @Schema(description = "모집중 여부", example = "true")
        Boolean teamIsActive;
        @Schema(description = "조회수", example = "0")
        Long views;
        @Schema(description = "답변수", example = "0")
        Long answers;
        @Schema(description = "좋아요수", example = "0")
        Long likes;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamAddMemberResponse {
        @Schema(description = "팀원 추가 id", example = "1")
        Long id;
        @Schema(description = "팀 모집 id", example = "1")
        Long teamId;
        @Schema(description = "추가된 멤버 id", example = "1")
        Long memberId;
    }

    @Getter
    @AllArgsConstructor
    public static class TeamMemberListWithIdResponse {
        @Schema(description = "팀 모집 id", example = "1")
        private Long teamId; // 팀 ID 한 번만 포함
        private List<TeamMemberListResponse> members; // 팀 멤버 리스트
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamMemberListResponse {
        @Schema(description = "팀원 추가 id", example = "1")
        //private Long id;
        private CommunityResponse.MemberInfo member; // 팀원의 정보 (MemberInfo를 사용)
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamUpdateResponse {
        @Schema(description = "팀 모집 id", example = "1")
        Long id;
        @Schema(description = "팀 모집 제목", example = "팀 모집 제목입니다.")
        String teamTitle;
        @Schema(description = "팀 모집 내용", example = "팀 모집 내용입니다.")
        String teamContent;
        @Schema(description = "팀 모집 타입", example = "STUDY")
        TeamType teamType;
        @Schema(description = "팀 모집 포지션", example = "BACKEND")
        String teamPosition;
        @Schema(description = "팀 모집 인원", example = "1")
        Long teamRecruitmentNum;
        @Schema(description = "기술 스택", example = "[\"Java\", \"Spring\", \"AWS\"]")
        List<String> teamTechStack;
        @Schema(description = "태그", example = "[\"태그1\", \"태그2\", \"태그3\"]")
        List<String> teamTags;
        @Schema(description = "작성시간", example = "2024-11-19T00:52:47.534061")
        LocalDateTime createdAt;
    }
}
