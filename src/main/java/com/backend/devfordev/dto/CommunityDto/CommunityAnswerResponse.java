package com.backend.devfordev.dto.CommunityDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommunityAnswerResponse {
    @Schema(description = "답변 ID", example = "1")
    private Long id;
    @Schema(description = "커뮤니티 게시글 ID", example = "1")
    private Long communityId;
    private CommunityResponse.MemberInfo member;
    @Schema(description = "답변 내용", example = "답변 내용입니다.")
    private String content;
    @Schema(description = "작성일시", example = "2024-12-01T12:34:56")
    private LocalDateTime createdAt;
}
