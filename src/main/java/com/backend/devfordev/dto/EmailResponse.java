package com.backend.devfordev.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record EmailResponse(
        @Schema(description = "회원 이메일", example = "xxx@naver.com")
        String email
) {

}