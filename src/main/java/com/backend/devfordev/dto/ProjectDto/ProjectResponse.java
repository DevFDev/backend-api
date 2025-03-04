package com.backend.devfordev.dto.ProjectDto;

import com.backend.devfordev.domain.enums.ProjectCategory;
import com.backend.devfordev.dto.CommunityDto.CommunityResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectCreateResponse {
        @Schema(description = "프로젝트 ID", example = "1")
        Long id;
        @Schema(description = "작성자 ID", example = "1")
        Long writer;
        @Schema(description = "프로젝트  제목", example = "김민지의 프로젝트 ~~")
        String projectTitle;
        @Schema(description = "프로젝트 내용", example = "프로젝트 내용~~")
        String projectContent;
        @NotNull(message = "This field must not be null.")
        @Schema(description = "프로젝트 개요", example = "프로젝트 개요~~")
        String projectSummary;
        @Schema(description = "프로젝트 분류", example = "APP")
        ProjectCategory projectCategory;
        @Schema(description = "태그", example = "[\"태그1\", \"태그2\", \"태그3\"]")
        List<String> tags;
        @Schema(description = "기술 스택", example = "[\"Java\", \"Spring\", \"AWS\"]")
        List<String> projectTechStacks;
        @Schema(description = "프로젝트 이미지 url", example = "이미지url")
        String projectImageUrl;
        @Schema(description = "작성시간", example = "2024-11-19T00:52:47.534061")
        LocalDateTime createdAt;
        @Schema(description = "프로젝트 링크 리스트")
        private List<ProjectResponse.ProjectCreateResponse.LinkResponse> links; // 링크 리스트 추가


        @Getter
        @Setter
        public static class LinkResponse {
            @Schema(description = "링크 타입", example = "github")
            private String type;
            @Schema(description = "url", example = "https://github.com/bboggo")
            private String url;
            @Schema(description = "정렬 순서", example = "1")
            private Integer orderIndex;


            public LinkResponse(String type, String url, Integer orderIndex) {
                this.type = type;
                this.url = url;
                this.orderIndex = orderIndex;
            }
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectDetailResponse {
        @Schema(description = "프로젝트 ID", example = "1")
        Long id;
        //@Schema(description = "작성자 ID", example = "1")
        CommunityResponse.MemberInfo writer;
        @Schema(description = "프로젝트  제목", example = "김민지의 프로젝트 ~~")
        String projectTitle;
        @Schema(description = "프로젝트 내용", example = "프로젝트 내용~~")
        String projectContent;
        @NotNull(message = "This field must not be null.")
        @Schema(description = "프로젝트 개요", example = "프로젝트 개요~~")
        String projectSummary;
        @Schema(description = "프로젝트 분류", example = "APP")
        ProjectCategory projectCategory;
        @Schema(description = "태그", example = "[\"태그1\", \"태그2\", \"태그3\"]")
        List<String> tags;
        @Schema(description = "기술 스택", example = "[\"Java\", \"Spring\", \"AWS\"]")
        List<String> projectTechStacks;
        @Schema(description = "프로젝트 이미지 url", example = "이미지url")
        String projectImageUrl;
        @Schema(description = "작성시간", example = "2024-11-19T00:52:47.534061")
        LocalDateTime createdAt;
        @Schema(description = "프로젝트 링크 리스트")
        private List<ProjectResponse.ProjectCreateResponse.LinkResponse> links; // 링크 리스트 추가

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
    public static class ProjectUpdateResponse {
        @Schema(description = "프로젝트 ID", example = "1")
        Long id;
        //@Schema(description = "작성자 ID", example = "1")
        //CommunityResponse.MemberInfo writer;
        @Schema(description = "프로젝트  제목", example = "김민지의 프로젝트 ~~")
        String projectTitle;
        @Schema(description = "프로젝트 내용", example = "프로젝트 내용~~")
        String projectContent;
        @NotNull(message = "This field must not be null.")
        @Schema(description = "프로젝트 개요", example = "프로젝트 개요~~")
        String projectSummary;
        @Schema(description = "프로젝트 분류", example = "APP")
        ProjectCategory projectCategory;
        @Schema(description = "태그", example = "[\"태그1\", \"태그2\", \"태그3\"]")
        List<String> tags;
        @Schema(description = "기술 스택", example = "[\"Java\", \"Spring\", \"AWS\"]")
        List<String> projectTechStacks;
        @Schema(description = "프로젝트 이미지 url", example = "이미지url")
        String projectImageUrl;
        @Schema(description = "작성시간", example = "2024-11-19T00:52:47.534061")
        LocalDateTime createdAt;
        @Schema(description = "프로젝트 링크 리스트")
        private List<ProjectResponse.ProjectCreateResponse.LinkResponse> links; // 링크 리스트 추가

    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor

    public static class ProjectListResponse {
        @Schema(description = "프로젝트 ID", example = "1")
        Long id;

        CommunityResponse.MemberInfo writer;

        @Schema(description = "프로젝트  제목", example = "김민지의 프로젝트 ~~")
        String projectTitle;
        //        @Schema(description = "프로젝트 내용", example = "프로젝트 내용~~")
//        String projectContent;
//        @NotNull(message = "This field must not be null.")
//        @Schema(description = "프로젝트 개요", example = "프로젝트 개요~~")
//        String projectSummary;
        @Schema(description = "프로젝트 분류", example = "APP")
        ProjectCategory projectCategory;

        @Schema(description = "태그", example = "[\"태그1\", \"태그2\", \"태그3\"]")
        List<String> tags;
        //        @Schema(description = "기술 스택", example = "[\"Java\", \"Spring\", \"AWS\"]")
//        List<String> projectTechStacks;
        @Schema(description = "프로젝트 이미지 url", example = "이미지url")
        String projectImageUrl;

        @Schema(description = "작성시간", example = "2024-11-19T00:52:47.534061")
        LocalDateTime createdAt;

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
    public static class OtherProjectResponse {
        @Schema(description = "프로젝트 ID", example = "1")
        Long id;
        @Schema(description = "프로젝트  제목", example = "김민지의 프로젝트 ~~")
        String projectTitle;

        @Schema(description = "프로젝트 분류", example = "APP")
        ProjectCategory projectCategory;

        @Schema(description = "기술 스택", example = "[\"Java\", \"Spring\", \"AWS\"]")
        List<String> projectTechStacks;
        @Schema(description = "프로젝트 이미지 url", example = "이미지url")
        String projectImageUrl;
        @Schema(description = "작성시간", example = "2024-11-19T00:52:47.534061")
        LocalDateTime createdAt;


    }
}