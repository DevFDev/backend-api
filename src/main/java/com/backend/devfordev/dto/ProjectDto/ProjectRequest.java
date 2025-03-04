package com.backend.devfordev.dto.ProjectDto;


import com.backend.devfordev.domain.enums.ProjectCategory;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jdk.dynalink.linker.LinkRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class ProjectRequest {
    /*
    TODO: 기술스택(여러개 가능)과 프로젝트 개요 추가
     */

    @Getter
    @Setter
    public static class ProjectCreateRequest {
        @NotNull(message = "This field must not be null.")
        @Schema(description = "프로젝트  제목", example = "김민지의 프로젝트 ~~")
        String projectTitle;

        @NotNull(message = "This field must not be null.")
        @Schema(description = "프로젝트 내용", example = "프로젝트 내용~~")
        String projectContent;

        @NotNull(message = "This field must not be null.")
        @Schema(description = "프로젝트 개요", example = "프로젝트 개요~~")
        String projectSummary;

        @Schema(description = "프로젝트 분류", example = "APP")
        @NotNull(message = "This field must not be null.")
        ProjectCategory projectCategory;

        @ArraySchema(
                schema = @Schema(description = "기술스택", example = "기술스택1"),
                arraySchema = @Schema(example = "[\"스택1\", \"스택2\", \"스택3\", \"스택4\"]")
        )
        List<String> projectTechStacks;

        @ArraySchema(
                schema = @Schema(description = "태그", example = "태그1"),
                arraySchema = @Schema(example = "[\"태그1\", \"태그2\", \"태그3\", \"태그4\"]")
        )
        List<String> tags;
//        @Schema(description = "프로젝트 이미지 url", example = "이미지url")
//        String projectImageUrl;
        @Schema(description = "프로젝트 링크 리스트")
        private List<LinkRequest> links;

        @Getter
        @Setter
        public static class LinkRequest {

            @Schema(description = "링크 타입", example = "github")
            private String type;
            @Schema(description = "url", example = "https://github.com/bboggo")
            private String url;

        }
    }

    @Getter
    @Setter
    public static class ProjectUpdateRequest {
        @NotNull(message = "This field must not be null.")
        @Schema(description = "프로젝트  제목", example = "김민지의 프로젝트 ~~")
        String projectTitle;

        @NotNull(message = "This field must not be null.")
        @Schema(description = "프로젝트 내용", example = "프로젝트 내용~~")
        String projectContent;

        @NotNull(message = "This field must not be null.")
        @Schema(description = "프로젝트 개요", example = "프로젝트 개요~~")
        String projectSummary;

        @Schema(description = "프로젝트 분류", example = "APP")
        @NotNull(message = "This field must not be null.")
        ProjectCategory projectCategory;

        @ArraySchema(
                schema = @Schema(description = "기술스택", example = "기술스택1"),
                arraySchema = @Schema(example = "[\"스택1\", \"스택2\", \"스택3\", \"스택4\"]")
        )
        List<String> projectTechStacks;

        @ArraySchema(
                schema = @Schema(description = "태그", example = "태그1"),
                arraySchema = @Schema(example = "[\"태그1\", \"태그2\", \"태그3\", \"태그4\"]")
        )
        List<String> tags;
        //        @Schema(description = "프로젝트 이미지 url", example = "이미지url")
//        String projectImageUrl;
        @Schema(description = "프로젝트 링크 리스트")
        private List<LinkRequest> links;

        @Getter
        @Setter
        public static class LinkRequest {

            @Schema(description = "링크 타입", example = "github")
            private String type;
            @Schema(description = "url", example = "https://github.com/bboggo")
            private String url;

        }

    }

}
