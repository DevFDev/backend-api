package com.backend.devfordev.dto.CommunityDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class CommunityCommentResponse {

    @Schema(description = "댓글 ID", example = "1")
    private Long commentId;

    @Schema(description = "부모 댓글 ID (없을 경우 null)", example = "null")
    private Long parentCommentId;

    @Schema(description = "댓글 내용", example = "댓글입니다")
    private String content;

    @Schema(description = "작성자 ID", example = "1")
    Long writer;

    @Schema(description = "작성일시", example = "2024-12-01T12:34:56")
    private LocalDateTime createdAt;

    @Schema(description = "답글 리스트 (없을 경우 빈 리스트)", example = "[]")
    private List<CommunityCommentResponse> replies;

}
