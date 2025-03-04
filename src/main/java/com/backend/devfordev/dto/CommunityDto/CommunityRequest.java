package com.backend.devfordev.dto.CommunityDto;

import com.backend.devfordev.domain.enums.CommunityCategory;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CommunityRequest {

    @Getter
    public static class CommunityCreateRequest {
        @NotNull(message = "This field must not be null.")
        @Schema(description = "커뮤니티 카테고리", example = "SKILL")
        CommunityCategory communityCategory;
        @NotBlank(message = "This field must not be null.")
        @Schema(description = "커뮤니티 제목", example = "스프링부트 너무 어려워요")
        String communityTitle;
        @NotBlank(message = "This field must not be null.")
        @Schema(description = "커뮤니티 내용", example = "인기유저 5명을 조회하려고 하는데 redis로 캐싱을 하는게 좋을까요 아니면 스케줄링으로 처리하는게 좋을까요")
        String communityContent;

        @Schema(description = "커뮤니티 답변 동의 여부", example = "true")
        Boolean isComment;
    }


    @Getter
    public static class CommunityUpdateRequest {
        @NotNull(message = "This field must not be null.")
        @Schema(description = "커뮤니티 카테고리", example = "SKILL")
        CommunityCategory communityCategory;

        @NotBlank(message = "This field must not be null.")
        @Schema(description = "커뮤니티 제목", example = "커뮤니티 제목1")
        String communityTitle;

        @NotBlank(message = "This field must not be null.")
        @Schema(description = "커뮤니티 내용", example = "커뮤니티 내용1")
        String communityContent;
    }

}
