package com.backend.devfordev.dto.CommunityDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommunityAnswerRequest {
    @Schema(description = "답변 내용", example = "답변 내용입니다.")
    private String content;
}
