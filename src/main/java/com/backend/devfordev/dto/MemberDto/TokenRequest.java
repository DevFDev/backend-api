package com.backend.devfordev.dto.MemberDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenRequest {
    @Schema(description = "기존 accessToken", example = "Bearer eyJhbGciOiJIUzUxMi...")
    private String oldAccessToken;

    @Schema(description = "refreshToken", example = "Bearer eyJhbGciOiJIUzUxMi...")
    private String refreshToken;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class RefreshTokenRequest {
        private String oldAccessToken;
        private String refreshToken;
    }
}
