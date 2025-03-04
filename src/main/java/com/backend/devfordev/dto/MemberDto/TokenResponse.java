package com.backend.devfordev.dto.MemberDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class TokenResponse {

    @Getter
    @AllArgsConstructor
    public static class AccessTokenResponse {
        @Schema(description = "accessToken", example = "Bearer eyJhbGciOiJIUzUxMi...")
        private String accessToken;
    }
}
