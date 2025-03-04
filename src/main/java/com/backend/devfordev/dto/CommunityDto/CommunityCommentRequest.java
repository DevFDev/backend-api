package com.backend.devfordev.dto.CommunityDto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommunityCommentRequest {
    @NotNull(message = "This field must not be null.")
    @Schema(description = "댓글 내용", example = "댓글입니다")
    private String content;  // 댓글 내용
    @Schema(description = "부모 댓글 ID (최상위 댓글이면 null)", example = "null")
    private Long parentId;
}